package com.iromoratoys.family_portal.goal;

public class GoalResponse {

    private Long id;
    private String child;
    private Integer targetAmount;
    private Integer currentAmount;

    public GoalResponse(Goal goal) {
        this.id = goal.getId();
        this.child = goal.getChild();
        this.targetAmount = goal.getTargetAmount();
        this.currentAmount = goal.getCurrentAmount();
    }

    // getter
}
