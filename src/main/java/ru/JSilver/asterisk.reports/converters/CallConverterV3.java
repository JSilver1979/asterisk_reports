package ru.JSilver.asterisk.reports.converters;

import org.springframework.stereotype.Component;
import ru.JSilver.asterisk.reports.data.QueueMap;
import ru.JSilver.asterisk.reports.data.RowEntity;
import ru.JSilver.asterisk.reports.dto.CallItemDtoV3;
import ru.JSilver.asterisk.reports.dto.QueueItem;
import ru.JSilver.asterisk.reports.tools.CallStatus;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class CallConverterV3 {
    private final QueueMap queueMap;

    public CallConverterV3() {
        queueMap = new QueueMap();
    }

    public CallItemDtoV3 createCall(RowEntity row) {
        CallItemDtoV3 call = new CallItemDtoV3();

        call.setLinkedId(row.getLinkedId());
        call.setNumber(row.getSrc());
        call.setDate(row.getCallDateTime().toLocalDate());
        call.setTime(row.getCallDateTime().toLocalTime());
        call.setStatus("NO ANSWER");
        call.setAudioPath(row.getRecordingFile());

        if (isAnswered(row)) {
            call.setStatus("ANSWERED");
            call.setAudioPath(row.getRecordingFile());
        }

        List<QueueItem> historyList = new ArrayList<>();
        historyList.add(addHistory(row));
        call.setHistoryList(historyList);

        return call;
    }

    public void updateCall(CallItemDtoV3 call, RowEntity row) {
        if (call.getTime().isAfter(row.getCallDateTime().toLocalTime())) {
            call.setTime(row.getCallDateTime().toLocalTime());
        }

        if (isAnswered(row)) {
            call.setStatus(CallStatus.ANSWERED.getStatus());
        }

        if (call.getAudioPath() == null || call.getAudioPath().isBlank()) {
            call.setAudioPath(row.getRecordingFile());
        }

        updateHistory(call, row);

    }

    private QueueItem addHistory(RowEntity row) {
        QueueItem item = new QueueItem();

        item.setTimeStart(row.getCallDateTime().toLocalTime());
        item.setQueue(queueMap.getQueue(null, row.getDst()));
        item.setTimeWait(LocalTime.MIN.plusSeconds(row.getDuration()));
        item.setQueueStatus(addNewStatus(row));

        if (isAnswered(row)) {
            item.setAgent(row.getDst());
            updateAnswerTime(item, row);
            item.setTimeWait(LocalTime.MIN.plusSeconds(
                    item.getTimeStart().until(item.getAgentAnswerTime(), ChronoUnit.SECONDS)
            ));
            item.setAgentAnswerDuration(LocalTime.MIN.plusSeconds(row.getDuration()));

        }

        return item;
    }

    private void updateHistory(CallItemDtoV3 call, RowEntity row) {
        List<QueueItem> historyList = call.getHistoryList();
        boolean isItemFound = false;

        for (QueueItem item : historyList) {
            if (item.getQueue().equals(queueMap.getQueue(item.getQueue(), row.getDst()))) {
                isItemFound = true;

                updateTimeStart(item, row);
                updateAnswerTime(item, row);
                updateStatus(item, row);

                item.setTimeWait(LocalTime.MIN.plusSeconds(
                        item.getTimeStart().until(item.getAgentAnswerTime(), ChronoUnit.SECONDS)
                ));
            }
        }

        if (!isItemFound && !row.getDst().equals("hangup")) historyList.add(addHistory(row));
    }

    private void updateTimeStart(QueueItem item, RowEntity row) {
        if (item.getTimeStart().isAfter(row.getCallDateTime().toLocalTime())) {
            item.setTimeStart(row.getCallDateTime().toLocalTime());
        }
    }

    private void updateAnswerTime(QueueItem item, RowEntity row) {
        if (item.getAgentAnswerTime() == null ||
                item.getAgentAnswerTime().isBefore(row.getCallDateTime().toLocalTime())) {
            item.setAgentAnswerTime(row.getCallDateTime().toLocalTime());
        }
    }

    private void updateStatus(QueueItem item, RowEntity row) {
        if (!isItemBusyOrAnswered(item)) {
            if (row.getDisposition().equals("NO ANSWER") || isHangUp(row)) {
                item.setQueueStatus(CallStatus.NO_ANSWER_BY_OPERATOR.getStatus());
            }

            if (isBusy(row)) {
                item.setQueueStatus(CallStatus.BUSY.getStatus());
            }

            if (isAnswered(row)) {
                item.setQueueStatus(CallStatus.ANSWERED.getStatus());
                item.setAgentAnswerDuration(LocalTime.MIN.plusSeconds(row.getDuration()));
                item.setAgent(row.getDst());
            }
        }
    }

    private String addNewStatus (RowEntity row) {
        if (isAnswered(row)) {
            return CallStatus.ANSWERED.getStatus();
        }
        return CallStatus.NO_ANSWER_BY_QUEUE.getStatus();
    }

    private boolean isHangUp (RowEntity row) {
        return row.getDisposition().equals("ANSWERED") && row.getLastApp().equals("Hangup");
    }

    private boolean isAnswered(RowEntity row) {
        return row.getDisposition().equals("ANSWERED") && row.getLastApp().equals("Dial");
    }

    private boolean isBusy(RowEntity row) {
        return row.getDisposition().equals("BUSY");
    }

    private boolean isItemBusyOrAnswered(QueueItem item) {
        return item.getQueueStatus().equals(CallStatus.ANSWERED.getStatus())
                || item.getQueueStatus().equals(CallStatus.BUSY.getStatus());
    }

}
