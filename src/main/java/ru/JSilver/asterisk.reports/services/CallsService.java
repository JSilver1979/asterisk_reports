package ru.JSilver.asterisk.reports.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.JSilver.asterisk.reports.data.RawRow;
import ru.JSilver.asterisk.reports.dto.CallHistoryDto;
import ru.JSilver.asterisk.reports.dto.CallItemDto;
import ru.JSilver.asterisk.reports.repos.RawRowRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CallsService {
    private final RawRowRepository rowRepository;

    public List<RawRow> getCalls(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return rowRepository.findAllByCallDateTimeBetween(dateFrom, dateTo);
    }

    public List<CallItemDto> getCallItems(LocalDateTime dateFrom, LocalDateTime dateTo, String group) {
        List<RawRow> rawItems = getCalls(dateFrom, dateTo);
        Map<String, CallItemDto> callMap = new HashMap<>();

        for (int i = 0; i < rawItems.size(); i++) {

            CallHistoryDto callHistoryDto = new CallHistoryDto(
                    rawItems.get(i).getCallDateTime(),
                    rawItems.get(i).getDuration(),
                    rawItems.get(i).getLastApp(),
                    rawItems.get(i).getDisposition()
            );
            if (!callMap.isEmpty() && callMap.containsKey(rawItems.get(i).getLinkedId())) {
                CallItemDto callItem = callMap.get(rawItems.get(i).getLinkedId());
                callItem.getCallHistory().put(rawItems.get(i).getSequence(), callHistoryDto);

                    for (Map.Entry<Long, CallHistoryDto> entry: callItem.getCallHistory().entrySet()) {
                        if(entry.getValue().getLastApp().equals("Hangup")) {
                            callItem.setFinalStatus(CallStatus.HANGUP.name());
                            break;
                        }
                        if (entry.getValue().getStatus().equals(CallStatus.ANSWERED.name())) {
                            callItem.setFinalStatus(CallStatus.ANSWERED.name());
                            callItem.setOperatorAnswerDate(callHistoryDto.getCallTimeStart().toLocalDate());
                            callItem.setOperatorAnswerTime(callHistoryDto.getCallTimeStart().toLocalTime());
                            callItem.setOperatorAnswerDuration(LocalTime.MIN.plusSeconds(callHistoryDto.getCallDuration()));
                        }
                    }

                if (callItem.getCallTime().isAfter(callHistoryDto.getCallTimeStart().toLocalTime())) {
                    callItem.setCallTime(callHistoryDto.getCallTimeStart().toLocalTime());
                }

            } else {
                if (rawItems.get(i).getSrc().length() > 4) {
                    callMap.put(rawItems.get(i).getLinkedId(), new CallItemDto(
                            rawItems.get(i).getSequence(),
                            rawItems.get(i).getSrc(),
                            rawItems.get(i).getLinkedId(),
                            rawItems.get(i).getDst(),
                            createHistoryDtoMap(rawItems.get(i).getSequence(), callHistoryDto),
                            rawItems.get(i).getCallDateTime().toLocalDate(),
                            rawItems.get(i).getCallDateTime().toLocalTime(),
                            CallStatus.NO_ANSWER.name(),
                            null,
                            null,
                            null,
                            rawItems.get(i).getRecordingFile()
                    ));
                }
            }
        }
        List<CallItemDto>  dtoList = callMap.values()
                .stream()
                .filter(callItemDto -> callItemDto.getOperatorsGroup().equals(group))
                .sorted(Comparator.comparing(CallItemDto::getSequence))
                .collect(Collectors.toList());
        return dtoList;
    }

    private Map<Long, CallHistoryDto> createHistoryDtoMap(Long key, CallHistoryDto historyDto) {
        Map<Long, CallHistoryDto> historyMap = new HashMap<>();
        historyMap.put(key, historyDto);
        return historyMap;
    }
}
