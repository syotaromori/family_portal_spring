package com.iromoratoys.family_portal.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    // 指定した期間と重なる予定を取得(月表示・週表示・日表示すべてで使う)
    @Query("SELECT e FROM Event e " +
           "WHERE e.startDateTime <= :rangeEnd AND e.endDateTime >= :rangeStart " +
           "ORDER BY e.startDateTime ASC")
    List<Event> findInRange(@Param("rangeStart") LocalDateTime rangeStart,
                             @Param("rangeEnd") LocalDateTime rangeEnd);

    // 開始日時が [from, to) にあり、リマインド有効で未送信の予定
    @Query("SELECT e FROM Event e " +
           "WHERE e.reminderEnabled = true AND e.reminderSent = false " +
           "AND e.startDateTime >= :from AND e.startDateTime < :to " +
           "ORDER BY e.startDateTime ASC")
    List<Event> findRemindable(@Param("from") LocalDateTime from,
                               @Param("to") LocalDateTime to);

    // 送信済みにする(画面での編集を上書きしないよう、この列だけ更新する)
    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.reminderSent = true WHERE e.id = :id")
    void markReminderSent(@Param("id") Long id);
}
