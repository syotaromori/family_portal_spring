package com.iromoratoys.family_portal.growth;

import jakarta.validation.constraints.NotBlank;

public class GrowthPhotoCommentRequest {

    @NotBlank(message = "投稿者は必須です by spring")
    private String author;

    @NotBlank(message = "コメントは必須です by spring")
    private String content;

    public String getAuthor() { return author; }
    public String getContent() { return content; }
}
