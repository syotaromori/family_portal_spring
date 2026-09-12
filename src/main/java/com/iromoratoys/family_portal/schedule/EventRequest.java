package com.iromoratoys.family_portal.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class EventRequest {

    @NotBlank(message = "タイトルは必須です by spring")
    private String title;

    @NotNull(message = "開始日時は必須です by spring")
    private LocalDateTime startDateTime;

    @NotNull(message = "終了日時は必須です by spring")
    private LocalDateTime endDateTime;

    private boolean allDay;

    private String location;

    @NotBlank(message = "担当者は必須です by spring")
    private String assignee;

    private String memo;

    public String getTitle() { return title; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public boolean isAllDay() { return allDay; }
    public String getLocation() { return location; }
    public String getAssignee() { return assignee; }
    public String getMemo() { return memo; }
}
