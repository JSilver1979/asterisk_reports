package ru.JSilver.asterisk.reports.dto;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;


public class CallItemDto {
    private Long sequence;
    private String callNumber;
    private String callId;
    private String operatorsGroup;
    private Map<Long, CallHistoryDto> callHistory;
    private LocalDate callDate;
    private LocalTime callTime;
    private String finalStatus;
    private LocalDate operatorAnswerDate;
    private LocalTime operatorAnswerTime;
    private LocalTime operatorAnswerDuration;
    private String recordingFile;

    public CallItemDto() {
    }

    public CallItemDto(Long sequence, String callNumber, String callId, String operatorsGroup, Map<Long, CallHistoryDto> callHistory, LocalDate callDate, LocalTime callTime, String finalStatus, LocalDate operatorAnswerDate, LocalTime operatorAnswerTime, LocalTime operatorAnswerDuration, String recordingFile) {
        this.sequence = sequence;
        this.callNumber = callNumber;
        this.callId = callId;
        this.operatorsGroup = operatorsGroup;
        this.callHistory = callHistory;
        this.callDate = callDate;
        this.callTime = callTime;
        this.finalStatus = finalStatus;
        this.operatorAnswerDate = operatorAnswerDate;
        this.operatorAnswerTime = operatorAnswerTime;
        this.operatorAnswerDuration = operatorAnswerDuration;
        this.recordingFile = recordingFile;
    }

    public Long getSequence() {
        return sequence;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getOperatorsGroup() {
        return operatorsGroup;
    }

    public void setOperatorsGroup(String operatorsGroup) {
        this.operatorsGroup = operatorsGroup;
    }

    public Map<Long, CallHistoryDto> getCallHistory() {
        return callHistory;
    }

    public void setCallHistory(Map<Long, CallHistoryDto> callHistory) {
        this.callHistory = callHistory;
    }

    public LocalDate getCallDate() {
        return callDate;
    }

    public void setCallDate(LocalDate callDate) {
        this.callDate = callDate;
    }

    public LocalTime getCallTime() {
        return callTime;
    }

    public void setCallTime(LocalTime callTime) {
        this.callTime = callTime;
    }

    public String getFinalStatus() {
        return finalStatus;
    }

    public void setFinalStatus(String finalStatus) {
        this.finalStatus = finalStatus;
    }

    public LocalDate getOperatorAnswerDate() {
        return operatorAnswerDate;
    }

    public void setOperatorAnswerDate(LocalDate operatorAnswerDate) {
        this.operatorAnswerDate = operatorAnswerDate;
    }

    public LocalTime getOperatorAnswerTime() {
        return operatorAnswerTime;
    }

    public void setOperatorAnswerTime(LocalTime operatorAnswerTime) {
        this.operatorAnswerTime = operatorAnswerTime;
    }

    public LocalTime getOperatorAnswerDuration() {
        return operatorAnswerDuration;
    }

    public void setOperatorAnswerDuration(LocalTime operatorAnswerDuration) {
        this.operatorAnswerDuration = operatorAnswerDuration;
    }

    public String getRecordingFile() {
        return recordingFile;
    }

    public void setRecordingFile(String recordingFile) {
        this.recordingFile = recordingFile;
    }
}
