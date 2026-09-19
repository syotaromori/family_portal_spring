package com.iromoratoys.family_portal.growth;

import java.time.LocalDateTime;

public class GrowthPhotoResponse {

    private Long id;
    private Long recordId;
    private String url;
    private String mediaType;
    private LocalDateTime createdAt;

    public GrowthPhotoResponse(GrowthPhoto photo) {
        this.id = photo.getId();
        this.recordId = photo.getRecord().getId();
        // 静的リソース配信パス(WebConfigの設定と対応)
        this.url = "/images/growth/" + photo.getImagePath();
        // 動画対応前のデータ(NULL)は画像扱い
        this.mediaType = photo.getMediaType() != null ? photo.getMediaType() : "IMAGE";
        this.createdAt = photo.getCreatedAt();
    }

    public Long getId() { return id; }
    public Long getRecordId() { return recordId; }
    public String getUrl() { return url; }
    public String getMediaType() { return mediaType; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
