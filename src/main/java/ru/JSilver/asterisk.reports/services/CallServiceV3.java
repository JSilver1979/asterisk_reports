package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.converters.RowToCallCollectorV3;
import ru.JSilver.asterisk.reports.data.RowEntity;
import ru.JSilver.asterisk.reports.dto.CallItemDtoV3;
import ru.JSilver.asterisk.reports.dto.CallQueueDto;
import ru.JSilver.asterisk.reports.dto.QueueItem;
import ru.JSilver.asterisk.reports.repos.RawRowRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CallServiceV3 {
    private final RawRowRepository repository;

    private final RowToCallCollectorV3 collectorV3;

    public List<CallItemDtoV3> getCallItems(LocalDateTime dateFrom, LocalDateTime dateTo, String queue) {
        return collectorV3.collectCalls(getRows(dateFrom, dateTo), queue);
    }

    private List<RowEntity> getRows(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return repository.findAllByCallDateTimeBetweenOrderByCallDateTime(dateFrom, dateTo);
    }

    public List<QueueItem> getQueueItems(LocalDateTime dateFrom, LocalDateTime dateTo, String queue) {
        List<QueueItem> queueItems = new ArrayList<>();
        List<CallItemDtoV3> calls = getCallItems(dateFrom, dateTo, queue);
        for (int i = 0; i < calls.size(); i++) {
            CallItemDtoV3 call = calls.get(i);
            List<QueueItem> callHistory = call.getHistoryList();
            for (int j = 0; j < callHistory.size() ; j++) {
                QueueItem item = callHistory.get(j);
                if (item.getQueue().equals(queue)) {
                    queueItems.add(item);
                }
            }
        }
        return queueItems;
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

        return detailedList;
    }
}
