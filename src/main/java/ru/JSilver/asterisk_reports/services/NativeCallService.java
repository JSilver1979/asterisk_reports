package ru.JSilver.asterisk_reports.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk_reports.dto.NativeCallDto;
import ru.JSilver.asterisk_reports.dto.NativeStatsDto;
import ru.JSilver.asterisk_reports.dto.OneCallDto;
import ru.JSilver.asterisk_reports.repos.NativeCallRepository;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class NativeCallService {
    private final NativeCallRepository nativeCallRepository;

//    public List<NativeCallDto> getNative() {
//        return nativeCallRepository.nativeQuery();
//    }

    public NativeStatsDto getNativeStats(String fromDate, String toDate) {

        NativeStatsDto nativeStatsDto = new NativeStatsDto();
        List<NativeCallDto> listForStats = nativeCallRepository.nativeQuery(fromDate, toDate);

        Map<String, OneCallDto> trueMap = new HashMap<>();

        for (int i = 0; i < listForStats.size(); i++) {
            if (!trueMap.containsKey(listForStats.get(i).getLinkedId())) {
                if (listForStats.get(i).getDisposition().equals("ANSWERED")) {
                    trueMap.put(listForStats.get(i).getLinkedId(), new OneCallDto(true, false,false, 0));
                }
                if (listForStats.get(i).getDisposition().equals("NO ANSWER")) {
                    trueMap.put(listForStats.get(i).getLinkedId(), new OneCallDto(false, true,false, listForStats.get(i).getResultRows()));
                }
                if (listForStats.get(i).getDisposition().equals("BUSY")) {
                    trueMap.put(listForStats.get(i).getLinkedId(), new OneCallDto(false, false, true,0));
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

        for (Map.Entry<String,OneCallDto> entry : trueMap.entrySet()) {
            if (entry.getValue().answeredByOperator()) {
                nativeStatsDto.setAnsweredCount(nativeStatsDto.getAnsweredCount() + 1);
            }
            if (entry.getValue().droppedInQueue()) {
                nativeStatsDto.setQueuedDropCallsCount(nativeStatsDto.getQueuedDropCallsCount() + 1);
            }
            if (entry.getValue().busyCall()) {
                nativeStatsDto.setBusyCallCount(nativeStatsDto.getBusyCallCount() + 1);
            }
            if (entry.getValue().notAnsweredByOperators()) {
                nativeStatsDto.setNonAnsweredByOperatorsCount(nativeStatsDto.getNonAnsweredByOperatorsCount() + 1);
            }
            if (entry.getValue().notAnsweredByQueue()) {
                nativeStatsDto.setNonAnsweredByQueueCount(nativeStatsDto.getNonAnsweredByQueueCount() + 1);
            }
        }
        nativeStatsDto.countNonAnsweredCalls();
        nativeStatsDto.setTotalCalls(trueMap.size());
        return nativeStatsDto;
    }
}
