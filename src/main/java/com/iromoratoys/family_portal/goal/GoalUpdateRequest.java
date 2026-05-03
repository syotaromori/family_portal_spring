package com.iromoratoys.family_portal.goal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class GoalUpdateRequest {

    @NotNull
    @Min(1)
    private Integer targetAmount;

    @NotNull
    @Min(0)
    private Integer currentAmount;

    public int getCurrentAmount() { return currentAmount; }
    public int getTargetAmount() {
        return targetAmount;
    }
}

