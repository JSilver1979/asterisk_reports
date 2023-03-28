package ru.JSilver.asterisk.reports.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
public class CallQueueDto {
    private String callId;
    private String number;
    private LocalDate date;
    private LocalTime callTime;
    private LocalTime queueTime;
    private LocalTime waitTime;
    private String agentNumber;
    private LocalTime answerTime;
    private LocalTime answerDuration;
    private String callStatus;
    private String queueStatus;
    private String audioPath;
    private boolean isRedirected;
}
