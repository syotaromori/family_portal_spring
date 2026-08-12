package com.iromoratoys.family_portal.growth;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/children")
@CrossOrigin
public class ChildController {

    private final ChildService service;

    public ChildController(ChildService service) {
        this.service = service;
    }

    // 一覧
    @GetMapping
    public List<ChildResponse> getAll() {
        return service.findAll()
                .stream()
                .map(ChildResponse::new)
                .toList();
    }

    // 登録
    @PostMapping
    public ChildResponse create(@Valid @RequestBody ChildRequest req) {
        return new ChildResponse(service.create(req));
    }

    // 更新
    @PutMapping("/{id}")
    public ChildResponse update(@PathVariable Long id,
                                 @Valid @RequestBody ChildUpdateRequest req) {
        return new ChildResponse(service.update(id, req));
    }

    @GetMapping("/{id}")
    public ChildResponse getById(@PathVariable Long id) {
        return new ChildResponse(service.getById(id));
    }

    // 削除
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
