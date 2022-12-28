package ru.JSilver.asterisk.reports.data;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NamedNativeQuery(name = "StatisticEntity.getCallsForStatistic",
        query = "SELECT calldate, src, dst, disposition, linkedid, max(duration) AS dur, COUNT(disposition) AS resultRows "
                + "FROM cdr "
                + "WHERE LENGTH(src) > 4 "
                + "and DATE(calldate) BETWEEN ? and ? "
                + "and dst IN (?) "
                + "group by linkedid, disposition;",
        resultSetMapping = "statisticEntitySet")
@SqlResultSetMapping(name = "statisticEntitySet",
        classes = @ConstructorResult(targetClass = StatisticEntity.class,
                columns = {
                        @ColumnResult(name = "linkedid", type = String.class),
                        @ColumnResult(name = "disposition", type = String.class),
                        @ColumnResult(name = "resultRows", type = Integer.class)
                }))

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticEntity {

    @Id
    @Column(name = "linkedid")
    private String linkedId;

    @Column(name = "disposition")
    private String disposition;

    @Column(name = "resultRows")
    private Integer resultRows;
}
