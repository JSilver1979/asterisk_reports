package ru.JSilver.asterisk.reports.data;

import java.io.Serializable;

public class RowID implements Serializable {
    private String uniqueId;
    private Long sequence;

    public RowID() {
    }

    public RowID(String uniqueId, Long sequence) {
        this.uniqueId = uniqueId;
        this.sequence = sequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RowID rowID = (RowID) o;

        if (!uniqueId.equals(rowID.uniqueId)) return false;
        return sequence.equals(rowID.sequence);
    }

    @Override
    public int hashCode() {
        int result = uniqueId.hashCode();
        result = 31 * result + sequence.hashCode();
        return result;
    }
}
