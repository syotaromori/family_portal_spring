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
    public List<Goal> getAll(@RequestParam String child) {
        // 親用
        if ("ALL".equals(child)) {
            return repo.findAll();
        }
        // 子供用
        return repo.findByChild(child);
    }

    // 登録
    @PostMapping
    public Goal create(@RequestBody Goal goal) {
        return repo.save(goal);
    }

    @GetMapping("/{id}")
    public Goal getById(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public Goal update(@PathVariable Long id, @RequestBody Goal updated) {
        Goal goal = repo.findById(id).orElseThrow();

        goal.setChild(updated.getChild());
        goal.setTargetAmount(updated.getTargetAmount());
        goal.setCurrentAmount(updated.getCurrentAmount());

        return repo.save(goal);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }

}