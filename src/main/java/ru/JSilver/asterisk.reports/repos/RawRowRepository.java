package ru.JSilver.asterisk.reports.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.JSilver.asterisk.reports.data.RowEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RawRowRepository extends JpaRepository<RowEntity, Long> {

    List<RowEntity> findAllByCallDateTimeBetweenOrderByCallDateTime(LocalDateTime from, LocalDateTime to);
}
