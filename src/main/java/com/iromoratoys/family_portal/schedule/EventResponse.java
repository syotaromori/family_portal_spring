package com.iromoratoys.family_portal.schedule;

import java.time.LocalDateTime;

public class EventResponse {

    private Long id;
    private String title;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private boolean allDay;
    private String location;
    private String assignee;
    private String memo;
    private LocalDateTime createdAt;

    public EventResponse(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.startDateTime = event.getStartDateTime();
        this.endDateTime = event.getEndDateTime();
        this.allDay = event.isAllDay();
        this.location = event.getLocation();
        this.assignee = event.getAssignee();
        this.memo = event.getMemo();
        this.createdAt = event.getCreatedAt();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public boolean isAllDay() { return allDay; }
    public String getLocation() { return location; }
    public String getAssignee() { return assignee; }
    public String getMemo() { return memo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
