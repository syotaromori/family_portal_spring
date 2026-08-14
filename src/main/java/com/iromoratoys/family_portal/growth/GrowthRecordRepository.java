package com.iromoratoys.family_portal.growth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GrowthRecordRepository extends JpaRepository<GrowthRecord, Long> {
    List<GrowthRecord> findByChildIdOrderByRecordDateDesc(Long childId);
    List<GrowthRecord> findByCategoryId(Long categoryId);
}
