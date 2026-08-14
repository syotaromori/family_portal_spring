package com.iromoratoys.family_portal.growth;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrowthRecordService {

    private final GrowthRecordRepository repo;
    private final ChildRepository childRepo;
    private final CategoryRepository categoryRepo;

    public GrowthRecordService(GrowthRecordRepository repo, ChildRepository childRepo, CategoryRepository categoryRepo) {
        this.repo = repo;
        this.childRepo = childRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<GrowthRecord> findAll(Long childId) {
        return repo.findByChildIdOrderByRecordDateDesc(childId);
    }

    public GrowthRecord create(GrowthRecordRequest req) {

        Child child = childRepo.findById(req.getChildId())
                .orElseThrow(() -> new IllegalArgumentException("指定された子どもが見つかりません"));

        GrowthRecord record = new GrowthRecord();
        record.setChild(child);
        record.setCategory(resolveCategory(req.getCategoryId()));
        record.setRecordDate(req.getRecordDate());
        record.setHeightCm(req.getHeightCm());
        record.setWeightKg(req.getWeightKg());
        record.setMemo(req.getMemo());

        return repo.save(record);
    }

    public GrowthRecord update(Long id, GrowthRecordUpdateRequest req) {

        GrowthRecord record = repo.findById(id).orElseThrow();

        record.setCategory(resolveCategory(req.getCategoryId()));
        record.setRecordDate(req.getRecordDate());
        record.setHeightCm(req.getHeightCm());
        record.setWeightKg(req.getWeightKg());
        record.setMemo(req.getMemo());

        return repo.save(record);
    }

    private Category resolveCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepo.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("指定されたカテゴリが見つかりません"));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public GrowthRecord getById(Long id) {
        return repo.findById(id).orElseThrow();
    }
}
