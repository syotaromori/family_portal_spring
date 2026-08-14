package com.iromoratoys.family_portal.growth;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    public List<Category> findAll() {
        return repo.findAllByOrderBySortOrderAsc();
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
        repo.deleteById(id);
    }
}
