package ru.JSilver.asterisk.reports.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
public class QueueItem {
    private String queue;
    private LocalTime timeStart;
    private LocalTime timeWait;
    private String agent;
    private LocalTime agentAnswerTime;
    private LocalTime agentAnswerDuration;
    private String queueStatus;
}
