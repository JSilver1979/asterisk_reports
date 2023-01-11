package ru.JSilver.asterisk.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallHistoryDto {
    private LocalDateTime callTimeStart;
    private Integer callDuration;
    private String lastApp;
    private String status;
    private String callGroup;
}
