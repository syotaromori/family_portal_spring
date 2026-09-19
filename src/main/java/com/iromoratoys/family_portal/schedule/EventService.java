package com.iromoratoys.family_portal.schedule;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    // 予定の担当者(固定)。複数選択できるので「全員」は個別に5人選ぶ形で表現する。
    public static final List<String> ALLOWED_ASSIGNEES = List.of(
            "翔太郎", "奈津子", "彩乃", "結菜", "羚弥"
    );

    private final EventRepository repo;
    private final EventMailService mail;
    private final ReminderPolicy reminderPolicy;

    public EventService(EventRepository repo, EventMailService mail, ReminderPolicy reminderPolicy) {
        this.repo = repo;
        this.mail = mail;
        this.reminderPolicy = reminderPolicy;
    }

    public List<Event> findInRange(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        return repo.findInRange(rangeStart, rangeEnd);
    }

    public Event getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public Event create(EventRequest req) {

        validate(req);

        Event event = new Event();
        event.setTitle(req.getTitle());
        event.setStartDateTime(req.getStartDateTime());
        event.setEndDateTime(req.getEndDateTime());
        event.setAllDay(req.isAllDay());
        event.setLocation(req.getLocation());
        event.setAssignees(new ArrayList<>(req.getAssignees()));
        event.setMemo(req.getMemo());
        applyReminder(event, req);

        Event saved = repo.save(event);
        mail.notifyCreated(saved);
        return saved;
    }

    public Event update(Long id, EventRequest req) {

        validate(req);

        Event event = repo.findById(id).orElseThrow();

        event.setTitle(req.getTitle());
        event.setStartDateTime(req.getStartDateTime());
        event.setEndDateTime(req.getEndDateTime());
        event.setAllDay(req.isAllDay());
        event.setLocation(req.getLocation());
        event.setAssignees(new ArrayList<>(req.getAssignees()));
        event.setMemo(req.getMemo());
        applyReminder(event, req);

        return repo.save(event);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // 日時を変えた予定でも再びリマインドされるよう、登録・更新のたびに送信済みフラグを決め直す
    private void applyReminder(Event event, EventRequest req) {
        event.setReminderEnabled(req.isReminderEnabled());
        event.setReminderSent(reminderPolicy.isAlreadyPast(req.getStartDateTime(), LocalDateTime.now()));
    }

    private void validate(EventRequest req) {
        if (req.getEndDateTime().isBefore(req.getStartDateTime())) {
            throw new IllegalArgumentException("終了日時は開始日時より後にしてください");
        }
        for (String assignee : req.getAssignees()) {
            if (!ALLOWED_ASSIGNEES.contains(assignee)) {
                throw new IllegalArgumentException("担当者はメンバーの中から選択してください");
            }
        }
    }
}
