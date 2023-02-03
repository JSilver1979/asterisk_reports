package ru.JSilver.asterisk.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
public class StatisticDto {
    Integer answeredCount;
    BigDecimal answeredCountAVG;
    Integer nonAnsweredByOperatorsCount;
    BigDecimal nonAnsweredByOperatorsCountAVG;
    Integer nonAnsweredByQueueCount;
    BigDecimal nonAnsweredByQueueCountAVG;
    Integer queuedDropCallsCount;
    Integer busyCallCount;
    Integer totalNonAnsweredCalls;
    BigDecimal totalNonAnsweredCallsAVG;
    Integer totalCalls;
    BigDecimal totalCallsAVG;
    List<Integer> answeredHours;
    List<BigDecimal> answeredHoursAVG;
    List<Integer> nonAOperHours;
    List<BigDecimal> nonAOperHoursAVG;
    List<Integer> nonAQHours;
    List<BigDecimal> nonAQHoursAVG;
    List<Integer> totalNonAHours;
    List<BigDecimal> totalNonAHoursAVG;
    List<Integer> totalHours;
    List<BigDecimal> totalHoursAVG;
    BigDecimal totalAnsByPercent;
    List<BigDecimal> hourAnswerByPercent;
    LocalTime waitTimeAVG;
    List<Integer> hourWaitTime;
    List<LocalTime> hourWaitTimeAVG;
    LocalTime talkTimeAVG;
    List<LocalTime> hourTalkTimeAVG;


    public StatisticDto() {
        answeredCount = 0;
        nonAnsweredByOperatorsCount = 0;
        nonAnsweredByQueueCount = 0;
        queuedDropCallsCount = 0;
        busyCallCount = 0;
        totalNonAnsweredCalls = 0;
        totalCalls = 0;
        answeredHours = new ArrayList<>(Collections.nCopies(16,0));
        answeredHoursAVG = new ArrayList<>();
        nonAOperHours = new ArrayList<>(Collections.nCopies(16,0));
        nonAOperHoursAVG = new ArrayList<>();
        totalNonAHours = new ArrayList<>(Collections.nCopies(16,0));
        totalNonAHoursAVG = new ArrayList<>();
        totalHours = new ArrayList<>(Collections.nCopies(16,0));
        totalHoursAVG = new ArrayList<>();
        nonAQHours = new ArrayList<>(Collections.nCopies(16,0));
        nonAQHoursAVG = new ArrayList<>();
        hourAnswerByPercent = new ArrayList<>();
        hourWaitTime = new ArrayList<>(Collections.nCopies(16,0));
        hourWaitTimeAVG = new ArrayList<>();
        hourTalkTimeAVG = new ArrayList<>();
    }

    public void countNonAnsweredCalls() {
        totalNonAnsweredCalls = nonAnsweredByOperatorsCount
                + nonAnsweredByQueueCount
                + queuedDropCallsCount
                + busyCallCount;
    }
}
