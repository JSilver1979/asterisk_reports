package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.dto.CallItemDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DownloadReportService {
    private final CallsService callsService;

    public String getSomeData(String dateFrom, String dateTo, String group) {
        LocalDateTime searchFromDate = LocalDateTime.parse(dateFrom + "T00:00:01", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        LocalDateTime searchToDate = LocalDateTime.parse(dateTo + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        List<CallItemDto> callItems = callsService.getCallItems(searchFromDate, searchToDate, group).stream()
                .sorted(Comparator.comparing(CallItemDto::getCallDate).thenComparing(CallItemDto::getCallTime))
                .collect(Collectors.toList());

        String headers = "Sequence;CallNumber;LinkedId;Group;callDate;callTime;callWaitTime;Status;AnswerDate;AnswerTime;Duration;File\n";
        StringBuilder sb = new StringBuilder();
        sb.append(headers);
        for (CallItemDto item: callItems) {
            sb.append(item.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

}
