package com.iromoratoys.family_portal.schedule;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// 前日リマインドを「いつ送るか」のルール(開始日の前日の reminder-hour 時)
@Component
public class ReminderPolicy {

    private final int reminderHour;

    public ReminderPolicy(@Value("${app.notification.reminder-hour:18}") int reminderHour) {
        this.reminderHour = reminderHour;
    }

    public LocalDateTime reminderAt(LocalDateTime eventStart) {
        return eventStart.toLocalDate().minusDays(1).atTime(reminderHour, 0);
    }

    // 登録・更新した時点で送信時刻を過ぎていれば、リマインドは送らない(直前に作った予定へ即通知しない)
    public boolean isAlreadyPast(LocalDateTime eventStart, LocalDateTime now) {
        return reminderAt(eventStart).isBefore(now);
    }
}
