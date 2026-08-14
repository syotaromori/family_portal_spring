package com.iromoratoys.family_portal.growth;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // 一覧(表示順)
    @GetMapping
    public List<CategoryResponse> getAll() {
        return service.findAll()
                .stream()
                .map(CategoryResponse::new)
                .toList();
    }

    // 登録(将来のカスタマイズ・追加用)
    @PostMapping
    public CategoryResponse create(@Valid @RequestBody CategoryRequest req) {
        return new CategoryResponse(service.create(req));
    }

    // 更新
    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id,
                                    @Valid @RequestBody CategoryRequest req) {
        return new CategoryResponse(service.update(id, req));
    }

    // 削除
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
