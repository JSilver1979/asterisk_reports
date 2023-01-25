package ru.JSilver.asterisk.reports.dto;


import java.time.LocalDate;
import java.time.LocalTime;


public class CallItemDto {
    private Long sequence;
    private String callNumber;
    private String linkedId;
    private String operatorsGroup;
    private LocalDate callDate;
    private LocalTime callTime;
    private LocalTime callWaitingTime;
    private String finalStatus;
    private LocalDate operatorAnswerDate;
    private LocalTime operatorAnswerTime;
    private LocalTime operatorAnswerDuration;
    private String recordingFile;

    public CallItemDto() {
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

    public String getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(String linkedId) {
        this.linkedId = linkedId;
    }

    public String getOperatorsGroup() {
        return operatorsGroup;
    }

    public void setOperatorsGroup(String operatorsGroup) {
        this.operatorsGroup = operatorsGroup;
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

    public LocalTime getCallWaitingTime() {
        return callWaitingTime;
    }

    public void setCallWaitingTime(LocalTime callWaitingTime) {
        this.callWaitingTime = callWaitingTime;
    }

    @Override
    public String toString() {
        String delimiter = ";";

        return sequence.toString() + delimiter +
                callNumber + delimiter +
                linkedId + delimiter +
                operatorsGroup + delimiter +
                (callDate != null ? callDate.toString() : "") + delimiter +
                (callTime != null ? callTime.toString() : "") + delimiter +
                (callWaitingTime != null ? callWaitingTime.toString() : "") + delimiter +
                finalStatus + delimiter +
                (operatorAnswerDate != null ? operatorAnswerDate.toString() : "") + delimiter +
                (operatorAnswerTime != null ? operatorAnswerTime.toString() : "") + delimiter +
                (operatorAnswerDuration != null ? operatorAnswerDuration.toString() : "") + delimiter +
                recordingFile;
    }
}
