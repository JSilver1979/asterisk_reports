package ru.JSilver.asterisk.reports.data;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@IdClass(RowID.class)
@Table(name = "cdr")
@Data
public class RowEntity {

    @Id
    @Column(name = "uniqueid")
    private String uniqueId;

    @Id
    @Column(name = "sequence")
    private Long sequence;

    @Column(name = "calldate")
    private LocalDateTime callDateTime;

    @Column(name = "src")
    private String src;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "disposition")
    private String disposition;

    @Column(name = "linkedid")
    private String linkedId;

    @Column(name = "dst")
    private String dst;

    @Column(name = "recordingfile")
    private String recordingFile;

    @Column(name = "lastapp")
    private String lastApp;
}
