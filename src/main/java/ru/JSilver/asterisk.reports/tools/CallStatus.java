package ru.JSilver.asterisk.reports.tools;

public enum CallStatus {
    ANSWERED ("ANSWERED"),
    NO_ANSWER_BY_QUEUE ("NO ANSWERED BY QUEUE"),
    NO_ANSWER_BY_OPERATOR ("NO ANSWERED BY OPERATOR"),
    BUSY ("BUSY"),
    UNKNOWN_STATUS ("UNKNOWN STATUS")
    ;

    private final String status;

    CallStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
