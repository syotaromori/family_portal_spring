package com.iromoratoys.family_portal.schedule;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "spring_event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;

    @Column(name = "all_day")
    private boolean allDay;

    @Column(name = "location")
    private String location;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "spring_event_assignee", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "assignee")
    private List<String> assignees = new ArrayList<>();

    @Column(name = "memo", length = 1000)
    private String memo;

    // 前日リマインドを送るか / 送信済みか
    // 列追加前から存在する行はNULLになるため、primitiveではなくBooleanで受けてgetterでfalse扱いにする
    @Column(name = "reminder_enabled")
    private Boolean reminderEnabled = false;

    @Column(name = "reminder_sent")
    private Boolean reminderSent = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // getter/setter
    public Long getId() { return id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDateTime getStartDateTime() { return startDateTime; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }

    public LocalDateTime getEndDateTime() { return endDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    public boolean isAllDay() { return allDay; }
    public void setAllDay(boolean allDay) { this.allDay = allDay; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<String> getAssignees() { return assignees; }
    public void setAssignees(List<String> assignees) { this.assignees = assignees; }

    public String getMemo() { return memo; }
    public void setMemo(String memo) { this.memo = memo; }

    public boolean isReminderEnabled() { return Boolean.TRUE.equals(reminderEnabled); }
    public void setReminderEnabled(boolean reminderEnabled) { this.reminderEnabled = reminderEnabled; }

    public boolean isReminderSent() { return Boolean.TRUE.equals(reminderSent); }
    public void setReminderSent(boolean reminderSent) { this.reminderSent = reminderSent; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
