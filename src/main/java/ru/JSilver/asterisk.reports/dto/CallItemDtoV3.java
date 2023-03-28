package ru.JSilver.asterisk.reports.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
public class CallItemDtoV3 {
    private String linkedId;
    private String number;
    private LocalDate date;
    private LocalTime time;
    private String status;
    private String audioPath;
    private List<QueueItem> historyList;

    public String getQueueNumbers () {
        String numbers = "";
        for (int i = 0; i < historyList.size(); i++) {
            numbers = numbers + historyList.get(i).getQueue() + ";";
        }
        return numbers;
    }
}
