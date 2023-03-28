package ru.JSilver.asterisk.reports.converters;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.JSilver.asterisk.reports.data.RowEntity;
import ru.JSilver.asterisk.reports.dto.CallItemDtoV3;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class RowToCallCollectorV3 {
    private Map<String, CallItemDtoV3> callMap;

    private final CallConverterV3 converter;

    public List<CallItemDtoV3> collectCalls(List<RowEntity> rows, String queue) {
        if (callMap != null) callMap.clear();

        for (int i = 0; i < rows.size(); i++) {
            var row = rows.get(i);

            if (row.getSrc().length() < 5) continue;

            if (!callMap.containsKey(row.getLinkedId())) {
                callMap.put(row.getLinkedId(), converter.createCall(row));
                continue;
            }
            converter.updateCall(callMap.get(row.getLinkedId()), row);
        }

        return callMap.values().stream()
                .filter(callItemDtoV3 -> callItemDtoV3.getQueueNumbers().contains(queue))
                .sorted(Comparator.comparing(CallItemDtoV3::getDate))
                .sorted(Comparator.comparing(CallItemDtoV3::getTime))
                .collect(Collectors.toList());
    }
}
