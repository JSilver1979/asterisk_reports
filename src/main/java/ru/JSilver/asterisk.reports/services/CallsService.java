package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.converters.CallConverter;
import ru.JSilver.asterisk.reports.converters.CallInfoConverter;
import ru.JSilver.asterisk.reports.data.CallEntity;
import ru.JSilver.asterisk.reports.data.RawRow;
import ru.JSilver.asterisk.reports.dto.CallDto;
import ru.JSilver.asterisk.reports.dto.CallItemDto;
import ru.JSilver.asterisk.reports.repos.CallRepository;
import ru.JSilver.asterisk.reports.repos.RawRowRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallsService {
    private final RawRowRepository rowRepository;

    private final CallRepository callRepository;
    private final CallInfoConverter callInfoConverter;
    private final CallConverter converter;

    private List<RawRow> getCalls(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return rowRepository.findAllByCallDateTimeBetween(dateFrom, dateTo);
    }

    public List<CallItemDto> getCallItems(LocalDateTime dateFrom, LocalDateTime dateTo, String group) {
        return callInfoConverter.convertRawToDto(getCalls(dateFrom, dateTo), group);
    }

    public List<CallDto> getAllCalls(LocalDateTime from, LocalDateTime to, String group) {
        return callRepository.getCalls(from, to, "%" + group + "%").stream()
                .map(converter::EntityToDto)
                .collect(Collectors.toList());
    }
}
