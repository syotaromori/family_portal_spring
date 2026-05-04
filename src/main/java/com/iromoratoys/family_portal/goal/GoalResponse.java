package com.iromoratoys.family_portal.goal;

public class GoalResponse {

    private Long id;
    private String child;
    private Integer targetAmount;
    private Integer currentAmount;
    private int progressPercent;

    public GoalResponse(Goal goal) {
        this.id = goal.getId();
        this.child = goal.getChild();
        this.targetAmount = goal.getTargetAmount();
        this.currentAmount = goal.getCurrentAmount();
        if (goal.getTargetAmount() > 0) {
            this.progressPercent =
                    (int)((goal.getCurrentAmount() * 100.0) / goal.getTargetAmount());
        } else {
            this.progressPercent = 0;
        }
        this.progressPercent = Math.min(100, this.progressPercent);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }


    public Integer getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Integer targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Integer getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(Integer currentAmount) {
        this.currentAmount = currentAmount;
    }

    public int getProgressPercent() {
        return this.progressPercent;
    }
}
