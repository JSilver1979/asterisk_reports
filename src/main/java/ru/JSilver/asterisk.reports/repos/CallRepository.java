package ru.JSilver.asterisk.reports.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.JSilver.asterisk.reports.data.CallEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CallRepository extends JpaRepository<CallEntity, String> {

    @Query(name = "CallEntity.getCalls",
            nativeQuery = true)
    List<CallEntity> getCalls(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to, @Param("qGroup") String group);
}
