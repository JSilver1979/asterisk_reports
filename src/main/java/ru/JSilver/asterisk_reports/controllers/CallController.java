package ru.JSilver.asterisk_reports.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.JSilver.asterisk_reports.data.CallEntity;
import ru.JSilver.asterisk_reports.data.RawRow;
import ru.JSilver.asterisk_reports.dto.*;
import ru.JSilver.asterisk_reports.repos.CallRepository;
import ru.JSilver.asterisk_reports.services.CallsService;
import ru.JSilver.asterisk_reports.services.NativeCallService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@Slf4j
public class CallController {
    private final NativeCallService nativeCallService;

    private final CallRepository callRepository;

    private final CallsService callsService;


    @PostMapping("/calls_search")
    public List<CallItemDto> searchCallByDate(@RequestBody DateSearchDto dateSearch)

    {
        LocalDateTime searchFromDate = LocalDateTime.parse(dateSearch.getDateFrom() + "T00:00:01", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime searchToDate = LocalDateTime.parse(dateSearch.getDateTo() + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return callsService.getCallItems(searchFromDate, searchToDate, dateSearch.getGroup());
    }
    @GetMapping("/call")
    public List<RawRow> getDtoCalls() {
        return callsService.getCalls(
                LocalDateTime.parse("2022-09-01T00:00:01",DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                LocalDateTime.parse("2022-09-14T23:59:01",DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @GetMapping("/test/{page}")
    public List<CallEntity> getCalls(@PathVariable Integer page) {
       return callRepository.findCallsByDate(PageRequest.of(page, 10));

    }
}
