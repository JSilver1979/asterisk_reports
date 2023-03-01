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

        if(row.getLastApp().equals("Dial") && row.getDisposition().equals("ANSWERED")) {
            callItemDto.setOperatorAnswerDuration(LocalTime.MIN.plusSeconds(row.getDuration()));
            callItemDto.setOperatorAnswerTime(row.getCallDateTime().toLocalTime());
            callItemDto.setOperatorAnswerDate(row.getCallDateTime().toLocalDate());
        }

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
        if (callItemDto.getRecordingFile() == null) {
            callItemDto.setRecordingFile(row.getRecordingFile());
        }

        callItemDto.setCallWaitingTime(LocalTime.MIN.plusSeconds(
                callItemDto.getCallTime().until(
                        callItemDto.getOperatorAnswerTime(),
                        ChronoUnit.SECONDS
                )));

        //TODO: разобраться с множественным вложением условий
        if (callItemDto.getFinalStatus().equals(CallStatus.ANSWERED.getStatus())) {
            if (row.getDisposition().equals("ANSWERED") && row.getLastApp().equals("Dial")) {
                if(callItemDto.getOperatorAnswerDuration() == null) {
                    callItemDto.setOperatorAnswerDuration(
                            LocalTime.MIN.plusSeconds(row.getDuration()));
                } else {
                    callItemDto.setOperatorAnswerDuration(
                            callItemDto.getOperatorAnswerDuration().plusSeconds(row.getDuration()));
                }
            }
        } else {
            if (row.getDisposition().equals("NO ANSWER") || (row.getDisposition().equals("ANSWERED") && row.getLastApp().equals("Hangup"))) {
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
        }

        return callItemDto;
    }
}
