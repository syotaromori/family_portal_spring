package com.iromoratoys.family_portal.schedule;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    // 予定の担当者一覧(固定選択肢をDjango側に提供)
    @GetMapping("/api/event-assignees")
    public List<String> getAssignees() {
        return EventService.ALLOWED_ASSIGNEES;
    }

    // 期間内の予定一覧(月表示・週表示・日表示すべてこれを使う)
    @GetMapping
    public List<EventResponse> getInRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return service.findInRange(start, end)
                .stream()
                .map(EventResponse::new)
                .toList();
    }

    // 1件取得
    @GetMapping("/{id}")
    public EventResponse getById(@PathVariable Long id) {
        return new EventResponse(service.getById(id));
    }

    // 登録
    @PostMapping
    public EventResponse create(@Valid @RequestBody EventRequest req) {
        return new EventResponse(service.create(req));
    }

    // 更新
    @PutMapping("/{id}")
    public EventResponse update(@PathVariable Long id, @Valid @RequestBody EventRequest req) {
        return new EventResponse(service.update(id, req));
    }

    // 削除
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
