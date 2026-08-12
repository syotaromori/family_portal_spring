package com.iromoratoys.family_portal.growth;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
public class GrowthPhotoController {

    private final GrowthPhotoService service;

    public GrowthPhotoController(GrowthPhotoService service) {
        this.service = service;
    }

    // 一覧(記録ごと)
    @GetMapping("/api/growth-records/{recordId}/photos")
    public List<GrowthPhotoResponse> getAll(@PathVariable Long recordId) {
        return service.findByRecordId(recordId)
                .stream()
                .map(GrowthPhotoResponse::new)
                .toList();
    }

    // アップロード
    @PostMapping("/api/growth-records/{recordId}/photos")
    public GrowthPhotoResponse upload(@PathVariable Long recordId,
                                       @RequestParam("file") MultipartFile file) {
        return new GrowthPhotoResponse(service.upload(recordId, file));
    }

    // 削除
    @DeleteMapping("/api/growth-photos/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
