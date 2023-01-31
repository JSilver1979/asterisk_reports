package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.dto.CallItemDto;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DownloadReportService {
    private final CallsService callsService;

    public String getSomeData(int year, int month, String group) {
        LocalDateTime searchFromDate = LocalDateTime.of(year,month,1,0,0,1);
        LocalDateTime searchToDate = LocalDateTime.of(year,month, YearMonth.of(year,month).atEndOfMonth().getDayOfMonth(),23,59,59);
        List<CallItemDto> callItems = callsService.getCallItems(searchFromDate, searchToDate, group)
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

}
