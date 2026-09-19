package com.iromoratoys.family_portal.growth;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "spring_growth_photo")
public class GrowthPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "growth_record_id")
    private GrowthRecord record;

    @Column(name = "image_path")
    private String imagePath;

    // "IMAGE" or "VIDEO"。動画対応前に登録された行はNULLのため、画像として扱う
    @Column(name = "media_type", length = 10)
    private String mediaType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // getter/setter
    public Long getId() { return id; }

    public GrowthRecord getRecord() { return record; }
    public void setRecord(GrowthRecord record) { this.record = record; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
