package ru.JSilver.asterisk.reports.converters;

import org.springframework.stereotype.Component;
import ru.JSilver.asterisk.reports.data.RowEntity;
import ru.JSilver.asterisk.reports.dto.CallItemDto;
import ru.JSilver.asterisk.reports.tools.CallStatus;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Component
public class RowToCallConverter {

    public CallItemDto createCallItem(RowEntity row) {
        CallItemDto callItemDto = new CallItemDto();

        callItemDto.setSequence(row.getSequence());
        callItemDto.setCallNumber(row.getSrc());
        callItemDto.setLinkedId(row.getLinkedId());
        callItemDto.setOperatorsGroup(row.getDst());
        callItemDto.setCallDate(row.getCallDateTime().toLocalDate());
        callItemDto.setCallTime(row.getCallDateTime().toLocalTime());
        callItemDto.setFinalStatus(CallStatus.NO_ANSWER_BY_QUEUE.getStatus());
        callItemDto.setRecordingFile(row.getRecordingFile());
        callItemDto.setCallWaitingTime(LocalTime.MIN.plusSeconds(row.getDuration()));

        return callItemDto;
    }

    public CallItemDto updateCallItem(CallItemDto callItemDto, RowEntity row) {
        if (callItemDto.getCallTime().isAfter(row.getCallDateTime().toLocalTime())) {
            callItemDto.setCallTime(row.getCallDateTime().toLocalTime());
        }
        if (callItemDto.getOperatorAnswerTime() == null) {
            callItemDto.setOperatorAnswerDate(row.getCallDateTime().toLocalDate());
            callItemDto.setOperatorAnswerTime(row.getCallDateTime().toLocalTime());
        }
        if (callItemDto.getOperatorAnswerTime().isBefore(row.getCallDateTime().toLocalTime())) {
            callItemDto.setOperatorAnswerTime(row.getCallDateTime().toLocalTime());
        }

        callItemDto.setCallWaitingTime(LocalTime.MIN.plusSeconds(
                callItemDto.getCallTime().until(
                        callItemDto.getOperatorAnswerTime(),
                        ChronoUnit.SECONDS
                )));

        if (isCallGroup(row.getDst())) {
            callItemDto.setOperatorsGroup(row.getDst());
        }

        if (callItemDto.getCallTime().isAfter(row.getCallDateTime().toLocalTime())) {
            callItemDto.setCallTime(row.getCallDateTime().toLocalTime());
        }

        if (row.getDisposition().equals("NO ANSWER")) {
            callItemDto.setFinalStatus(CallStatus.NO_ANSWER_BY_OPERATOR.getStatus());

        } else if (row.getDisposition().equals("ANSWERED")) {
            callItemDto.setFinalStatus(CallStatus.ANSWERED.getStatus());
            if(row.getLastApp().equals("Dial")) {
                if(callItemDto.getOperatorAnswerDuration() == null) {
                    callItemDto.setOperatorAnswerDuration(
                            LocalTime.MIN.plusSeconds(row.getDuration()));
                } else {
                    callItemDto.setOperatorAnswerDuration(
                            callItemDto.getOperatorAnswerDuration().plusSeconds(row.getDuration()));
                }
            }
        }

        return callItemDto;
    }

    private boolean isCallGroup(String callGroup) {
        //TODO: remove hardcode
        String[] groupArray = {
                "1111",
                "1112",
                "1113",
                "1114",
                "1115",
                "1116",
                "1117",
                "1120",
                "1122"
        };

        for (String item: groupArray) {
            if (item.equals(callGroup)) {
                return true;
            }
        }
        return false;
    }
}
