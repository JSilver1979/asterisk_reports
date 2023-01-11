package ru.JSilver.asterisk.reports.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.JSilver.asterisk.reports.dto.CallItemDto;
import ru.JSilver.asterisk.reports.dto.DateSearchDto;
import ru.JSilver.asterisk.reports.services.CallsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class CallController {

    private final CallsService callsService;

    @PostMapping("/calls_search")
    public List<CallItemDto> searchCallByDate(@RequestBody DateSearchDto dateSearch) {
        LocalDateTime searchFromDate = LocalDateTime.parse(dateSearch.getDateFrom() + "T00:00:01", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime searchToDate = LocalDateTime.parse(dateSearch.getDateFrom() + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return callsService.getCallItems(searchFromDate, searchToDate, dateSearch.getGroup());
    }
}
