package ru.JSilver.asterisk_reports.repos;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.JSilver.asterisk_reports.data.CallEntity;

import java.util.List;

@Repository
public interface CallRepository extends JpaRepository<CallEntity, Long> {
    List<CallEntity> findCallsByDate(Pageable pageable);
}
