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
import java.util.Set;
import java.util.UUID;

@Service
public class GrowthPhotoService {

    private static final Set<String> VIDEO_EXTENSIONS = Set.of("mov", "mp4", "m4v");

    private final GrowthPhotoRepository repo;
    private final GrowthRecordRepository recordRepo;
    private final GrowthPhotoCommentRepository commentRepo;
    private final VideoConverter videoConverter;

    @Value("${app.upload-dir:uploads/growth-images}")
    private String uploadDir;

    public GrowthPhotoService(GrowthPhotoRepository repo, GrowthRecordRepository recordRepo,
                               GrowthPhotoCommentRepository commentRepo, VideoConverter videoConverter) {
        this.repo = repo;
        this.recordRepo = recordRepo;
        this.commentRepo = commentRepo;
        this.videoConverter = videoConverter;
    }

    public List<GrowthPhoto> findByRecordId(Long recordId) {
        return repo.findByRecordId(recordId);
    }

    public GrowthPhoto upload(Long recordId, MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("ファイルが空です");
        }

        String mediaType = detectMediaType(file);
        if (mediaType == null) {
            throw new IllegalArgumentException("画像または動画ファイルのみアップロードできます");
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
            String baseName = UUID.randomUUID().toString();
            String filename = baseName + ext;

            Path target = dir.resolve(filename);
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }

            if ("VIDEO".equals(mediaType)) {
                filename = convertToMp4IfPossible(dir, target, baseName, filename);
            }

            GrowthPhoto photo = new GrowthPhoto();
            photo.setRecord(record);
            photo.setImagePath(filename);
            photo.setMediaType(mediaType);

            return repo.save(photo);

        } catch (IOException e) {
            throw new RuntimeException("ファイル保存に失敗しました", e);
        }
    }

    /**
     * 動画をH.264のMP4に変換し、保存するファイル名を返す。
     * 変換できなかった場合(ffmpeg未導入・失敗など)は元の動画をそのまま使い、元のファイル名を返す。
     */
    private String convertToMp4IfPossible(Path dir, Path original, String baseName, String originalFilename)
            throws IOException {

        // 元ファイルの拡張子が .mp4 の場合に上書きし合わないよう、一旦別名で出力する
        Path converting = dir.resolve(baseName + ".converting.mp4");
        if (!videoConverter.convertToMp4(original, converting)) {
            return originalFilename;
        }

        Files.delete(original);
        String mp4Filename = baseName + ".mp4";
        Files.move(converting, dir.resolve(mp4Filename), StandardCopyOption.REPLACE_EXISTING);
        return mp4Filename;
    }

    /**
     * アップロードされたファイルの種別を判定する("IMAGE" / "VIDEO"、対象外はnull)。
     * iPhoneの動画(.MOV)はブラウザによってContent-Typeが video/quicktime にならず
     * 空や application/octet-stream で届くことがあるため、拡張子でも判定する。
     */
    private String detectMediaType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                return "IMAGE";
            }
            if (contentType.startsWith("video/")) {
                return "VIDEO";
            }
        }

        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            String ext = original.substring(original.lastIndexOf(".") + 1).toLowerCase();
            if (VIDEO_EXTENSIONS.contains(ext)) {
                return "VIDEO";
            }
        }
        return null;
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
