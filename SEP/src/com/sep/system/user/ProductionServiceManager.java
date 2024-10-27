package com.sep.system.user;

import com.sep.system.auth.AuthenticationService;
import com.sep.system.requests.FinancialRequest;
import com.sep.system.requests.StaffRecruitmentRequest;
import com.sep.system.tasks.Task;

import java.util.*;


public class ProductionServiceManager extends User {
    private Map<String, List<SimpleUser>> subTeams;
    private Map<String, List<Task>> tasks;
    private AuthenticationService authService; // Reference to AuthenticationService
    private List<StaffRecruitmentRequest> staffRecruitmentRequests;
    private List<FinancialRequest> financialRequests;

    // Constructor
    public ProductionServiceManager(String username, String password) {
        super(username, password, Role.PRODUCTION_SERVICE_MANAGER);
        this.subTeams = new HashMap<>();
        this.tasks = new HashMap<>();
        this.staffRecruitmentRequests = new ArrayList<>();
        this.financialRequests = new ArrayList<>();
    }

    public void setAuthService(AuthenticationService authService) {
        this.authService = authService;
    }

    // Method to create a new sub-team
    public void createSubTeam(String subTeamName) {
        if (!subTeams.containsKey(subTeamName)) {
            subTeams.put(subTeamName, new ArrayList<>());
            tasks.put(subTeamName, new ArrayList<>()); // Initialize task list for the sub-team
            System.out.println("Sub-team created: " + subTeamName);
        } else {
            System.out.println("Sub-team already exists: " + subTeamName);
        }
    }

    // Method to add a new user to an existing sub-team
    public void addMemberToSubTeam(SimpleUser newUser, String subTeam) {
        if (subTeams.containsKey(subTeam)) {
            subTeams.get(subTeam).add(newUser);
            newUser.setSubTeam(subTeam); // Set the user's sub-team
            System.out.println("User added to sub-team: " + subTeam);
        } else {
            System.out.println("Sub-team does not exist: " + subTeam);
        }
    }

    // Method to create a Simple User and add to a specified sub-team using AuthenticationService
    public void createSimpleUser(String username, String password, String department, String subTeam) {
        SimpleUser newUser = authService.createSimpleUser(username, password, department, subTeam, this);
        addMemberToSubTeam(newUser, subTeam); // Automatically adds the user to the sub-team
        System.out.println("Simple User created and added to sub-team: " + subTeam);
    }

    // Method to create and assign a task to a sub-team
    public void createTaskForSubTeam(String subTeamName, String description, double budget) {
        if (subTeams.containsKey(subTeamName)) {
            Task task = new Task(description, budget, this);  // Pass PSM reference to the task
            tasks.get(subTeamName).add(task);

            // Assign task to each member of the sub-team
            for (SimpleUser member : subTeams.get(subTeamName)) {
                member.assignTask(task);  // Assign the task to each SimpleUser
            }
            System.out.println("Task created and assigned to sub-team: " + subTeamName);
        } else {
            System.out.println("Sub-team does not exist: " + subTeamName);
        }
    }

    // Method to view tasks for a sub-team
    public String viewTasksForSubTeam(String subTeamName) {
        StringBuilder taskInfo = new StringBuilder();
        if (tasks.containsKey(subTeamName)) {
            taskInfo.append("Tasks for Sub-Team: ").append(subTeamName).append("\n");
            for (Task task : tasks.get(subTeamName)) {
                taskInfo.append(task.toString()).append("\n\n");
            }
        } else {
            taskInfo.append("Sub-team does not exist: ").append(subTeamName);
        }
        return taskInfo.toString();
    }

    // Method to view all tasks created by the PSM across all sub-teams
    public String viewAllCreatedTasks() {
        StringBuilder allTasksInfo = new StringBuilder();
        if (!tasks.isEmpty()) {
            for (Map.Entry<String, List<Task>> entry : tasks.entrySet()) {
                String subTeamName = entry.getKey();
                List<Task> subTeamTasks = entry.getValue();

                allTasksInfo.append("Sub-Team: ").append(subTeamName).append("\n");
                for (Task task : subTeamTasks) {
                    allTasksInfo.append("\t").append(task.toString()).append("\n");
                }
            }
        } else {
            allTasksInfo.append("No tasks available.");
        }
        return allTasksInfo.toString();
    }

    // Implement the login method
    @Override
    public boolean login(String username, String password) {
        return this.getUsername().equals(username) && this.getPassword().equals(password);
    }

    // Getters for tasks and sub-teams
    public Map<String, List<Task>> getTasks() {
        return tasks;
    }

    public Map<String, List<SimpleUser>> getSubTeams() {
        return subTeams;
    }

    // Get all tasks assigned to a SimpleUser in a specific sub-team
    public List<Task> getTasksForSubTeam(String subTeamName) {
        if (subTeams.containsKey(subTeamName) && !subTeams.get(subTeamName).isEmpty()) {
            List<Task> allTasks = new ArrayList<>();
            for (SimpleUser member : subTeams.get(subTeamName)) {
                allTasks.addAll(member.getAssignedTasks()); // Collect tasks assigned to each SimpleUser
            }
            return allTasks;
        }
        return Collections.emptyList(); // Return empty list if no tasks or sub-team is not found
    }

    public List<Task> getTasksForPSM() {
        List<Task> allTasks = new ArrayList<>();
        for (List<Task> taskList : tasks.values()) {
            allTasks.addAll(taskList); // Collect all tasks for this PSM
        }
        return allTasks; // Return all tasks created by this PSM
    }

    public void addCommentToTask(Task task, String comment){
        for (List<Task> taskList : tasks.values()) {
            for (Task t : taskList) {
                if(t.equals(task)){
                    t.setBudgetComment(comment);

                }
            }
        }
    }

    public void addPlanToTask(Task task, String plan){
        for (List<Task> taskList : tasks.values()) {
            for (Task t : taskList) {
                if(t.equals(task)){
                    t.setPlan(plan);
                }
            }
        }
    }

    public void createStaffRecruitmentRequest(String details) {
        StaffRecruitmentRequest request = new StaffRecruitmentRequest(details, this);
        staffRecruitmentRequests.add(request);
        System.out.println("Staff recruitment request created and sent to HR Manager.");
    }

    // Getter for staff recruitment requests
    public List<StaffRecruitmentRequest> getStaffRecruitmentRequests() {
        return staffRecruitmentRequests;
    }

    public void createFinancialRequest(String details) {
        FinancialRequest request = new FinancialRequest(details, this);
        financialRequests.add(request);
        System.out.println("Financial request created and sent to Financial Manager.");
    }

    // Getter for financial requests
    public List<FinancialRequest> getFinancialRequests() {
        return financialRequests;
    }

    public void setStatusFinancialRequest(FinancialRequest financialRequest){
        for (FinancialRequest fr : financialRequests) {
            if(fr.equals(financialRequest)){
                fr.setStatus(financialRequest.getStatus());
            }
        }
    }

}
