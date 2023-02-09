package ru.JSilver.asterisk.reports.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class StatisticDto {
    Integer answeredCount;
    BigDecimal answeredCountAVG;
    Integer nonAnsweredByOperatorsCount;
    BigDecimal nonAnsweredByOperatorsCountAVG;
    Integer nonAnsweredByQueueCount;
    BigDecimal nonAnsweredByQueueCountAVG;
    Integer totalNonAnsweredCalls;
    BigDecimal totalNonAnsweredCallsAVG;
    Integer totalCalls;
    BigDecimal totalCallsAVG;
    BigDecimal totalAnsByPercent;
    LocalTime waitTimeAVG;
    LocalTime talkTimeAVG;
    List<HoursStatDto> hourStats;

    public StatisticDto() {
        answeredCount = 0;
        nonAnsweredByOperatorsCount = 0;
        nonAnsweredByQueueCount = 0;
        totalNonAnsweredCalls = 0;
        totalCalls = 0;
        hourStats = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hourStats.add(new HoursStatDto());
        }
    }
}
