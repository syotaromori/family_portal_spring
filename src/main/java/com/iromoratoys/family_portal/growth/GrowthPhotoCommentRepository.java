package com.iromoratoys.family_portal.growth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrowthPhotoCommentRepository extends JpaRepository<GrowthPhotoComment, Long> {
    List<GrowthPhotoComment> findByPhotoIdOrderByCreatedAtAsc(Long photoId);
    List<GrowthPhotoComment> findByPhotoId(Long photoId);
}
