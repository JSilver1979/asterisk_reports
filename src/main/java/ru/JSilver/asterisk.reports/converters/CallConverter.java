package ru.JSilver.asterisk.reports.converters;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.JSilver.asterisk.reports.data.CallEntity;
import ru.JSilver.asterisk.reports.dto.CallDto;

import java.time.LocalTime;

@Component
@Data
@NoArgsConstructor
public class CallConverter {

    public CallDto EntityToDto (CallEntity entity) {
        CallDto dto = new CallDto();

        dto.setLinkedId(entity.getLinkedId());
        dto.setCallDate(entity.getCallStartTime().toLocalDate());
        dto.setCallTime(entity.getCallStartTime().toLocalTime());
        dto.setOperatorAnswerDate(entity.getCallEndTime().toLocalDate());
        dto.setOperatorAnswerTime(entity.getCallEndTime().toLocalTime());
        dto.setCallNumber(entity.getIncomingCallNumber());
        dto.setFinalStatus(selectStatus(entity.getCallStatuses(), entity.getCallRowsNumber()));
        dto.setOperatorAnswerDuration(LocalTime.MIN.plusSeconds(entity.getCallTalkTime()));
        dto.setRecordingFile(entity.getRecordingFile());
        return dto;
    }

    private String selectStatus(String statuses, int rowNumber) {
        if (statuses.contains("ANSWERED") && rowNumber > 1) {
            return "ANSWERED";
        }
        else if (statuses.contains("ANSWERED") && rowNumber <= 1) {
            return "Сброшено в очереди";
        }
        else if (statuses.contains("NO ANSWER")) {
            return "Не отвечено оператором";
        }
        return "НЕИЗВЕСТНЫЙ СТАТУС";
    }
}
