package ru.JSilver.asterisk_reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NativeCallDto {
    private String linkedId;
    private String disposition;
    private Integer resultRows;
}
