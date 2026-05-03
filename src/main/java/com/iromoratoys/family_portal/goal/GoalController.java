package com.iromoratoys.family_portal.goal;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
@CrossOrigin
public class GoalController {

    private final GoalService service;

    public GoalController(GoalService service) {
        this.service = service;
    }

    // 一覧
    @GetMapping
    public List<Goal> getAll(@RequestParam String child) {
        return service.findAll(child);
    }

    // 登録
    @PostMapping
    public GoalResponse create(@Valid @RequestBody GoalRequest req) {
        return new GoalResponse(service.create(req));
    }

    // 更新
    @PutMapping("/{id}")
    public GoalResponse update(@PathVariable Long id,
                               @Valid @RequestBody GoalUpdateRequest req) {
        return new GoalResponse(service.update(id, req));
    }

    @GetMapping("/{id}")
    public Goal getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // 削除
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}