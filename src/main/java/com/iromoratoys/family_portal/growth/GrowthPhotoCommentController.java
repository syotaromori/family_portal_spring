package com.iromoratoys.family_portal.growth;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
public class GrowthPhotoCommentController {

    private final GrowthPhotoCommentService service;

    public GrowthPhotoCommentController(GrowthPhotoCommentService service) {
        this.service = service;
    }

    // コメントできるメンバー一覧(固定選択肢をDjango側に提供)
    @GetMapping("/api/comment-authors")
    public List<String> getAuthors() {
        return GrowthPhotoCommentService.ALLOWED_AUTHORS;
    }

    // 一覧(写真ごと)
    @GetMapping("/api/growth-photos/{photoId}/comments")
    public List<GrowthPhotoCommentResponse> getAll(@PathVariable Long photoId) {
        return service.findByPhotoId(photoId)
                .stream()
                .map(GrowthPhotoCommentResponse::new)
                .toList();
    }

    // 登録
    @PostMapping("/api/growth-photos/{photoId}/comments")
    public GrowthPhotoCommentResponse create(@PathVariable Long photoId,
                                              @Valid @RequestBody GrowthPhotoCommentRequest req) {
        return new GrowthPhotoCommentResponse(service.create(photoId, req));
    }

    // 削除
    @DeleteMapping("/api/growth-photo-comments/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
