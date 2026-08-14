package com.iromoratoys.family_portal.growth;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "spring_growth_photo_comment")
public class GrowthPhotoComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "photo_id")
    private GrowthPhoto photo;

    @Column(name = "author")
    private String author;

    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // getter/setter
    public Long getId() { return id; }

    public GrowthPhoto getPhoto() { return photo; }
    public void setPhoto(GrowthPhoto photo) { this.photo = photo; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
