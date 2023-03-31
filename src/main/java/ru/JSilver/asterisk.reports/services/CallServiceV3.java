package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.converters.RowToCallCollectorV3;
import ru.JSilver.asterisk.reports.data.RowEntity;
import ru.JSilver.asterisk.reports.dto.CallItemDtoV3;
import ru.JSilver.asterisk.reports.dto.CallQueueDto;
import ru.JSilver.asterisk.reports.dto.QueueItem;
import ru.JSilver.asterisk.reports.repos.RawRowRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallServiceV3 {
    private final RawRowRepository repository;

    private final RowToCallCollectorV3 collectorV3;

    public List<CallItemDtoV3> getCallItems(LocalDateTime dateFrom, LocalDateTime dateTo, String queue) {
        return collectorV3.collectCalls(getRows(dateFrom, dateTo), queue);
    }

    private List<RowEntity> getRows(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return repository.findAllByCallDateTimeBetweenOrderByCallDateTime(dateFrom, dateTo);
    }

    public List<CallQueueDto> getDetailedCalls(LocalDateTime dateFrom, LocalDateTime dateTo, String queue) {
        List<CallQueueDto> detailedList = new ArrayList<>();
        List<CallItemDtoV3> calls = getCallItems(dateFrom, dateTo, queue);
        for (int i = 0; i < calls.size(); i++) {
            CallItemDtoV3 call = calls.get(i);
            List<QueueItem> queues = call.getHistoryList();
            for (int j = 0; j < queues.size(); j++) {
                QueueItem item = queues.get(j);
                if (item.getQueue().equals(queue)) {
                    CallQueueDto detailedCall = new CallQueueDto();
                    detailedCall.setCallId(call.getLinkedId());
                    detailedCall.setNumber(call.getNumber());
                    detailedCall.setDate(call.getDate());
                    detailedCall.setCallTime(call.getTime());
                    detailedCall.setCallStatus(call.getStatus());
                    detailedCall.setAudioPath(call.getAudioPath());

                    detailedCall.setQueueTime(item.getTimeStart());
                    detailedCall.setWaitTime(item.getTimeWait());
                    detailedCall.setQueueStatus(item.getQueueStatus());

                    if (item.getAgent() != null) {
                        detailedCall.setAgentNumber(item.getAgent());
                        detailedCall.setAnswerTime(item.getAgentAnswerTime());
                        detailedCall.setAnswerDuration(item.getAgentAnswerDuration());
                    }

                    if (queues.size() > 1) {
                        detailedCall.setRedirected(true);
                    } else detailedCall.setRedirected(false);

                    detailedList.add(detailedCall);
                }
            }
        }

        return fixDuplicates(detailedList);
    }

    private List<CallQueueDto> fixDuplicates(List<CallQueueDto> detailedList) {
        Map<String, CallQueueDto> fixedMap = new HashMap<>();

        for (int i = 0; i < detailedList.size(); i++) {
            CallQueueDto item = checkDuplicates(detailedList.get(i), detailedList);
            fixedMap.put(item.getCallId(), item);
        }
        return fixedMap.values().stream()
                .sorted(Comparator.comparing(CallQueueDto::getCallTime))
                .collect(Collectors.toList());
    }

    private CallQueueDto checkDuplicates(CallQueueDto firstItem, List<CallQueueDto> list) {
        for (int i = 0; i < list.size(); i++) {
            CallQueueDto secondItem = list.get(i);
            if (isMergedCall(firstItem, secondItem)) {
                if (firstIsMainItem(firstItem, secondItem)) {
                    CallQueueDto mergedItem = mergeItems(firstItem, secondItem);
                    list.remove(secondItem);
                    return mergedItem;
                } else {
                    CallQueueDto mergedItem = mergeItems(firstItem, secondItem);
                    list.remove(secondItem);
                    return mergedItem;
                }
            }

        }
        return firstItem;
    }

    private boolean isMergedCall(CallQueueDto firstItem, CallQueueDto secondItem) {
        if (firstItem.getCallId().equals(secondItem.getCallId())) return false;

        if (firstItem.getNumber().equals(secondItem.getNumber()))
            if ((firstItem.getQueueTime().plusSeconds(firstItem.getWaitTime().getSecond()).isAfter(secondItem.getCallTime()) || firstItem.getQueueTime().equals(secondItem.getCallTime()))
                    && (firstItem.getCallTime().isBefore(secondItem.getCallTime()) || firstItem.getQueueTime().equals(secondItem.getCallTime()))) {
                return true;
            }
        return false;
    }

    private CallQueueDto mergeItems(CallQueueDto mainItem, CallQueueDto childItem) {
        CallQueueDto mergedItem = new CallQueueDto();

        mergedItem.setCallId(childItem.getCallId());
        mergedItem.setNumber(mainItem.getNumber());
        mergedItem.setDate(mainItem.getDate());
        mergedItem.setCallTime(mainItem.getCallTime());
        mergedItem.setQueueTime(childItem.getQueueTime());
        mergedItem.setWaitTime(childItem.getWaitTime());
        mergedItem.setAgentNumber(childItem.getAgentNumber());
        mergedItem.setAnswerTime(childItem.getAnswerTime());
        mergedItem.setAnswerDuration(childItem.getAnswerDuration());
        mergedItem.setCallStatus(childItem.getQueueStatus());
        mergedItem.setQueueStatus(childItem.getQueueStatus());
        if (mainItem.getAudioPath() == null || mainItem.getAudioPath().isEmpty()) {
            mergedItem.setAudioPath(childItem.getAudioPath());
        } else {
            mergedItem.setAudioPath(mainItem.getAudioPath());
        }

        mergedItem.setRedirected(mainItem.isRedirected());

        return mergedItem;
    }

    private boolean firstIsMainItem(CallQueueDto first, CallQueueDto second) {
        if (first.getCallTime().isBefore(second.getCallTime())) {
            return true;
        }
        return false;
    }
}
