package ru.JSilver.asterisk.reports.converters;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.JSilver.asterisk.reports.dto.CallQueueDto;
import ru.JSilver.asterisk.reports.dto.HoursStatDto;
import ru.JSilver.asterisk.reports.dto.StatisticDto;
import ru.JSilver.asterisk.reports.tools.CallStatus;
import ru.JSilver.asterisk.reports.tools.StatisticCalculator;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Data
public class StatsConverterV3 {
    private int days;
    private int waitTime;
    private int talkTime;
    private int callHour;
    private List<Integer> hoursTalkTime;
    private List<Integer> hoursWaitTime;
    private StatisticDto statDto;
    private final StatisticCalculator calc;

    public StatisticDto buildStats(List<CallQueueDto> queueList, int days) {
        clear();
        this.days = days;
        statDto.setTotalCalls(queueList.size());

        for(CallQueueDto item : queueList) {
            waitTime += calc.getSeconds(item.getWaitTime());

            setHourStats(item);

            setSpecializedStats(item);

        }

        setFinalStats();

        return statDto;
    }

    private void setFinalStats() {
        statDto.setAnsweredCountAVG(calc.getAVG(statDto.getAnsweredCount(), days, 2));
        statDto.setNonAnsweredByQueueCountAVG(calc.getAVG(statDto.getNonAnsweredByQueueCount(), days, 2));
        statDto.setNonAnsweredByOperatorsCountAVG(calc.getAVG(statDto.getNonAnsweredByOperatorsCount(), days, 1));
        statDto.setBusyQueueCountAVG(calc.getAVG(statDto.getBusyQueueCount(), days, 2));
        statDto.setTotalNonAnsweredCallsAVG(calc.getAVG(statDto.getTotalNonAnsweredCalls(), days, 2));
        statDto.setTotalCallsAVG(calc.getAVG(statDto.getTotalCalls(), days, 2));

        statDto.setWaitTimeAVG(LocalTime.MIN.plusSeconds(
                calc.getAVG(waitTime, statDto.getTotalCalls(), 0).intValue()));
        statDto.setTalkTimeAVG(LocalTime.MIN.plusSeconds(
                calc.getAVG(talkTime, statDto.getAnsweredCount(), 0).intValue()));
        statDto.setTotalAnsByPercent(
                calc.getPercent(statDto.getTotalCalls(), statDto.getAnsweredCount()));

        setFinalHourStats(statDto.getHourStats());

    }

    private void setFinalHourStats(List<HoursStatDto> list) {
        for (int i = 0; i < list.size(); i++) {
            HoursStatDto hourItem = list.get(i);

            hourItem.setHour(i + ":00 - " + (i + 1) + ":00");
            hourItem.setAnsweredAVG(calc.getAVG(hourItem.getAnswered(), days, 2));
            hourItem.setNaQueueAVG(calc.getAVG(hourItem.getNaQueue(), days, 2));
            hourItem.setNaOperatorAVG(calc.getAVG(hourItem.getNaOperator(), days, 2));
            hourItem.setNaTotalAVG(calc.getAVG(hourItem.getNaTotal(), days, 2));
            hourItem.setBusyQueueAVG(calc.getAVG(hourItem.getBusyQueue(), days, 2));
            hourItem.setTotalAVG(calc.getAVG(hourItem.getTotal(), days, 2));

            hourItem.setWaitTimeAVG(LocalTime.MIN.plusSeconds(
                    calc.getAVG(hoursWaitTime.get(i), hourItem.getTotal(), 0).intValue()));
            hourItem.setTalkTimeAVG(LocalTime.MIN.plusSeconds(
                    calc.getAVG(hoursTalkTime.get(i),hourItem.getAnswered(), 0).intValue()));
            hourItem.setAnsweredPercent(
                    calc.getPercent(hourItem.getTotal(), hourItem.getAnswered()));
        }
    }

    private void setSpecializedStats(CallQueueDto item) {
        String status = item.getQueueStatus();

        if (status.equals(CallStatus.ANSWERED.getStatus())) {
            setAnsweredStats(item);
        }

        if (status.equals(CallStatus.NO_ANSWER_BY_QUEUE.getStatus())) {
            setNoAnsweredByQueue();
        }

        if (status.equals(CallStatus.NO_ANSWER_BY_OPERATOR.getStatus())) {
            setNoAnsweredByAgent();
        }

        if (status.equals(CallStatus.BUSY.getStatus())) {
            setQueueIsBusy();
        }
    }

    private void setQueueIsBusy() {
        statDto.setBusyQueueCount(statDto.getBusyQueueCount() + 1);
        statDto.getHourStats().get(callHour).setBusyQueue(
                statDto.getHourStats().get(callHour).getBusyQueue() + 1);
        updateNonAnswered();
    }

    private void setNoAnsweredByQueue() {
        statDto.setNonAnsweredByQueueCount(statDto.getNonAnsweredByQueueCount() + 1);
        statDto.getHourStats().get(callHour).setNaQueue(
                statDto.getHourStats().get(callHour).getNaQueue() + 1);
        updateNonAnswered();
    }

    private void setNoAnsweredByAgent() {
        statDto.setNonAnsweredByOperatorsCount(statDto.getNonAnsweredByOperatorsCount() + 1);
        statDto.getHourStats().get(callHour).setNaOperator(
                statDto.getHourStats().get(callHour).getNaOperator() + 1);
        updateNonAnswered();
    }

    private void setAnsweredStats(CallQueueDto item) {
        statDto.setAnsweredCount(statDto.getAnsweredCount() + 1);
        talkTime += calc.getSeconds(item.getAnswerDuration());
        statDto.getHourStats().get(callHour).setAnswered(
                statDto.getHourStats().get(callHour).getAnswered() + 1);
        incrementTime(hoursTalkTime, calc.getSeconds(item.getAnswerDuration()));
    }

    private void setHourStats(CallQueueDto queueItem) {
        callHour = queueItem.getQueueTime().getHour(); //? Зачем эта переменная, если можно без нее?
        HoursStatDto hourStat = statDto.getHourStats().get(callHour);
        hourStat.setTotal(hourStat.getTotal() + 1);

        incrementTime(hoursWaitTime, calc.getSeconds(queueItem.getWaitTime()));
    }

    private void incrementTime(List<Integer> timeList, int seconds) {
        timeList.set(callHour, timeList.get(callHour) + seconds);
    }

    private void clear() {
        setWaitTime(0);
        setTalkTime(0);
        setCallHour(0);
        setHoursTalkTime(new ArrayList<>(Collections.nCopies(24,0)));
        setHoursWaitTime(new ArrayList<>(Collections.nCopies(24,0)));
        statDto = new StatisticDto();
    }

    private void updateNonAnswered() {
        statDto.setTotalNonAnsweredCalls(statDto.getTotalNonAnsweredCalls() + 1);
        statDto.getHourStats().get(callHour).setNaTotal(
                statDto.getHourStats().get(callHour).getNaTotal() + 1);
    }
}
