package com.iromoratoys.family_portal.growth;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GrowthRecordResponse {

    private Long id;
    private Long childId;
    private String childName;
    private LocalDate recordDate;
    private Double heightCm;
    private Double weightKg;
    private String memo;
    private LocalDateTime createdAt;

    public GrowthRecordResponse(GrowthRecord record) {
        this.id = record.getId();
        this.childId = record.getChild().getId();
        this.childName = record.getChild().getName();
        this.recordDate = record.getRecordDate();
        this.heightCm = record.getHeightCm();
        this.weightKg = record.getWeightKg();
        this.memo = record.getMemo();
        this.createdAt = record.getCreatedAt();
    }

    public Long getId() { return id; }
    public Long getChildId() { return childId; }
    public String getChildName() { return childName; }
    public LocalDate getRecordDate() { return recordDate; }
    public Double getHeightCm() { return heightCm; }
    public Double getWeightKg() { return weightKg; }
    public String getMemo() { return memo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
