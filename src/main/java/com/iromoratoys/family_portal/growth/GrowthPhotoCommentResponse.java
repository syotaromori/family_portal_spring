package com.iromoratoys.family_portal.growth;

import java.time.LocalDateTime;

public class GrowthPhotoCommentResponse {

    private Long id;
    private Long photoId;
    private String author;
    private String content;
    private LocalDateTime createdAt;

    public GrowthPhotoCommentResponse(GrowthPhotoComment comment) {
        this.id = comment.getId();
        this.photoId = comment.getPhoto().getId();
        this.author = comment.getAuthor();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
    }

    public Long getId() { return id; }
    public Long getPhotoId() { return photoId; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
