package ru.JSilver.asterisk.reports.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.JSilver.asterisk.reports.data.NewStatisticEntity;

import java.util.List;

@Repository
public interface NewStatisticRepository extends JpaRepository<NewStatisticEntity, String> {
    @Query(name = "NewStatisticEntity.getCallsForStats", nativeQuery = true)
    List<NewStatisticEntity> getCallsForStats(String fromDate, String toDate, String group);
}
