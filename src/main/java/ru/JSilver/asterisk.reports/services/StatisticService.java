package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.dto.CallItemDto;
import ru.JSilver.asterisk.reports.dto.StatisticDto;
import ru.JSilver.asterisk.reports.tools.CallStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticService {

    private final CallsService callsService;

    public StatisticDto getTrueStats (LocalDateTime dateFrom, LocalDateTime dateTo, String group) {
        StatisticDto stats = new StatisticDto();
        List<CallItemDto> callsList = callsService.getCallItems(dateFrom, dateTo, group);
        for (CallItemDto item : callsList) {
            stats.setTotalCalls(stats.getTotalCalls() + 1);
            if (item.getFinalStatus().equals(CallStatus.ANSWERED.getStatus())) {
                stats.setAnsweredCount(stats.getAnsweredCount() + 1);
            }
            if (item.getFinalStatus().equals(CallStatus.NO_ANSWER_BY_QUEUE.getStatus())) {
                stats.setNonAnsweredByQueueCount(stats.getNonAnsweredByQueueCount() + 1);
                stats.setTotalNonAnsweredCalls(stats.getTotalNonAnsweredCalls() + 1);
            }
            if (item.getFinalStatus().equals(CallStatus.NO_ANSWER_BY_OPERATOR.getStatus())) {
                stats.setNonAnsweredByOperatorsCount(stats.getNonAnsweredByOperatorsCount() + 1);
                stats.setTotalNonAnsweredCalls(stats.getTotalNonAnsweredCalls() + 1);
            }
        }

        return stats;
    }
}
