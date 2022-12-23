package ru.JSilver.asterisk_reports.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.JSilver.asterisk_reports.data.RawRow;
import ru.JSilver.asterisk_reports.dto.CallItemDto;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RowToCallConverter {
    private List<CallItemDto> callsList;

    @PostConstruct
    public void init() {
        callsList = new ArrayList<>();
    }
    public CallItemDto entityToCallItemDto(CallItemDto callItemDto, RawRow row) {
        if (!callsList.isEmpty() || hasItem(callItemDto)) {

        }


        return callItemDto;
    }

    private boolean hasItem (CallItemDto callItemDto) {
        for (int i = 0; i < callsList.size(); i++) {
            if (callsList.get(i).getCallId().equals(callItemDto.getCallId())) {
                return true;
            }
        }
        return false;
    }
}
