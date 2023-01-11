package ru.JSilver.asterisk.reports.converters;

import org.springframework.stereotype.Component;
import ru.JSilver.asterisk.reports.data.RawRow;
import ru.JSilver.asterisk.reports.dto.CallHistoryDto;
import ru.JSilver.asterisk.reports.dto.CallItemDto;
import ru.JSilver.asterisk.reports.services.CallStatus;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CallInfoConverter {

    private Map<String,CallItemDto> callMap;

    public CallInfoConverter() {
        callMap = new HashMap<>();
    }

    public List<CallItemDto> convertRawToDto(List<RawRow> rowList, String group) {
        callMap.clear();

        for (int i = 0; i < rowList.size(); i++) {
            if (rowList.get(i).getSrc().length() < 5) {
                continue;
            }
            CallHistoryDto callHistoryDto = new CallHistoryDto(
                    rowList.get(i).getCallDateTime(),
                    rowList.get(i).getDuration(),
                    rowList.get(i).getLastApp(),
                    rowList.get(i).getDisposition()
            );
            if (!callMap.containsKey(rowList.get(i).getLinkedId())) {
                callMap.put(rowList.get(i).getLinkedId(), new CallItemDto(
                   rowList.get(i).getSequence(),
                   rowList.get(i).getSrc(),
                   rowList.get(i).getLinkedId(),
                   rowList.get(i).getDst(),
                   createCallHistoryMap(rowList.get(i).getSequence(), callHistoryDto),
                   rowList.get(i).getCallDateTime().toLocalDate(),
                   rowList.get(i).getCallDateTime().toLocalTime(),
                   CallStatus.NO_ANSWER.name(),
                   null,
                   null,
                   null,
                   rowList.get(i).getRecordingFile()
                ));
                continue;
            }

            CallItemDto callItem = callMap.get(rowList.get(i).getLinkedId());
            callItem.getCallHistory().put(rowList.get(i).getSequence(), callHistoryDto);

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
        }

        return callMap.values()
                .stream()
                .filter(callItemDto -> callItemDto.getOperatorsGroup().equals(group))
                .sorted(Comparator.comparing(CallItemDto::getSequence))
                .collect(Collectors.toList());
    }

    private Map<Long, CallHistoryDto> createCallHistoryMap(Long key, CallHistoryDto callHistoryDto) {
        Map<Long, CallHistoryDto> historyMap = new HashMap<>();
        historyMap.put(key, callHistoryDto);
        return historyMap;
    }
}
