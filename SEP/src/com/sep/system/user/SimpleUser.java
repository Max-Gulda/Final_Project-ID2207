package com.sep.system.user;

import com.sep.system.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class SimpleUser extends User {
    private String department;
    private String subTeam;
    private ProductionServiceManager manager;
    private List<Task> assignedTasks;  // List to store tasks assigned to the SimpleUser

    // Constructor
    public SimpleUser(String username, String password, String department, String subTeam, ProductionServiceManager manager) {
        super(username, password, Role.SIMPLE_USER);
        this.department = department;
        this.manager = manager;
        this.subTeam = subTeam;
        this.assignedTasks = new ArrayList<>();  // Initialize assigned tasks
    }

    // Implement the login method
    @Override
    public boolean login(String username, String password) {
        return this.getUsername().equals(username) && this.getPassword().equals(password);
    }

    // Getter for department
    public String getDepartment() {
        return department;
    }

    // Setter for department
    public void setDepartment(String department) {
        this.department = department;
    }

    // Getter for subTeam
    public String getSubTeam() {
        return subTeam;
    }

    // Setter for subTeam
    public void setSubTeam(String subTeam) {
        this.subTeam = subTeam;
    }

    public ProductionServiceManager getManager() {
        return manager;
    }

    // Method to add a plan to a task
    public void addPlanToTask(Task task, String plan) {
        if (task != null) {
            task.setPlan(plan);
            manager.addPlanToTask(task, plan);
            System.out.println("Plan added to task by: " + getUsername());
        } else {
            System.out.println("Task is null, cannot add plan.");
        }
    }

    // Method to add a budget comment
    public void addBudgetComment(Task task, String comment) {
        if (task != null) {
            task.setBudgetComment(comment);
            manager.addCommentToTask(task,comment);
            System.out.println("Budget comment added by: " + getUsername());
        } else {
            System.out.println("Task is null, cannot add budget comment.");
        }
    }

    // Method to assign a task to this SimpleUser
    public void assignTask(Task task) {
        if (task != null) {
            assignedTasks.add(task);
            System.out.println("Task assigned to " + getUsername() + ": " + task.getDescription());
        } else {
            System.out.println("Cannot assign a null task.");
        }
    }

    // Getter for the list of tasks assigned to this SimpleUser
    public List<Task> getAssignedTasks() {
        return assignedTasks;
    }

    // Method to remove a task (optional, in case task removal is needed)
    public void removeTask(Task task) {
        if (assignedTasks.contains(task)) {
            assignedTasks.remove(task);
            System.out.println("Task removed for " + getUsername() + ": " + task.getDescription());
        } else {
            System.out.println("Task not found in the assigned tasks.");
        }
    }

    @Override
    public String toString() {
        return String.format("User: %s, Department: %s, Sub-Team: %s, Assigned Tasks: %s",
                getUsername(), department, subTeam, assignedTasks);
    }
}
