package ru.JSilver.asterisk.reports.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StatisticDto {
    private Integer answeredCount;
    private BigDecimal answeredCountAVG;
    private Integer nonAnsweredByOperatorsCount;
    private BigDecimal nonAnsweredByOperatorsCountAVG;
    private Integer nonAnsweredByQueueCount;
    private BigDecimal nonAnsweredByQueueCountAVG;
    private Integer busyQueueCount;
    private BigDecimal busyQueueCountAVG;
    private Integer totalNonAnsweredCalls;
    private BigDecimal totalNonAnsweredCallsAVG;
    private Integer totalCalls;
    private BigDecimal totalCallsAVG;
    private BigDecimal totalAnsByPercent;
    private LocalTime waitTimeAVG;
    private LocalTime talkTimeAVG;
    private List<HoursStatDto> hourStats;

    public StatisticDto() {
        answeredCount = 0;
        nonAnsweredByOperatorsCount = 0;
        nonAnsweredByQueueCount = 0;
        busyQueueCount = 0;
        totalNonAnsweredCalls = 0;
        totalCalls = 0;
        hourStats = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hourStats.add(new HoursStatDto());
        }
    }
}
