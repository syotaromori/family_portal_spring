package com.iromoratoys.family_portal.schedule;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EventReminderSchedulerTest {

    private final ReminderPolicy policy = new ReminderPolicy(18);

    @Test
    void reminderAtIsPreviousDayAtConfiguredHour() {
        LocalDateTime start = LocalDateTime.of(2026, 9, 20, 9, 30);
        assertEquals(LocalDateTime.of(2026, 9, 19, 18, 0), policy.reminderAt(start));
    }

    @Test
    void eventCreatedAfterReminderTimeIsMarkedAsPast() {
        LocalDateTime start = LocalDateTime.of(2026, 9, 20, 9, 30);
        assertFalse(policy.isAlreadyPast(start, LocalDateTime.of(2026, 9, 19, 17, 59)));
        assertTrue(policy.isAlreadyPast(start, LocalDateTime.of(2026, 9, 19, 18, 1)));
    }

    @Test
    void schedulerSkipsWhenMailDisabled() {
        EventRepository repo = mock(EventRepository.class);
        EventMailService mail = mock(EventMailService.class);
        when(mail.isEnabled()).thenReturn(false);

        new EventReminderScheduler(repo, mail, policy).sendReminders();

        verify(repo, never()).findRemindable(any(), any());
    }

    @Test
    void schedulerMarksSentOnlyWhenMailSucceeds() {
        // 開始が「明日」の予定にして、実行時刻に依存しないよう送信時刻(0時)を過ぎている設定にする
        ReminderPolicy alwaysDue = new ReminderPolicy(0);
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);

        Event ok = eventWithId(1L, tomorrow);
        Event ng = eventWithId(2L, tomorrow);

        EventRepository repo = mock(EventRepository.class);
        EventMailService mail = mock(EventMailService.class);
        when(mail.isEnabled()).thenReturn(true);
        when(repo.findRemindable(any(), any())).thenReturn(List.of(ok, ng));
        when(mail.sendReminder(ok)).thenReturn(true);
        when(mail.sendReminder(ng)).thenReturn(false);

        new EventReminderScheduler(repo, mail, alwaysDue).sendReminders();

        verify(repo).markReminderSent(1L);
        verify(repo, never()).markReminderSent(2L);
    }

    private Event eventWithId(Long id, LocalDateTime start) {
        Event e = mock(Event.class);
        when(e.getId()).thenReturn(id);
        when(e.getStartDateTime()).thenReturn(start);
        return e;
    }
}
