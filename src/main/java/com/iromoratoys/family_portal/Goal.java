package com.iromoratoys.family_portal;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "spring_goal")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String child;

    @Column(name = "target_amount")
    private int targetAmount;

    @Column(name = "current_amount")
    private int currentAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // getter/setter
    public Long getId() { return id; }

    public String getChild() { return child; }
    public void setChild(String child) { this.child = child; }

    public int getTargetAmount() { return targetAmount; }
    public void setTargetAmount(int targetAmount) { this.targetAmount = targetAmount; }

    public int getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(int currentAmount) { this.currentAmount = currentAmount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}