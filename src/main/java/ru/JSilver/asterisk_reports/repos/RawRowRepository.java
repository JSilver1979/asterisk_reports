package ru.JSilver.asterisk_reports.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.JSilver.asterisk_reports.data.RawRow;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RawRowRepository extends JpaRepository<RawRow, Long> {

    List<RawRow> findAllByCallDateTimeBetween(LocalDateTime from, LocalDateTime to);

}
