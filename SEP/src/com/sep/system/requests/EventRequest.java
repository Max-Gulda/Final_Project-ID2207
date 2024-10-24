package com.sep.system.requests;

import java.io.Serializable;

public class EventRequest implements Serializable {
    private String eventName;
    private String clientName;
    private String description;
    private double budget;
    private String status; // PENDING, APPROVED, DISAPPROVED
    private String budgetComment;
    private boolean finalized;

    public EventRequest(String eventName, String clientName, String description, double budget) {
        this.eventName = eventName;
        this.clientName = clientName;
        this.description = description;
        this.budget = budget;
        this.status = "PENDING";
        this.budgetComment = "";
        this.finalized = false;
    }

    public String getEventName() {
        return eventName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getDescription() {
        return description;
    }

    public double getBudget() {
        return budget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBudgetComment() {
        return budgetComment;
    }

    public void setBudgetComment(String budgetComment) {
        this.budgetComment = budgetComment;
    }

    public boolean isFinalized() {
        return finalized;
    }

    public void setFinalized(boolean finalized) {
        this.finalized = finalized;
    }

    @Override
    public String toString() {
        return "EventRequest{" +
                "eventName='" + eventName + '\'' +
                ", clientName='" + clientName + '\'' +
                ", description='" + description + '\'' +
                ", budget=" + budget +
                ", status='" + status + '\'' +
                ", budgetComment='" + budgetComment + '\'' +
                ", finalized=" + finalized +
                '}';
    }
}
