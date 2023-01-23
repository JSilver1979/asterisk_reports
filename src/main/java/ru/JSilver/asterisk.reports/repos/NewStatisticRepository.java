package ru.JSilver.asterisk.reports.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.JSilver.asterisk.reports.data.StatisticEntity;

import java.util.List;

@Repository
public interface NewStatisticRepository extends JpaRepository<StatisticEntity, String> {
    @Query(name = "StatisticEntity.getCallsForStats", nativeQuery = true)
    List<StatisticEntity> getCallsForStats(String fromDate, String toDate, String group);
}
