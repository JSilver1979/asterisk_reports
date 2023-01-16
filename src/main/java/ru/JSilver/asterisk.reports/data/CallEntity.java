package ru.JSilver.asterisk.reports.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NamedNativeQuery(name = "CallEntity.getCalls",
        query = "SELECT " +
                "linkedid, " +
                "MIN(calldate) as start_time, " +
                "MAX(calldate) as end_time, " +
                "src," +
                "group_concat(distinct dst) as destination, " +
                "group_concat(distinct disposition separator ';') as statuses, " +
                "max(billsec) as talktime, " +
                "count(linkedid) as rowsNum, " +
                "max(recordingfile) as recordingfile " +
                "FROM cdr " +
                "WHERE LENGTH(src) > 4 " +
                "and calldate BETWEEN :from and :to " +
                "group by linkedid having destination like :qGroup ",
        resultSetMapping = "callEntitySet")
@SqlResultSetMapping(name = "callEntitySet",
        classes = @ConstructorResult(targetClass = CallEntity.class,
                columns = {
                        @ColumnResult(name = "linkedid", type = String.class),
                        @ColumnResult(name = "start_time", type = LocalDateTime.class),
                        @ColumnResult(name = "end_time", type = LocalDateTime.class),
                        @ColumnResult(name = "src", type = String.class),
                        @ColumnResult(name = "destination", type = String.class),
                        @ColumnResult(name = "statuses", type = String.class),
                        @ColumnResult(name = "talktime", type = Long.class),
                        @ColumnResult(name = "rowsNum", type = Integer.class),
                        @ColumnResult(name = "recordingfile", type = String.class)
                }
                ))
@Entity
@Table(name = "cdr")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallEntity {

    @Id
    @Column(name = "linkedid")
    private String linkedId;

    @Column(name = "start_time")
    private LocalDateTime callStartTime;

    @Column(name = "end_time")
    private LocalDateTime callEndTime;

    @Column(name = "src")
    private String incomingCallNumber;

    @Column(name = "destination")
    private String callDestination;

    @Column(name = "statuses")
    private String callStatuses;

    @Column(name = "talktime")
    private Long callTalkTime;

    @Column(name = "rowsNum")
    private Integer callRowsNumber;

    @Column(name = "recordingfile")
    private String recordingFile;
}
