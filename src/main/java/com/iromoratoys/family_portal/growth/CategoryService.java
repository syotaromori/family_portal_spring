package com.iromoratoys.family_portal.growth;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repo;
    private final GrowthRecordRepository growthRecordRepo;

    public CategoryService(CategoryRepository repo, GrowthRecordRepository growthRecordRepo) {
        this.repo = repo;
        this.growthRecordRepo = growthRecordRepo;
    }

    public List<Category> findAll() {
        return repo.findAllByOrderBySortOrderAsc();
    }

    public Category getById(Long id) {
        return repo.findById(id).orElseThrow();
    }

    public Category create(CategoryRequest req) {
        Category category = new Category();
        category.setName(req.getName());
        category.setSortOrder(req.getSortOrder());
        category.setColorCode(req.getColorCode());

        return repo.save(category);
    }

    public Category update(Long id, CategoryRequest req) {
        Category category = repo.findById(id).orElseThrow();

        category.setName(req.getName());
        category.setSortOrder(req.getSortOrder());
        category.setColorCode(req.getColorCode());

        return repo.save(category);
    }

    public void delete(Long id) {
        // このカテゴリを使っている記録を「未分類」に戻してから削除する
        List<GrowthRecord> affected = growthRecordRepo.findByCategoryId(id);
        for (GrowthRecord r : affected) {
            r.setCategory(null);
            growthRecordRepo.save(r);
        }
        repo.deleteById(id);
    }
}
