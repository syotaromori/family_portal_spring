package com.iromoratoys.family_portal;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoalService {

    private final GoalRepository repo;

    public GoalService(GoalRepository repo) {
        this.repo = repo;
    }

    public List<Goal> findAll(String child) {
        if ("ALL".equals(child)) {
            return repo.findAll();
        }
        return repo.findByChild(child);
    }

    public Goal create(Goal goal) {
        return repo.save(goal);
    }

    public Goal update(Long id, Goal updated) {
        Goal goal = repo.findById(id).orElseThrow();

        goal.setTargetAmount(updated.getTargetAmount());
        goal.setCurrentAmount(updated.getCurrentAmount());

        return repo.save(goal);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Goal getById(Long id) {
        return repo.findById(id).orElseThrow();
    }
}