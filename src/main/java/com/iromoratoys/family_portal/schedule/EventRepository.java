package com.iromoratoys.family_portal.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // 指定した期間と重なる予定を取得(月表示・週表示・日表示すべてで使う)
    @Query("SELECT e FROM Event e " +
           "WHERE e.startDateTime <= :rangeEnd AND e.endDateTime >= :rangeStart " +
           "ORDER BY e.startDateTime ASC")
    List<Event> findInRange(@Param("rangeStart") LocalDateTime rangeStart,
                             @Param("rangeEnd") LocalDateTime rangeEnd);
}
