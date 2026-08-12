package com.iromoratoys.family_portal.growth;

import java.time.LocalDateTime;

public class GrowthPhotoResponse {

    private Long id;
    private Long recordId;
    private String url;
    private LocalDateTime createdAt;

    public GrowthPhotoResponse(GrowthPhoto photo) {
        this.id = photo.getId();
        this.recordId = photo.getRecord().getId();
        // 静的リソース配信パス(WebConfigの設定と対応)
        this.url = "/images/growth/" + photo.getImagePath();
        this.createdAt = photo.getCreatedAt();
    }

    public Long getId() { return id; }
    public Long getRecordId() { return recordId; }
    public String getUrl() { return url; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
