package ru.JSilver.asterisk.reports.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import ru.JSilver.asterisk.reports.dto.CallItemDto;
import ru.JSilver.asterisk.reports.dto.CallItemDtoV3;
import ru.JSilver.asterisk.reports.dto.CallQueueDto;
import ru.JSilver.asterisk.reports.dto.DateSearchDto;
import ru.JSilver.asterisk.reports.services.CallServiceV3;
import ru.JSilver.asterisk.reports.services.CallsService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class CallController {

    private final CallsService callsService;

    private final CallServiceV3 callServiceV3;

    @PostMapping("/calls_search")
    public List<CallItemDto> searchCallByDate(@RequestBody DateSearchDto dateSearch) {
        LocalDateTime searchFromDate = LocalDateTime.parse(dateSearch.getDateFrom() + "T00:00:01", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime searchToDate = LocalDateTime.parse(dateSearch.getDateFrom() + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return callsService.getCallItems(searchFromDate, searchToDate, dateSearch.getGroup());
    }

    @PostMapping("/test")
    public List<CallItemDtoV3> getCalls(@RequestBody DateSearchDto dateSearch) {
        LocalDateTime searchFromDate = LocalDateTime.parse(dateSearch.getDateFrom() + "T00:00:01", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime searchToDate = LocalDateTime.parse(dateSearch.getDateFrom() + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return callServiceV3.getCallItems(searchFromDate, searchToDate, dateSearch.getGroup());
    }

    @PostMapping("/detailed")
    public List<CallQueueDto> getDetailedCalls(@RequestBody DateSearchDto dateSearch) {
        LocalDateTime fromDate = LocalDateTime.parse(dateSearch.getDateFrom() + "T00:00:01", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime toDate = LocalDateTime.parse(dateSearch.getDateFrom() + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return callServiceV3.getDetailedCalls(fromDate, toDate, dateSearch.getGroup());
    }
}
