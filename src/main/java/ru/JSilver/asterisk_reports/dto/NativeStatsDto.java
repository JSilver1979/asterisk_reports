package ru.JSilver.asterisk_reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NativeStatsDto {

    Integer answeredCount = 0;
    Integer nonAnsweredByOperatorsCount = 0;
    Integer nonAnsweredByQueueCount = 0;
    Integer queuedDropCallsCount = 0;
    Integer busyCallCount = 0;
    Integer totalNonAnsweredCalls = 0;
    Integer totalCalls = 0;

    public void countNonAnsweredCalls() {
        totalNonAnsweredCalls = nonAnsweredByOperatorsCount
                + nonAnsweredByQueueCount
                + queuedDropCallsCount
                + busyCallCount;
    }

}
