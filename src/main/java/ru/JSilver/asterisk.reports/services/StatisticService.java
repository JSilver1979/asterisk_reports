package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.dto.CallItemDto;
import ru.JSilver.asterisk.reports.dto.StatisticDto;
import ru.JSilver.asterisk.reports.tools.CallStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticService {

    private final CallsService callsService;

    public StatisticDto getTrueStats (LocalDateTime dateFrom, LocalDateTime dateTo, String group) {
        StatisticDto stats = new StatisticDto();
        int days = dateTo.getDayOfYear()-dateFrom.getDayOfYear() + 1;
        int waitTime = 0;
        int talkTime = 0;
        List<Integer> hoursTalkTime = new ArrayList<>(Collections.nCopies(16,0));
        List<CallItemDto> callsList = callsService.getCallItems(dateFrom, dateTo, group);
        for (CallItemDto item : callsList) {
            int itemHour = item.getCallTime().getHour();
            waitTime = calculateSeconds(item.getCallWaitingTime()) + waitTime;

            stats.setTotalCalls(stats.getTotalCalls() + 1);
            setHours(stats.getTotalHours(), itemHour, 1);
            setHours(stats.getHourWaitTime(), itemHour, calculateSeconds(item.getCallWaitingTime()));

            if (item.getFinalStatus().equals(CallStatus.ANSWERED.getStatus())) {
                stats.setAnsweredCount(stats.getAnsweredCount() + 1);
                talkTime = calculateSeconds(item.getOperatorAnswerDuration()) + talkTime;
                setHours(stats.getAnsweredHours(), itemHour, 1);
                setHours(hoursTalkTime, itemHour, calculateSeconds(item.getOperatorAnswerDuration()));
            }
            if (item.getFinalStatus().equals(CallStatus.NO_ANSWER_BY_QUEUE.getStatus())) {
                stats.setNonAnsweredByQueueCount(stats.getNonAnsweredByQueueCount() + 1);
                setHours(stats.getNonAQHours(), itemHour, 1);

                stats.setTotalNonAnsweredCalls(stats.getTotalNonAnsweredCalls() + 1);
                setHours(stats.getTotalNonAHours(), itemHour, 1);
            }
            if (item.getFinalStatus().equals(CallStatus.NO_ANSWER_BY_OPERATOR.getStatus())) {
                stats.setNonAnsweredByOperatorsCount(stats.getNonAnsweredByOperatorsCount() + 1);
                setHours(stats.getNonAOperHours(), itemHour, 1);

                stats.setTotalNonAnsweredCalls(stats.getTotalNonAnsweredCalls() + 1);
                setHours(stats.getTotalNonAHours(), itemHour, 1);
            }
        }
        stats.setAnsweredCountAVG(calculateAVG(stats.getAnsweredCount(), days,2));
        stats.setNonAnsweredByQueueCountAVG(calculateAVG(stats.getNonAnsweredByQueueCount(),days,2));
        stats.setNonAnsweredByOperatorsCountAVG(calculateAVG(stats.getNonAnsweredByOperatorsCount(),days,2));
        stats.setTotalNonAnsweredCallsAVG(calculateAVG(stats.getTotalNonAnsweredCalls(),days,2));
        stats.setTotalCallsAVG(calculateAVG(stats.getTotalCalls(),days,2));
        stats.setTotalAnsByPercent(calculatePercent(stats.getTotalCalls(),stats.getAnsweredCount()));
        stats.setWaitTimeAVG(LocalTime.MIN.plusSeconds(calculateAVG(waitTime, stats.getTotalCalls(), 0).intValue()));
        stats.setTalkTimeAVG(LocalTime.MIN.plusSeconds(calculateAVG(talkTime, stats.getAnsweredCount(), 0).intValue()));
        setAVGHours(stats.getAnsweredHours(), stats.getAnsweredHoursAVG(),days);
        setAVGHours(stats.getNonAOperHours(), stats.getNonAOperHoursAVG(), days);
        setAVGHours(stats.getNonAQHours(), stats.getNonAQHoursAVG(), days);
        setAVGHours(stats.getTotalNonAHours(), stats.getTotalNonAHoursAVG(), days);
        setAVGHours(stats.getTotalHours(), stats.getTotalHoursAVG(), days);
        setAVGHours(stats.getHourWaitTime(), stats.getHourWaitTimeAVG(), stats.getTotalHours());
        setAVGHours(hoursTalkTime,stats.getHourTalkTimeAVG(),stats.getAnsweredHours());
        setPercentAnswersByHours(stats.getTotalHours(),stats.getAnsweredHours(),stats.getHourAnswerByPercent());
        return stats;
    }

    private int calculateSeconds (LocalTime time) {
        if (time == null) {
            return 0;
        }
        return time.getMinute() * 60 + time.getSecond();
    }

    private BigDecimal calculateAVG(Integer value, int div, int scale) {
        if (div == 0 || value == 0) {
            return new BigDecimal(0);
        }
        return new BigDecimal((float) value / div).setScale(scale, RoundingMode.HALF_UP);
    }

    private BigDecimal calculatePercent(Integer total, Integer part) {
        if (part == 0) {
            return new BigDecimal(0);
        }
        return new BigDecimal((float) 100 / total * part).setScale(2,RoundingMode.HALF_UP);
    }

    private void setPercentAnswersByHours(List<Integer> totalList, List<Integer> partList,List<BigDecimal> percentList) {
        for (int i = 0; i < totalList.size(); i++) {
            percentList.add(i,calculatePercent(totalList.get(i), partList.get(i)));
        }
    }

    private void setHours(List<Integer> hourList, int hour, int inc) {
        if (hour < 8) {
            hourList.set(0, hourList.get(0) + inc);
        } else if (hour > 21) {
            hourList.set(15, hourList.get(15) + inc);
        } else {
            hourList.set(hour - 7, hourList.get(hour-7) + inc);
        }
    }

    private void setAVGHours(List<Integer> hourList, List<LocalTime> avgList, List<Integer> countList) {
        for (int i = 0; i < hourList.size(); i++) {
            avgList.add(i, LocalTime.MIN.plusSeconds(calculateAVG(hourList.get(i), countList.get(i), 2).intValue()));
        }
    }

    private void setAVGHours(List<Integer> hourList, List<BigDecimal> avgList, int daysCount) {
        for (int i = 0; i < hourList.size(); i++) {
            avgList.add(i, calculateAVG(hourList.get(i),daysCount,2));
        }
    }
}
