package ru.JSilver.asterisk.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallDto {
    private String linkedId;

    private LocalDate callDate;
    private LocalTime callTime;
    private LocalDate operatorAnswerDate;
    private LocalTime operatorAnswerTime;
    private String callNumber;
    private String callDestination;
    private String finalStatus;
    private LocalTime operatorAnswerDuration;
    private String recordingFile;
    private LocalTime callWaitingTime;
}
