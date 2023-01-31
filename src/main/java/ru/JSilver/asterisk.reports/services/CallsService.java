package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.converters.RowToCallCollector;
import ru.JSilver.asterisk.reports.data.RowEntity;
import ru.JSilver.asterisk.reports.dto.CallItemDto;
import ru.JSilver.asterisk.reports.repos.RawRowRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallsService {
    private final RawRowRepository rowRepository;

    private final RowToCallCollector callCollector;

    private List<RowEntity> getCalls(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return rowRepository.findAllByCallDateTimeBetweenOrderByCallDateTime(dateFrom, dateTo);
    }

    public List<CallItemDto> getCallItems(LocalDateTime dateFrom, LocalDateTime dateTo, String group) {
        return callCollector.collectRowsToCall(getCalls(dateFrom, dateTo), group);
    }
}
