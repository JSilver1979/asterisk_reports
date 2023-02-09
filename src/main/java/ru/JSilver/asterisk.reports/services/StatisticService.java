package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.converters.CallToStatsConverter;
import ru.JSilver.asterisk.reports.dto.CallItemDto;
import ru.JSilver.asterisk.reports.dto.StatisticDto;


import java.time.LocalDateTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticService {

    private final CallsService callsService;
    private final CallToStatsConverter converter;

    public StatisticDto getTrueStats (LocalDateTime dateFrom, LocalDateTime dateTo, String group) {
        int days = dateTo.getDayOfYear()-dateFrom.getDayOfYear() + 1;
        List<CallItemDto> callsList = callsService.getCallItems(dateFrom, dateTo, group);
        StatisticDto stats = converter.buildStats(callsList, days);

        return stats;
    }
}
