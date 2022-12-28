package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.data.StatisticEntity;
import ru.JSilver.asterisk.reports.dto.StatItemDto;
import ru.JSilver.asterisk.reports.dto.StatisticDto;
import ru.JSilver.asterisk.reports.repos.StatisticCallsRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final StatisticCallsRepository statisticCallsRepository;

    public StatisticDto collectStatistic(String fromDate, String toDate, String group) {
        StatisticDto statsDto = new StatisticDto();
        List<StatisticEntity> listForStats = statisticCallsRepository.getCallsForStatistic(fromDate, toDate, group);

        Map<String, StatItemDto> trueMap = new HashMap<>();

        for (int i = 0; i < listForStats.size(); i++) {
            if (!trueMap.containsKey(listForStats.get(i).getLinkedId())) {
                if (listForStats.get(i).getDisposition().equals("ANSWERED")) {
                    trueMap.put(listForStats.get(i).getLinkedId(), new StatItemDto(true, false,false, 0));
                }
                if (listForStats.get(i).getDisposition().equals("NO ANSWER")) {
                    trueMap.put(listForStats.get(i).getLinkedId(), new StatItemDto(false, true,false, listForStats.get(i).getResultRows()));
                }
                if (listForStats.get(i).getDisposition().equals("BUSY")) {
                    trueMap.put(listForStats.get(i).getLinkedId(), new StatItemDto(false, false, true,0));
                }
            }
            else {
                if (listForStats.get(i).getDisposition().equals("ANSWERED")) {
                    trueMap.get(listForStats.get(i).getLinkedId()).setAnswered(true);
                }
                if (listForStats.get(i).getDisposition().equals("NO ANSWER")) {
                    trueMap.get(listForStats.get(i).getLinkedId()).setNoAnswer(true);
                    trueMap.get(listForStats.get(i).getLinkedId()).setNoAnswerRows(listForStats.get(i).getResultRows());
                }
                if (listForStats.get(i).getDisposition().equals("BUSY")) {
                    trueMap.get(listForStats.get(i).getLinkedId()).setBusy(true);
                }
            }
        }

        for (Map.Entry<String,StatItemDto> entry : trueMap.entrySet()) {
            if (entry.getValue().answeredByOperator()) {
                statsDto.setAnsweredCount(statsDto.getAnsweredCount() + 1);
            }
            if (entry.getValue().droppedInQueue()) {
                statsDto.setQueuedDropCallsCount(statsDto.getQueuedDropCallsCount() + 1);
            }
            if (entry.getValue().busyCall()) {
                statsDto.setBusyCallCount(statsDto.getBusyCallCount() + 1);
            }
            if (entry.getValue().notAnsweredByOperators()) {
                statsDto.setNonAnsweredByOperatorsCount(statsDto.getNonAnsweredByOperatorsCount() + 1);
            }
            if (entry.getValue().notAnsweredByQueue()) {
                statsDto.setNonAnsweredByQueueCount(statsDto.getNonAnsweredByQueueCount() + 1);
            }
        }
        statsDto.countNonAnsweredCalls();
        statsDto.setTotalCalls(trueMap.size());
        return statsDto;
    }
}
