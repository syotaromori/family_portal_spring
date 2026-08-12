package com.iromoratoys.family_portal.growth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class GrowthRecordRequest {

    @NotNull(message = "childIdは必須です by spring")
    private Long childId;

    @NotNull(message = "記録日は必須です by spring")
    private LocalDate recordDate;

    @Positive(message = "身長は正の数にしてください by spring")
    private Double heightCm;

    @Positive(message = "体重は正の数にしてください by spring")
    private Double weightKg;

    private String memo;

    public Long getChildId() { return childId; }
    public LocalDate getRecordDate() { return recordDate; }
    public Double getHeightCm() { return heightCm; }
    public Double getWeightKg() { return weightKg; }
    public String getMemo() { return memo; }
}
