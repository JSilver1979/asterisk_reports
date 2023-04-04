package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.dto.CallItemDto;
import ru.JSilver.asterisk.reports.dto.CallQueueDto;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DownloadReportService {
    private final CallsService callsService;

    private final CallServiceV3 callServiceV3;

    public String getSomeData(int year, int month, String group) {
        List<CallItemDto> callItems = callsService.getCallItems(getStartDate(year, month), getEndDate(year, month), group)
                .stream()
                .sorted(Comparator.comparing(CallItemDto::getCallDate).thenComparing(CallItemDto::getCallTime))
                .collect(Collectors.toList());

        String headers = "Sequence;CallNumber;LinkedId;Group;callDate;callTime;callWaitTime;Status;AnswerDate;AnswerTime;Duration;File\n";
        StringBuilder sb = new StringBuilder();
        sb.append(headers);
        for (CallItemDto item: callItems) {
            sb.append(item.toString()).append("\n");
        }
        return sb.toString();
    }

    public String getDetailedStrings(int year, int month, String queue) {
        String header = "Call ID;Call Number;Call Date;Call Time;Queue Time;Wait Time;Agent Number;Answer Time;Duration;Call Status;Queue Status;Redirected\n";
        String delimiter = ";";
        List<CallQueueDto> list = callServiceV3.getDetailedCalls(getStartDate(year, month), getEndDate(year,month), queue)
                .stream()
                .sorted(Comparator.comparing(CallQueueDto::getDate).thenComparing(CallQueueDto::getCallTime))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder();
        sb.append(header);
        for (CallQueueDto item: list) {
            sb
                    .append(item.getCallId()).append(delimiter)
                    .append(item.getNumber()).append(delimiter)
                    .append(nullToString(item.getDate())).append(delimiter)
                    .append(nullToString(item.getCallTime())).append(delimiter)
                    .append(nullToString(item.getQueueTime())).append(delimiter)
                    .append(nullToString(item.getWaitTime())).append(delimiter)
                    .append(nullToString(item.getAgentNumber())).append(delimiter)
                    .append(nullToString(item.getAnswerTime())).append(delimiter)
                    .append(nullToString(item.getAnswerDuration())).append(delimiter)
                    .append(item.getCallStatus()).append(delimiter)
                    .append(item.getQueueStatus()).append(delimiter)
                    .append((item.isRedirected() ? "REDIRECTED" : "")).append("\n");
        }

        return sb.toString();
    }

    private String nullToString(Object o) {
        return o == null ? "" : o.toString();
    }
    private LocalDateTime getStartDate(int year, int month) {
        return LocalDateTime.of(year, month, 1, 0,0,1);
    }

    private LocalDateTime getEndDate(int year, int month) {
        return LocalDateTime.of(year, month, YearMonth.of(year, month).atEndOfMonth().getDayOfMonth(), 23,59,59);
    }

}
