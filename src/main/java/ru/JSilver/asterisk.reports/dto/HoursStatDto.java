package ru.JSilver.asterisk.reports.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class HoursStatDto {
    private String hour;
    private Integer answered;
    private BigDecimal answeredAVG;
    private BigDecimal answeredPercent;
    private Integer naOperator;
    private BigDecimal naOperatorAVG;
    private Integer naQueue;
    private BigDecimal naQueueAVG;
    private Integer naTotal;
    private BigDecimal naTotalAVG;
    private Integer total;
    private BigDecimal totalAVG;
    private LocalTime waitTimeAVG;
    private LocalTime talkTimeAVG;

    public HoursStatDto() {
        this.answered = 0;
        this.naOperator = 0;
        this.naQueue = 0;
        this.naTotal = 0;
        this.total = 0;
    }
}
