package ru.JSilver.asterisk.reports.converters;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.JSilver.asterisk.reports.dto.CallItemDto;
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
public class CallToStatsConverter {
    private int days;
    private int waitTime;
    private int talkTime;
    private int callHour;
    private List<Integer> hoursTalkTime;
    private List<Integer> hoursWaitTime;
    private StatisticDto statDto;
    private final StatisticCalculator calculator;

    public StatisticDto buildStats(List<CallItemDto> callsList, int days) {
        this.days = days;
        this.clear();
        statDto.setTotalCalls(callsList.size());

        for (CallItemDto item : callsList) {
            callHour = item.getCallTime().getHour();
            HoursStatDto hourStat = statDto.getHourStats().get(callHour);
            hourStat.setTotal(hourStat.getTotal() + 1);

            waitTime += calculator.getSeconds(item.getCallWaitingTime());
            incrementInList(hoursWaitTime, calculator.getSeconds(item.getCallWaitingTime()));

            setSpecializedStats(item);
        }

        setFinalStats();

        return statDto;
    }

    private void setFinalStats() {
        statDto.setAnsweredCountAVG(calculator.getAVG(statDto.getAnsweredCount(), days, 2));
        statDto.setNonAnsweredByQueueCountAVG(calculator.getAVG(statDto.getNonAnsweredByQueueCount(), days, 2));
        statDto.setNonAnsweredByOperatorsCountAVG(calculator.getAVG(statDto.getNonAnsweredByOperatorsCount(), days, 1));
        statDto.setTotalNonAnsweredCallsAVG(calculator.getAVG(statDto.getTotalNonAnsweredCalls(), days, 2));
        statDto.setTotalCallsAVG(calculator.getAVG(statDto.getTotalCalls(), days, 2));

        statDto.setWaitTimeAVG(LocalTime.MIN.plusSeconds(calculator.getAVG(waitTime, statDto.getTotalCalls(), 0).intValue()));
        statDto.setTalkTimeAVG(LocalTime.MIN.plusSeconds(calculator.getAVG(talkTime, statDto.getAnsweredCount(), 0).intValue()));

        for (int i = 0; i < statDto.getHourStats().size(); i++) {
            HoursStatDto hourItem = statDto.getHourStats().get(i);
            hourItem.setHour(i + ":00 - " + (i + 1) + ":00");
            hourItem.setAnsweredAVG(calculator.getAVG(hourItem.getAnswered(), days, 2));
            hourItem.setNaQueueAVG(calculator.getAVG(hourItem.getNaQueue(), days, 2));
            hourItem.setNaOperatorAVG(calculator.getAVG(hourItem.getNaOperator(), days, 2));
            hourItem.setNaTotalAVG(calculator.getAVG(hourItem.getNaTotal(), days, 2));
            hourItem.setTotalAVG(calculator.getAVG(hourItem.getTotal(), days, 2));
            hourItem.setWaitTimeAVG(LocalTime.MIN.plusSeconds(
                    calculator.getAVG(hoursWaitTime.get(i), hourItem.getTotal(), 0)
                            .intValue()));
            hourItem.setTalkTimeAVG(LocalTime.MIN.plusSeconds(
                    calculator.getAVG(hoursTalkTime.get(i), hourItem.getAnswered(), 0)
                            .intValue()));
            hourItem.setAnsweredPercent(calculator.getPercent(hourItem.getTotal(), hourItem.getAnswered()));
            statDto.setTotalAnsByPercent(calculator.getPercent(statDto.getTotalCalls(), statDto.getAnsweredCount()));
        }
    }

    private void setSpecializedStats(CallItemDto item) {
        if (item.getFinalStatus().equals(CallStatus.ANSWERED.getStatus())) {
            statDto.setAnsweredCount(statDto.getAnsweredCount() + 1);
            talkTime += calculator.getSeconds(item.getOperatorAnswerDuration());
            statDto.getHourStats().get(callHour).setAnswered(
                    statDto.getHourStats().get(callHour).getAnswered() + 1);
            incrementInList(hoursTalkTime, calculator.getSeconds(item.getOperatorAnswerDuration()));
        }

        if (item.getFinalStatus().equals(CallStatus.NO_ANSWER_BY_QUEUE.getStatus())) {
            statDto.setNonAnsweredByQueueCount(statDto.getNonAnsweredByQueueCount() + 1);
            statDto.getHourStats().get(callHour).setNaQueue(
                    statDto.getHourStats().get(callHour).getNaQueue() + 1);
            updateNonAnswered();
        }

        if (item.getFinalStatus().equals(CallStatus.NO_ANSWER_BY_OPERATOR.getStatus())) {
            statDto.setNonAnsweredByOperatorsCount(statDto.getNonAnsweredByOperatorsCount() + 1);
            statDto.getHourStats().get(callHour).setNaOperator(
                    statDto.getHourStats().get(callHour).getNaOperator() + 1);

            updateNonAnswered();
        }
    }

    private void clear() {
        setWaitTime(0);
        setTalkTime(0);
        setCallHour(0);
        setHoursTalkTime(new ArrayList<>(Collections.nCopies(24, 0)));
        setHoursWaitTime(new ArrayList<>(Collections.nCopies(24, 0)));
        statDto = new StatisticDto();
    }

    private void incrementInList(List<Integer> list, int inc) {
        list.set(callHour, list.get(callHour) + inc);
    }

    private void updateNonAnswered() {
        statDto.setTotalNonAnsweredCalls(statDto.getTotalNonAnsweredCalls() + 1);
        statDto.getHourStats().get(callHour).setNaTotal(
                statDto.getHourStats().get(callHour).getNaTotal() + 1);
    }
}
