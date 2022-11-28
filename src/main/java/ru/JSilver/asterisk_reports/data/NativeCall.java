package ru.JSilver.asterisk_reports.data;

import ru.JSilver.asterisk_reports.dto.NativeCallDto;

import javax.persistence.*;

@NamedNativeQuery(name = "try_use_native_query",
query = "SELECT calldate, src, dst, disposition, linkedid, max(duration) AS dur, COUNT(disposition) AS resultRows "
        + "FROM cdr "
        + "WHERE LENGTH(src) > 4 "
        + "and DATE(calldate) BETWEEN ? and ? "
        + "and dst IN (1112) "
        + "group by linkedid, disposition;",
resultSetMapping = "native_result_set")
@SqlResultSetMapping(name = "native_result_set",
classes = @ConstructorResult(targetClass = NativeCallDto.class,
columns = {
        @ColumnResult(name = "linkedId", type = String.class),
        @ColumnResult(name = "disposition", type = String.class),
        @ColumnResult(name = "resultRows", type = Integer.class)
}))
@Entity
public class NativeCall {

    @Id
    @Column(name = "sequence")
    private Long sequence;
    @Column(name = "calldate")
    private String calldate;
    @Column(name = "src")
    private String src;
    @Column(name = "dst")
    private String dst;
    @Column(name = "disposition")
    private String disposition;
    @Column(name="linkedid")
    private String linkedId;
}
