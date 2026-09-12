package com.iromoratoys.family_portal.schedule;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    // 予定の担当者(固定)。「全員」は家族共有の予定を表す。
    public static final List<String> ALLOWED_ASSIGNEES = List.of(
            "翔太郎", "奈津子", "彩乃", "結菜", "羚弥", "全員"
    );

    private final EventRepository repo;

    public EventService(EventRepository repo) {
        this.repo = repo;
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
        event.setAssignee(req.getAssignee());
        event.setMemo(req.getMemo());

        return repo.save(event);
    }

    public Event update(Long id, EventRequest req) {

        validate(req);

        Event event = repo.findById(id).orElseThrow();

        event.setTitle(req.getTitle());
        event.setStartDateTime(req.getStartDateTime());
        event.setEndDateTime(req.getEndDateTime());
        event.setAllDay(req.isAllDay());
        event.setLocation(req.getLocation());
        event.setAssignee(req.getAssignee());
        event.setMemo(req.getMemo());

        return repo.save(event);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    private void validate(EventRequest req) {
        if (req.getEndDateTime().isBefore(req.getStartDateTime())) {
            throw new IllegalArgumentException("終了日時は開始日時より後にしてください");
        }
        if (!ALLOWED_ASSIGNEES.contains(req.getAssignee())) {
            throw new IllegalArgumentException("担当者はメンバーの中から選択してください");
        }
    }
}
