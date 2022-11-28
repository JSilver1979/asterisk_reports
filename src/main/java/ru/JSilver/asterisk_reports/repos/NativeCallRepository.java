package ru.JSilver.asterisk_reports.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.JSilver.asterisk_reports.data.NativeCall;
import ru.JSilver.asterisk_reports.dto.NativeCallDto;

import java.util.List;

@Repository
public interface NativeCallRepository extends JpaRepository<NativeCall, Long> {

    @Query(name = "try_use_native_query",nativeQuery = true)
    List<NativeCallDto> nativeQuery(String fromDate, String toDate);
}
