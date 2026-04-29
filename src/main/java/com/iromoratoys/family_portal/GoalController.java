package com.iromoratoys.family_portal;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
@CrossOrigin
public class GoalController {

    private final GoalRepository repo;

    public GoalController(GoalRepository repo) {
        this.repo = repo;
    }

    // 一覧取得
    @GetMapping
    public List<Goal> getAll() {
        return repo.findAll();
    }

    // 登録
    @PostMapping
    public Goal create(@RequestBody Goal goal) {
        return repo.save(goal);
    }
}