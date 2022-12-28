package ru.JSilver.asterisk.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDto {
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
