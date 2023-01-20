package ru.JSilver.asterisk.reports.tools;

public enum CallStatus {
    ANSWERED ("ANSWERED"),
    NO_ANSWER_BY_QUEUE ("Сброшено в очереди"),
    NO_ANSWER_BY_OPERATOR ("Не отвечено оператором"),
    UNKNOWN_STATUS ("Неизвестный статус")
    ;

    private final String status;

    CallStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
