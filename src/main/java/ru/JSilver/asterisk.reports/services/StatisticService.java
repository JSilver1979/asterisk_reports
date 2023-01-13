package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.data.NewStatisticEntity;
import ru.JSilver.asterisk.reports.dto.StatisticDto;
import ru.JSilver.asterisk.reports.repos.NewStatisticRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticService {

    private final NewStatisticRepository newStatsRepo;

    public StatisticDto newStatisticEntityList (String fromDate, String toDate, String group) {
        StatisticDto stats = new StatisticDto();
        List<NewStatisticEntity> entities = newStatsRepo.getCallsForStats(fromDate, toDate, "%" + group + "%");
        for (NewStatisticEntity entity: entities) {
            stats.setTotalCalls(stats.getTotalCalls() + 1);
            if (entity.getStatuses().contains("ANSWERED") && entity.getRowsNumber() > 1) {
                stats.setAnsweredCount(stats.getAnsweredCount() + 1);
            }
            else if (entity.getStatuses().contains("ANSWERED") && entity.getRowsNumber() < 2) {
                stats.setNonAnsweredByQueueCount(stats.getNonAnsweredByQueueCount() + 1);
                stats.setTotalNonAnsweredCalls(stats.getTotalNonAnsweredCalls() + 1);
            }
            else if (entity.getStatuses().contains("NO ANSWER")) {
                stats.setNonAnsweredByOperatorsCount(stats.getNonAnsweredByOperatorsCount() + 1);
                stats.setTotalNonAnsweredCalls(stats.getTotalNonAnsweredCalls() + 1);
            }
        }

        return stats;
    }
}
