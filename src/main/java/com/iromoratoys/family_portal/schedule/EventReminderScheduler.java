package com.iromoratoys.family_portal.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

// 「明日始まる予定」で、リマインド有効・未送信のものを送信時刻を過ぎていれば送る。
// 10分おきに確認するので、送信時刻にアプリが止まっていても起動後に追いついて送れる。
@Component
public class EventReminderScheduler {

    private final EventRepository repo;
    private final EventMailService mail;
    private final ReminderPolicy policy;

    public EventReminderScheduler(EventRepository repo, EventMailService mail, ReminderPolicy policy) {
        this.repo = repo;
        this.mail = mail;
        this.policy = policy;
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void sendReminders() {
        if (!mail.isEnabled()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrowStart = now.toLocalDate().plusDays(1).atStartOfDay();

        for (Event event : repo.findRemindable(tomorrowStart, tomorrowStart.plusDays(1))) {
            if (now.isBefore(policy.reminderAt(event.getStartDateTime()))) {
                continue;
            }
            if (mail.sendReminder(event)) {
                repo.markReminderSent(event.getId());
            }
        }
    }
}
