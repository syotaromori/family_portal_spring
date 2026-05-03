package com.iromoratoys.family_portal.goal;

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

    public Goal create(GoalRequest req) {

        if (req.getCurrentAmount() > req.getTargetAmount()) {
            throw new IllegalArgumentException("現在金額が目標を超えています");
        }

        Goal goal = new Goal();
        goal.setChild(req.getChild());
        goal.setTargetAmount(req.getTargetAmount());
        goal.setCurrentAmount(req.getCurrentAmount());

        return repo.save(goal);
    }

    public Goal update(Long id, GoalUpdateRequest req) {

        Goal goal = repo.findById(id).orElseThrow();

        if (req.getCurrentAmount() > req.getTargetAmount()) {
            throw new IllegalArgumentException("現在金額が目標を超えています");
        }

        goal.setTargetAmount(req.getTargetAmount());
        goal.setCurrentAmount(req.getCurrentAmount());

        return repo.save(goal);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public Goal getById(Long id) {
        return repo.findById(id).orElseThrow();
    }
}