package ru.JSilver.asterisk.reports.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NamedNativeQuery(name = "NewStatisticEntity.getCallsForStats",
        query = "SELECT " +
                "group_concat(distinct dst) as destination, " +
                "group_concat(distinct disposition separator ';') as statuses, " +
                "linkedid, " +
                "max(billsec) as talktime, " +
                "count(linkedid) as rowsNum " +
                "FROM cdr " +
                "WHERE LENGTH(src) > 4 " +
                "and DATE(calldate) BETWEEN ? and ? " +
                "group by linkedid having destination like ?;",
        resultSetMapping = "newStatisticEntitySet")
@SqlResultSetMapping(name = "newStatisticEntitySet",
        classes = @ConstructorResult(targetClass = NewStatisticEntity.class,
                columns = {
                        @ColumnResult(name = "linkedid", type = String.class),
                        @ColumnResult(name = "destination", type = String.class),
                        @ColumnResult(name = "statuses", type = String.class),
                        @ColumnResult(name = "talktime", type = Integer.class),
                        @ColumnResult(name = "rowsNum", type = Integer.class)
                }))

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cdr")
public class NewStatisticEntity {

    @Id
    @Column(name = "linkedid")
    private String linkedId;

    @Column(name = "destination")
    private String destination;

    @Column(name = "statuses")
    private String statuses;

    @Column(name = "talktime")
    private Integer talkTime;

    @Column(name = "rowsNum")
    private Integer rowsNumber;
}
