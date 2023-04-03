package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.converters.CallToStatsConverter;
import ru.JSilver.asterisk.reports.converters.StatsConverterV3;
import ru.JSilver.asterisk.reports.dto.CallItemDto;
import ru.JSilver.asterisk.reports.dto.CallItemDtoV3;
import ru.JSilver.asterisk.reports.dto.CallQueueDto;
import ru.JSilver.asterisk.reports.dto.StatisticDto;


import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final CallsService callsService;

    private final CallServiceV3 callServiceV3;
    private final CallToStatsConverter converter;

    private final StatsConverterV3 converterV3;

    public StatisticDto getTrueStats (LocalDateTime dateFrom, LocalDateTime dateTo, String group) {
        int days = dateTo.getDayOfYear()-dateFrom.getDayOfYear() + 1;
        List<CallItemDto> callsList = callsService.getCallItems(dateFrom, dateTo, group);
        StatisticDto stats = converter.buildStats(callsList, days);

        return stats;
    }

    public StatisticDto getDetailedStats (LocalDateTime dateFrom, LocalDateTime dateTo, String queue) {
        List<CallQueueDto> list = callServiceV3.getDetailedCalls(dateFrom, dateTo, queue);
        return converterV3.buildStats(list, getDays(dateFrom, dateTo));
    }

    private int getDays (LocalDateTime dateFrom, LocalDateTime dateTo) {
        return dateTo.getDayOfYear() - dateFrom.getDayOfYear() + 1;
    }
}
