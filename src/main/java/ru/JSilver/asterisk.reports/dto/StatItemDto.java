package ru.JSilver.asterisk.reports.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatItemDto {
    private boolean answered;
    private boolean noAnswer;
    private boolean busy;
    private int noAnswerRows;

    public boolean answeredByOperator() {
        return answered && (noAnswer || busy);
    }

    public boolean droppedInQueue() {
        return answered && !noAnswer && !busy;
    }

    public boolean busyCall() {
        return !answered && busy;
    }

    public boolean notAnsweredByOperators() {
        return noAnswer && !answered && !busy && noAnswerRows > 1;
    }
    public boolean notAnsweredByQueue() {
        return noAnswer && !answered && !busy && noAnswerRows < 2;
    }
}
