package com.iromoratoys.family_portal.growth;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrowthPhotoCommentService {

    // コメントできるメンバー(固定)
    public static final List<String> ALLOWED_AUTHORS = List.of(
            "翔太郎", "奈津子", "彩乃", "結菜", "羚弥"
    );

    private final GrowthPhotoCommentRepository repo;
    private final GrowthPhotoRepository photoRepo;

    public GrowthPhotoCommentService(GrowthPhotoCommentRepository repo, GrowthPhotoRepository photoRepo) {
        this.repo = repo;
        this.photoRepo = photoRepo;
    }

    public List<GrowthPhotoComment> findByPhotoId(Long photoId) {
        return repo.findByPhotoIdOrderByCreatedAtAsc(photoId);
    }

    public GrowthPhotoComment create(Long photoId, GrowthPhotoCommentRequest req) {

        if (!ALLOWED_AUTHORS.contains(req.getAuthor())) {
            throw new IllegalArgumentException("投稿者はメンバーの中から選択してください");
        }

        GrowthPhoto photo = photoRepo.findById(photoId)
                .orElseThrow(() -> new IllegalArgumentException("指定された写真が見つかりません"));

        GrowthPhotoComment comment = new GrowthPhotoComment();
        comment.setPhoto(photo);
        comment.setAuthor(req.getAuthor());
        comment.setContent(req.getContent());

        return repo.save(comment);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    /**
     * 指定した写真に紐づくコメントをすべて削除する。
     * GrowthPhoto削除時に呼び出す。
     */
    public void deleteAllByPhotoId(Long photoId) {
        repo.deleteAll(repo.findByPhotoId(photoId));
    }
}
