package ru.JSilver.asterisk.reports.data;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "cdr")
@NamedNativeQuery(name = "CallEntity.findCallsByDate",
query = "SELECT cdr.sequence, min(cdr.calldate) as startdate, max(cdr.calldate) as endwaitdate, max(cel.eventtime) as endcalldate, cdr.src, group_concat(distinct(cdr.dst)) as extensions, group_concat(distinct(cdr.disposition))as statuses, cdr.linkedid\n" +
        "FROM cdr\n" +
        "INNER JOIN cel\n" +
        "on cdr.linkedid = cel.linkedid\n" +
        "WHERE \n" +
        "DATE(cdr.calldate) = '2022-09-05'\n" +
        "group by cdr.linkedid having extensions like '%1112%'",
resultClass = CallEntity.class)
public class CallEntity {
    @Id
    @Column(name = "sequence")
    private Long sequence;

    @Column(name = "linkedid")
    private String linkedId;
    @Column(name = "startdate")
    private LocalDateTime callStartDateTime;

    @Column(name = "endwaitdate")
    private LocalDateTime callEndWaitDateTime;

    @Column(name = "endcalldate")
    private LocalDateTime callEndDateTime;

    @Column(name = "src")
    private String src;

    @Column(name = "extensions")
    private String extensions;

    @Column(name = "statuses")
    private String statuses;
}
