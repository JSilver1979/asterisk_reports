package ru.JSilver.asterisk.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DateSearchDto {
    private String dateFrom;
    private String dateTo;
    private String group;
    private int year;
    private int month;
}
