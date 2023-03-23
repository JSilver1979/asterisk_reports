package ru.JSilver.asterisk.reports.converters;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.JSilver.asterisk.reports.data.RowEntity;
import ru.JSilver.asterisk.reports.dto.CallItemDto;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RowToCallCollector {

    private Map<String, CallItemDto> callMap;
    private final RowToCallConverter rowConverter;

    public List<CallItemDto> collectRowsToCall(List<RowEntity> rowList, String group) {
        if (callMap != null) callMap.clear();

        for (int i = 0; i < rowList.size(); i++) {
            if (rowList.get(i).getSrc().length() < 5) {
                continue;
            }
            if (!callMap.containsKey(rowList.get(i).getLinkedId())) {
                callMap.put(rowList.get(i).getLinkedId(), rowConverter.createCallItem(rowList.get(i)));
                continue;
            }
            rowConverter.updateCallItem(callMap.get(rowList.get(i).getLinkedId()), rowList.get(i));
        }

        return callMap.values().stream()
                .filter(callItemDto -> callItemDto.getOperatorsGroup().equals(group))
                .sorted(Comparator.comparing(CallItemDto::getCallDate))
                .sorted(Comparator.comparing(CallItemDto::getCallTime))
                .collect(Collectors.toList());
    }
}
