package com.iromoratoys.family_portal.growth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CategoryDataSeeder implements CommandLineRunner {

    private final CategoryRepository repo;

    public CategoryDataSeeder(CategoryRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {

        if (repo.count() > 0) {
            return;
        }

        seed("身長・体重", 1, "#4fc3f7");
        seed("はじめてできた", 2, "#ffb74d");
        seed("行事・お出かけ", 3, "#ba68c8");
        seed("日常", 4, "#81c784");
    }

    private void seed(String name, int sortOrder, String colorCode) {
        Category category = new Category();
        category.setName(name);
        category.setSortOrder(sortOrder);
        category.setColorCode(colorCode);
        repo.save(category);
    }
}
