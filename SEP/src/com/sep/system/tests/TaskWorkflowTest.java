package com.sep.system.tests;

import com.sep.system.auth.AuthenticationService;
import com.sep.system.tasks.Task;
import com.sep.system.user.*;

import java.util.List;

public class TaskWorkflowTest {

    public static void main(String[] args) {
        // Step 0: Initialize AuthenticationService with default users
        List<User> defaultUsers = AuthenticationService.initializeDefaultUsers();
        AuthenticationService authService = new AuthenticationService(defaultUsers);

        // Find or initialize the ProductionServiceManager (psm) from default users
        ProductionServiceManager psm = (ProductionServiceManager) defaultUsers.stream()
                .filter(user -> user instanceof ProductionServiceManager)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("PSM not found in default users"));

        // Assign the authService to the psm
        psm.setAuthService(authService);

        // Step 1: Create a sub-team
        String subTeamName = "TechTeam";
        psm.createSubTeam(subTeamName);
        assert psm.getSubTeams().containsKey(subTeamName) : "Sub-team should be created";

        // Step 2: Add a SimpleUser to the sub-team
        psm.createSimpleUser("devUser", "devPass", "IT", subTeamName);
        SimpleUser simpleUser = psm.getSubTeams().get(subTeamName).get(0);
        assert simpleUser != null : "SimpleUser should be created and added to sub-team";
        assert simpleUser.getSubTeam().equals(subTeamName) : "SimpleUser should be in the correct sub-team";

        // Step 3: Assign a task to the sub-team
        String taskDescription = "Develop Feature A";
        double taskBudget = 5000.0;
        psm.createTaskForSubTeam(subTeamName, taskDescription, taskBudget);
        Task assignedTask = psm.getTasks().get(subTeamName).get(0);
        assert assignedTask != null : "Task should be created and assigned to sub-team";
        assert taskDescription.equals(assignedTask.getDescription()) : "Task description should match";
        assert taskBudget == assignedTask.getBudget() : "Task budget should match";

        // Step 4: User adds a budget comment and a plan
        String budgetComment = "Budget approved";
        String taskPlan = "Phase 1: Design, Phase 2: Development, Phase 3: Testing";
        simpleUser.addBudgetComment(assignedTask, budgetComment);
        simpleUser.addPlanToTask(assignedTask, taskPlan);
        assert assignedTask.getBudgetComment().equals(budgetComment) : "Budget comment should be added by SimpleUser";
        assert assignedTask.getPlan().equals(taskPlan) : "Task plan should be added by SimpleUser";

        // Step 5: Check if the task is marked as needing review due to the comment
        assert assignedTask.needsReview() : "Task should be marked for review after budget comment";

        // Step 6: PSM views all tasks in the sub-team
        String tasksForSubTeam = psm.viewTasksForSubTeam(subTeamName);
        assert tasksForSubTeam.contains(taskDescription) : "Sub-team tasks should include the assigned task";

        // Step 7: PSM views all created tasks across sub-teams
        String allTasks = psm.viewAllCreatedTasks();
        assert allTasks.contains(taskDescription) : "All created tasks should include the assigned task";

        System.out.println("Task workflow test passed successfully.");
    }
}

