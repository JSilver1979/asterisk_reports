package ru.JSilver.asterisk.reports.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.JSilver.asterisk.reports.dto.DateSearchDto;
import ru.JSilver.asterisk.reports.dto.StatisticDto;
import ru.JSilver.asterisk.reports.services.StatisticService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping("/v2")
    public StatisticDto getNewStats(@RequestBody DateSearchDto searchDto) {
        LocalDateTime searchFromDate = LocalDateTime.parse(searchDto.getDateFrom() + "T00:00:01", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime searchToDate = LocalDateTime.parse(searchDto.getDateTo() + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return statisticService.getTrueStats(searchFromDate, searchToDate, searchDto.getGroup());
    }
}
