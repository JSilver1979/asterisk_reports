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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticService {

    private final CallsService callsService;

    public StatisticDto getTrueStats (LocalDateTime dateFrom, LocalDateTime dateTo, String group) {
        StatisticDto stats = new StatisticDto();
        int days = dateTo.getDayOfYear()-dateFrom.getDayOfYear() + 1;
        List<CallItemDto> callsList = callsService.getCallItems(dateFrom, dateTo, group);
        for (CallItemDto item : callsList) {
            int itemHour = item.getCallTime().getHour();

            stats.setTotalCalls(stats.getTotalCalls() + 1);
            setHours(stats.getTotalHours(), itemHour);

            if (item.getFinalStatus().equals(CallStatus.ANSWERED.getStatus())) {
                stats.setAnsweredCount(stats.getAnsweredCount() + 1);
                setHours(stats.getAnsweredHours(), itemHour);
            }
            if (item.getFinalStatus().equals(CallStatus.NO_ANSWER_BY_QUEUE.getStatus())) {
                stats.setNonAnsweredByQueueCount(stats.getNonAnsweredByQueueCount() + 1);
                setHours(stats.getNonAQHours(), itemHour);

                stats.setTotalNonAnsweredCalls(stats.getTotalNonAnsweredCalls() + 1);
                setHours(stats.getTotalNonAHours(), itemHour);
            }
            if (item.getFinalStatus().equals(CallStatus.NO_ANSWER_BY_OPERATOR.getStatus())) {
                stats.setNonAnsweredByOperatorsCount(stats.getNonAnsweredByOperatorsCount() + 1);
                setHours(stats.getNonAOperHours(), itemHour);

                stats.setTotalNonAnsweredCalls(stats.getTotalNonAnsweredCalls() + 1);
                setHours(stats.getTotalNonAHours(), itemHour);
            }
        }
        stats.setAnsweredCountAVG(new BigDecimal((float) stats.getAnsweredCount()/days).setScale(2,RoundingMode.HALF_UP));
        stats.setNonAnsweredByQueueCountAVG(new BigDecimal((float) stats.getNonAnsweredByQueueCount()/days).setScale(2,RoundingMode.HALF_UP));
        stats.setNonAnsweredByOperatorsCountAVG(new BigDecimal((float) stats.getNonAnsweredByOperatorsCount()/days).setScale(2,RoundingMode.HALF_UP));
        stats.setTotalNonAnsweredCallsAVG(new BigDecimal((float) stats.getTotalNonAnsweredCalls()/days).setScale(2,RoundingMode.HALF_UP));
        stats.setTotalCallsAVG(new BigDecimal((float) stats.getTotalCalls()/days).setScale(2,RoundingMode.HALF_UP));
        setAVGHours(stats.getAnsweredHours(), stats.getAnsweredHoursAVG(),days);
        setAVGHours(stats.getNonAOperHours(), stats.getNonAOperHoursAVG(), days);
        setAVGHours(stats.getNonAQHours(), stats.getNonAQHoursAVG(), days);
        setAVGHours(stats.getTotalNonAHours(), stats.getTotalNonAHoursAVG(), days);
        setAVGHours(stats.getTotalHours(), stats.getTotalHoursAVG(), days);

        return stats;
    }

    private void setHours(List<Integer> hourList, int hour) {
        if (hour < 8) {
            hourList.set(0, hourList.get(0) + 1);
        } else if (hour > 21) {
            hourList.set(15, hourList.get(15) + 1);
        } else {
            hourList.set(hour - 7, hourList.get(hour-7) + 1);
        }
    }

    private void setAVGHours(List<Integer> hourList, List<BigDecimal> avgList, int daysCount) {
        for (int i = 0; i < hourList.size(); i++) {
            avgList.add(i, new BigDecimal((float) hourList.get(i)/daysCount).setScale(2, RoundingMode.HALF_UP));
        }
    }
}
