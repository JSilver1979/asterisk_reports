package ru.JSilver.asterisk_reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DateSearchDto {
    private String dateFrom;
    private String dateTo;
}
