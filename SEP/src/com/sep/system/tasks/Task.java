package com.sep.system.tasks;

import com.sep.system.user.ProductionServiceManager;

import java.util.Objects;

public class Task {
    private String description;
    private double budget;
    private String budgetComment;
    private String plan;
    private boolean needsReview;
    private ProductionServiceManager assignedPSM;  // Add PSM reference

    // Constructor with PSM reference
    public Task(String description, double budget, ProductionServiceManager assignedPSM) {
        this.description = description;
        this.budget = budget;
        this.needsReview = false;
        this.assignedPSM = assignedPSM;  // Set the PSM reference
    }

    // Getters and setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public String getBudgetComment() {
        return budgetComment;
    }

    public void setBudgetComment(String budgetComment) {
        this.budgetComment = budgetComment;
        this.needsReview = true;  // Set to true if a comment on budget is made
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public boolean isNeedsReview() {
        return needsReview;
    }

    public ProductionServiceManager getAssignedPSM() {
        return assignedPSM;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Task task = (Task) obj;
        return Double.compare(task.budget, budget) == 0 &&
                description.equals(task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, budget, budgetComment);
    }



    @Override
    public String toString() {
        return "[" + description + ", " + budget +
                ", " + (budgetComment != null ? budgetComment : "N/A") +
                ", " + (plan != null ? plan : "N/A") + "\nNeeds Review: " + needsReview + "]";
    }
}
