package com.iromoratoys.family_portal.growth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrowthPhotoRepository extends JpaRepository<GrowthPhoto, Long> {
    List<GrowthPhoto> findByRecordId(Long recordId);
}
