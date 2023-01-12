package ru.JSilver.asterisk.reports.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.JSilver.asterisk.reports.data.NewStatisticEntity;
import ru.JSilver.asterisk.reports.dto.DateSearchDto;
import ru.JSilver.asterisk.reports.dto.StatisticDto;
import ru.JSilver.asterisk.reports.services.StatisticService;

import java.util.List;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @PostMapping()
    public StatisticDto getStatistics(@RequestBody DateSearchDto searchDto) {
        return statisticService.collectStatistic(searchDto.getDateFrom(), searchDto.getDateTo(), searchDto.getGroup());
    }

    @PostMapping("/v2")
    public StatisticDto getNewStats(@RequestBody DateSearchDto searchDto) {
        return statisticService.newStatisticEntityList(searchDto.getDateFrom(), searchDto.getDateTo(), searchDto.getGroup());
    }
}
