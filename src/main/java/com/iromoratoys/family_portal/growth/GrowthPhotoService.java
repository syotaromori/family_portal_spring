package com.iromoratoys.family_portal.growth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class GrowthPhotoService {

    private final GrowthPhotoRepository repo;
    private final GrowthRecordRepository recordRepo;
    private final GrowthPhotoCommentRepository commentRepo;

    @Value("${app.upload-dir:uploads/growth-images}")
    private String uploadDir;

    public GrowthPhotoService(GrowthPhotoRepository repo, GrowthRecordRepository recordRepo,
                               GrowthPhotoCommentRepository commentRepo) {
        this.repo = repo;
        this.recordRepo = recordRepo;
        this.commentRepo = commentRepo;
    }

    public List<GrowthPhoto> findByRecordId(Long recordId) {
        return repo.findByRecordId(recordId);
    }

    public GrowthPhoto upload(Long recordId, MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("ファイルが空です");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("画像ファイルのみアップロードできます");
        }

        GrowthRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new IllegalArgumentException("指定された記録が見つかりません"));

        try {
            Path dir = Paths.get(uploadDir);
            Files.createDirectories(dir);

            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf("."));
            }
            String filename = UUID.randomUUID() + ext;

            Path target = dir.resolve(filename);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }

            GrowthPhoto photo = new GrowthPhoto();
            photo.setRecord(record);
            photo.setImagePath(filename);

            return repo.save(photo);

        } catch (IOException e) {
            throw new RuntimeException("ファイル保存に失敗しました", e);
        }
    }

    public void delete(Long id) {

        GrowthPhoto photo = repo.findById(id).orElseThrow();

        commentRepo.deleteAll(commentRepo.findByPhotoId(id));

        try {
            Path target = Paths.get(uploadDir).resolve(photo.getImagePath());
            Files.deleteIfExists(target);
        } catch (IOException e) {
            // ファイル削除に失敗してもDBレコードの削除は続行する
        }

        repo.deleteById(id);
    }

    /**
     * 指定した記録に紐づく写真(ファイル+DBレコード)をすべて削除する。
     * GrowthRecord削除時に呼び出す。
     */
    public void deleteAllByRecordId(Long recordId) {
        List<GrowthPhoto> photos = repo.findByRecordId(recordId);
        for (GrowthPhoto photo : photos) {
            commentRepo.deleteAll(commentRepo.findByPhotoId(photo.getId()));
            try {
                Path target = Paths.get(uploadDir).resolve(photo.getImagePath());
                Files.deleteIfExists(target);
            } catch (IOException e) {
                // ファイル削除に失敗してもDBレコードの削除は続行する
            }
        }
        repo.deleteAll(photos);
    }
}
