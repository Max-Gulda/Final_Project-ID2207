package com.sep.system.controller;

import com.sep.system.auth.AuthenticationService;
import com.sep.system.requests.EventRequest;
import com.sep.system.user.*;
import com.sep.system.tasks.Task;
import com.sep.system.util.DataManager;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SEPController {
    private AuthenticationService authService;
    private DataManager dataManager;
    private User loggedInUser;
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel cards;

    public SEPController(AuthenticationService authService, DataManager dataManager, JFrame frame, CardLayout cardLayout, JPanel cards) {
        this.authService = authService;
        this.dataManager = dataManager;
        this.frame = frame;
        this.cardLayout = cardLayout;
        this.cards = cards;
    }

    // Handle the login process
    public void handleLogin(String username, String password) {
        loggedInUser = authService.authenticate(username, password);
        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(frame, "Login successful! Welcome, " + loggedInUser.getUsername() + "!");
            updateMainMenuForUser();
            cardLayout.show(cards, "MainMenu");
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid credentials. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Handle the logout process
    public void handleLogout() {
        loggedInUser = null;
        cardLayout.show(cards, "Login");
    }

    public void exitSystem() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Do you want to save changes before exiting?", "Exit Confirmation", JOptionPane.YES_NO_CANCEL_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Save data before exiting
            DataManager.saveUsers(authService.getUsers());
            System.out.println("Data saved successfully.");
            System.out.println("Exiting the system. Goodbye!");

            // Close the frame and terminate the program
            frame.dispose();
            System.exit(0);
        } else if (confirm == JOptionPane.NO_OPTION) {
            // Exit without saving
            System.out.println("Exiting the system without saving. Goodbye!");
            frame.dispose();
            System.exit(0);
        } else {
            // Cancelled, do nothing
            System.out.println("Exit cancelled.");
        }
    }


    // Handle the creation of a new user
    public void handleCreateUser(String username, String password, Role role) {
        authService.createUser(username, password, role);
        JOptionPane.showMessageDialog(frame, "User created successfully!");
        dataManager.saveUsers(authService.getUsers());
    }

    // Handle the creation of an event request by CSO
    public void handleCreateEventRequest(String eventName, String clientName, String description, double budget) {
        if (loggedInUser instanceof CSO) {
            CSO cso = (CSO) loggedInUser;
            cso.createEventRequest(eventName, clientName, description, budget);
            JOptionPane.showMessageDialog(frame, "Event request created successfully!");
            dataManager.saveUsers(authService.getUsers());  // Save the updated state
        } else {
            JOptionPane.showMessageDialog(frame, "You do not have permission to create event requests.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Handle processing event requests by different roles
    public void handleProcessEventRequests() {
        if (loggedInUser instanceof SCSO) {
            List<User> users = authService.getUsers();
            for (User user : users) {
                if (user instanceof CSO) {
                    CSO cso = (CSO) user;
                    for (EventRequest request : cso.getEventRequests()) {
                        if ("PENDING".equals(request.getStatus())) {
                            int option = JOptionPane.showConfirmDialog(frame,
                                    "Approve request: " + request.getEventName() + "?",
                                    "SCSO Approval", JOptionPane.YES_NO_OPTION);

                            if (option == JOptionPane.YES_OPTION) {
                                request.setStatus("APPROVED");
                                JOptionPane.showMessageDialog(frame, "Event request approved.");
                            } else {
                                request.setStatus("DISAPPROVED");
                                JOptionPane.showMessageDialog(frame, "Event request disapproved.");
                            }
                        }
                    }
                }
            }
            dataManager.saveUsers(authService.getUsers());  // Save the updated state after processing requests
        } else {
            JOptionPane.showMessageDialog(frame, "You do not have permission to process event requests.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void handleViewMyEventRequests() {
        if (loggedInUser instanceof CSO) {
            CSO cso = (CSO) loggedInUser;
            List<EventRequest> eventRequests = cso.getEventRequests();

            if (eventRequests.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "You have no event requests.", "View My Event Requests", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder requestsInfo = new StringBuilder();
            for (EventRequest request : eventRequests) {
                requestsInfo.append("Event Name: ").append(request.getEventName()).append("\n")
                        .append("Client Name: ").append(request.getClientName()).append("\n")
                        .append("Description: ").append(request.getDescription()).append("\n")
                        .append("Budget: ").append(request.getBudget()).append("\n")
                        .append("Status: ").append(request.getStatus()).append("\n")
                        .append("Budget Comment: ").append(request.getBudgetComment() != null ? request.getBudgetComment() : "N/A").append("\n")
                        .append("Finalized: ").append(request.isFinalized() ? "Yes" : "No").append("\n")
                        .append("\n");
            }

            JTextArea textArea = new JTextArea(requestsInfo.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JOptionPane.showMessageDialog(frame, scrollPane, "My Event Requests", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "You do not have permission to view event requests.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public String[] getSubTeamsForPSM() {
        if (loggedInUser instanceof ProductionServiceManager) {
            ProductionServiceManager psm = (ProductionServiceManager) loggedInUser;
            return psm.getSubTeams().keySet().toArray(new String[0]); // Returns the list of sub-team names
        }
        return new String[0];  // Return an empty array if the user is not a PSM
    }


    public void handleViewSubTeams() {
        if (loggedInUser instanceof ProductionServiceManager) {
            ProductionServiceManager psm = (ProductionServiceManager) loggedInUser;
            StringBuilder subTeamsInfo = new StringBuilder();

            psm.getSubTeams().forEach((subTeamName, subTeamMembers) -> {
                subTeamsInfo.append("Sub-Team: ").append(subTeamName).append("\n");
                subTeamsInfo.append("Members:\n");
                for (SimpleUser member : subTeamMembers) {
                    subTeamsInfo.append("  - ").append(member.getUsername()).append("\n");
                }
                subTeamsInfo.append("\n");
            });

            if (subTeamsInfo.length() == 0) {
                subTeamsInfo.append("No sub-teams available.");
            }

            JTextArea textArea = new JTextArea(subTeamsInfo.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);

            JOptionPane.showMessageDialog(frame, scrollPane, "View Sub-Teams", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "You do not have permission to view sub-teams.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Handle viewing all event requests by any logged-in user
    public void handleViewAllRequests() {
        StringBuilder requestsInfo = new StringBuilder();
        List<User> users = authService.getUsers();
        for (User user : users) {
            if (user instanceof CSO) {
                CSO cso = (CSO) user;
                for (EventRequest request : cso.getEventRequests()) {
                    requestsInfo.append("Event Name: ").append(request.getEventName()).append("\n")
                            .append("Client Name: ").append(request.getClientName()).append("\n")
                            .append("Description: ").append(request.getDescription()).append("\n")
                            .append("Budget: ").append(request.getBudget()).append("\n")
                            .append("Status: ").append(request.getStatus()).append("\n")
                            .append("Budget Comment: ").append(request.getBudgetComment() != null ? request.getBudgetComment() : "N/A").append("\n")
                            .append("Finalized: ").append(request.isFinalized() ? "Yes" : "No").append("\n")
                            .append("\n");
                }
            }
        }

        if (requestsInfo.length() == 0) {
            requestsInfo.append("No requests available.");
        }

        JOptionPane.showMessageDialog(frame, requestsInfo.toString(), "View All Requests", JOptionPane.INFORMATION_MESSAGE);
    }

    // Handle creating a task for a sub-team
    public void handleCreateTaskForSubTeam(String description, double budget, String subTeamName) {
        if (loggedInUser instanceof ProductionServiceManager) {
            ProductionServiceManager psm = (ProductionServiceManager) loggedInUser;

            // Create the task with the PSM as reference
            Task newTask = new Task(description, budget, psm);

            // Get sub-team members by name
            List<SimpleUser> subTeamMembers = psm.getSubTeams().get(subTeamName);

            if (subTeamMembers != null && !subTeamMembers.isEmpty()) {
                // Assign the task to each member of the sub-team
                for (SimpleUser member : subTeamMembers) {
                    member.assignTask(newTask);
                    System.out.println("Assigned task to: " + member.getUsername());
                }

                // Store the task in the PSM's task list for that sub-team
                psm.createTaskForSubTeam(subTeamName, description, budget);

                JOptionPane.showMessageDialog(frame, "Task created and assigned to sub-team: " + subTeamName);
                dataManager.saveUsers(authService.getUsers());  // Save the updated state
            } else {
                JOptionPane.showMessageDialog(frame, "Sub-team not found or has no members.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "You do not have permission to create tasks.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public void handleViewAllCreatedTasks() {
        if (loggedInUser instanceof ProductionServiceManager) {
            ProductionServiceManager psm = (ProductionServiceManager) loggedInUser;
            String tasksInfo = psm.viewAllCreatedTasks();
            JOptionPane.showMessageDialog(frame, tasksInfo, "All Created Tasks", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "You do not have permission to view tasks.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    public void handleFinalizeEventRequests() {
        if (loggedInUser instanceof AdminManager) {
            StringBuilder pendingRequestsInfo = new StringBuilder();
            List<User> users = authService.getUsers();

            // Collect all pending, approved event requests for finalization
            for (User user : users) {
                if (user instanceof CSO) {
                    CSO cso = (CSO) user;
                    for (EventRequest request : cso.getEventRequests()) {
                        if ("APPROVED".equals(request.getStatus()) && !request.isFinalized()) {
                            pendingRequestsInfo.append("Event Name: ").append(request.getEventName()).append("\n")
                                    .append("Client Name: ").append(request.getClientName()).append("\n")
                                    .append("Description: ").append(request.getDescription()).append("\n")
                                    .append("Budget: ").append(request.getBudget()).append("\n")
                                    .append("Status: ").append(request.getStatus()).append("\n")
                                    .append("Budget Comment: ").append(request.getBudgetComment() != null ? request.getBudgetComment() : "N/A").append("\n")
                                    .append("\n");
                        }
                    }
                }
            }

            if (pendingRequestsInfo.length() == 0) {
                JOptionPane.showMessageDialog(frame, "No pending event requests for finalization.", "Finalize Event Requests", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Display pending requests
            JTextArea textArea = new JTextArea(pendingRequestsInfo.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            Object[] options = {"Finalize", "Cancel"};

            int option = JOptionPane.showOptionDialog(frame, scrollPane, "Pending Event Requests",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (option == JOptionPane.YES_OPTION) {
                // Iterate over users again to finalize event requests
                for (User user : users) {
                    if (user instanceof CSO) {
                        CSO cso = (CSO) user;
                        for (EventRequest request : cso.getEventRequests()) {
                            if ("APPROVED".equals(request.getStatus()) && !request.isFinalized()) {
                                request.setFinalized(true);
                                JOptionPane.showMessageDialog(frame, "Event request for " + request.getEventName() + " has been finalized.");
                            }
                        }
                    }
                }
                dataManager.saveUsers(authService.getUsers());  // Save the updated state after finalizing requests
            }
        } else {
            JOptionPane.showMessageDialog(frame, "You do not have permission to finalize event requests.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Get the names of sub-teams managed by the logged-in PSM
    public String[] getSubTeamNamesForPSM() {
        if (loggedInUser instanceof ProductionServiceManager) {
            ProductionServiceManager psm = (ProductionServiceManager) loggedInUser;
            return psm.getSubTeams().keySet().toArray(new String[0]);
        }
        return new String[]{};
    }

    // Get tasks for the selected sub-team
    public List<Task> getTasksForSubTeam(String subTeamName) {
        if (loggedInUser instanceof ProductionServiceManager) {
            ProductionServiceManager psm = (ProductionServiceManager) loggedInUser;
            return psm.getTasksForSubTeam(subTeamName);
        }
        return new ArrayList<>();
    }


    // Update the main menu based on the user's role
    public void updateMainMenuForUser() {
        System.out.println("Logged-in user role: " + loggedInUser.getClass().getSimpleName());  // Debug log

        if (loggedInUser instanceof AdminManager) {
            cardLayout.show(cards, "AdminManager");
            System.out.println("Switching to AdminManager view.");
        } else if (loggedInUser instanceof ProductionServiceManager) {
            cardLayout.show(cards, "PSM");
            System.out.println("Switching to PSM view.");
        } else if (loggedInUser instanceof SCSO) {
            cardLayout.show(cards, "SCSO");
            System.out.println("Switching to SCSO view.");
        } else if (loggedInUser instanceof CSO) {
            cardLayout.show(cards, "CSO");
            System.out.println("Switching to CSO view.");
        } else if(loggedInUser instanceof SimpleUser){
            System.out.println(loggedInUser);
            cardLayout.show(cards, "SimpleUser");
        } else {
            JOptionPane.showMessageDialog(frame, "No view available for your role.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleViewMyTasks() {
        if (loggedInUser instanceof SimpleUser) {
            SimpleUser simpleUser = (SimpleUser) loggedInUser;
            List<Task> tasks = simpleUser.getAssignedTasks();  // Retrieve assigned tasks

            // Use a Set to track unique tasks
            Set<Task> uniqueTasks = new HashSet<>(tasks); // This removes duplicates

            if (uniqueTasks.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "You have no assigned tasks.", "View My Tasks", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder taskInfo = new StringBuilder();
                for (Task task : uniqueTasks) {
                    taskInfo.append(task.toString()).append("\n"); // Append each unique task
                }
                JTextArea textArea = new JTextArea(taskInfo.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                JOptionPane.showMessageDialog(frame, scrollPane, "My Tasks", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "You do not have permission to view tasks.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public User getLoggedInUser() {
        return loggedInUser; // Return the currently logged-in user
    }

    public List<Task> getAssignedTasksForUser() {
        if (loggedInUser instanceof SimpleUser) {
            SimpleUser simpleUser = (SimpleUser) loggedInUser;
            // Use a Set to avoid duplicates
            Set<Task> uniqueTasks = new HashSet<>(simpleUser.getAssignedTasks());
            return new ArrayList<>(uniqueTasks); // Convert back to List
        }
        return Collections.emptyList(); // Return an empty list if not a SimpleUser
    }


    public List<Task> getTasksCreatedByPSM() {
        if (loggedInUser instanceof ProductionServiceManager) {
            ProductionServiceManager psm = (ProductionServiceManager) loggedInUser;
            return psm.getTasksForPSM(); // Ensure this method exists in the ProductionServiceManager class
        }
        return Collections.emptyList(); // Return an empty list if the user is not a PSM
    }


    public void handleCreateSubTeam(String subTeamName) {
        if (loggedInUser instanceof ProductionServiceManager) {
            ProductionServiceManager psm = (ProductionServiceManager) loggedInUser;
            psm.createSubTeam(subTeamName);
            JOptionPane.showMessageDialog(frame, "Sub-team created successfully: " + subTeamName);
        } else {
            JOptionPane.showMessageDialog(frame, "You do not have permission to create a sub-team.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to handle the creation of a simple user
    public void handleCreateSimpleUser(String username, String password, String department, String subTeam) {
        if (loggedInUser instanceof ProductionServiceManager) {
            ProductionServiceManager psm = (ProductionServiceManager) loggedInUser;
            psm.createSimpleUser(username, password, department, subTeam);
            JOptionPane.showMessageDialog(frame, "Simple user created successfully: " + username);
        } else {
            JOptionPane.showMessageDialog(frame, "You do not have permission to create a simple user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleAddBudgetComment(Task task, String comment) {
        if (loggedInUser instanceof SimpleUser) {
            SimpleUser simpleUser = (SimpleUser) loggedInUser;
            simpleUser.addBudgetComment(task, comment);
            JOptionPane.showMessageDialog(frame, "Budget comment added successfully.");
        } else {
            JOptionPane.showMessageDialog(frame, "You do not have permission to add comments.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    // Utility method to find a component by its name in a container
    private Component findComponentByName(Container container, String name) {
        for (Component component : container.getComponents()) {
            if (name.equals(component.getName())) {
                return component;
            }
        }
        return null;
    }

    // Utility method to find a component by its button text in a container
    private Component findComponentByText(Container container, String text) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton && text.equals(((JButton) component).getText())) {
                return component;
            }
        }
        return null;
    }

    // Handle processing event requests as SCSO
    private void processEventRequestsAsSCSO() {
        List<User> users = authService.getUsers();
        for (User user : users) {
            if (user instanceof CSO) {
                CSO cso = (CSO) user;
                for (EventRequest request : cso.getEventRequests()) {
                    if ("PENDING".equals(request.getStatus())) {
                        int option = JOptionPane.showConfirmDialog(frame,
                                "Approve request: " + request.getEventName() + "?",
                                "SCSO Approval", JOptionPane.YES_NO_OPTION);

                        if (option == JOptionPane.YES_OPTION) {
                            request.setStatus("APPROVED");
                            JOptionPane.showMessageDialog(frame, "Event request approved.");
                        } else {
                            request.setStatus("DISAPPROVED");
                            JOptionPane.showMessageDialog(frame, "Event request disapproved.");
                        }
                    }
                }
            }
        }
        dataManager.saveUsers(authService.getUsers());  // Save the updated state after processing requests
    }

    // Handle processing event requests as Financial Manager
    private void processEventRequestsAsFinancialManager() {
        List<User> users = authService.getUsers();
        for (User user : users) {
            if (user instanceof CSO) {
                CSO cso = (CSO) user;
                for (EventRequest request : cso.getEventRequests()) {
                    if ("APPROVED".equals(request.getStatus()) && request.getBudgetComment().isEmpty()) {
                        StringBuilder message = new StringBuilder();
                        message.append("Event Name: ").append(request.getEventName()).append("\n");
                        message.append("Client Name: ").append(request.getClientName()).append("\n");
                        message.append("Description: ").append(request.getDescription()).append("\n");
                        message.append("Budget: ").append(request.getBudget()).append("\n");

                        String comment = JOptionPane.showInputDialog(frame, message.toString() + "\nAdd budget comment:");
                        if (comment != null && !comment.trim().isEmpty()) {
                            request.setBudgetComment(comment);
                            JOptionPane.showMessageDialog(frame, "Budget comment added successfully.");
                        } else {
                            JOptionPane.showMessageDialog(frame, "Budget comment not added. Please provide a valid comment.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        }
        dataManager.saveUsers(authService.getUsers());  // Save the updated state after processing requests
    }

    // Handle processing event requests as Admin Manager
    private void processEventRequestsAsAdminManager() {
        List<User> users = authService.getUsers();
        for (User user : users) {
            if (user instanceof CSO) {
                CSO cso = (CSO) user;
                for (EventRequest request : cso.getEventRequests()) {
                    if ("APPROVED".equals(request.getStatus()) && request.getBudgetComment() != null && !request.getBudgetComment().isEmpty() && !request.isFinalized()) {
                        StringBuilder message = new StringBuilder();
                        message.append("Event Name: ").append(request.getEventName()).append("\n");
                        message.append("Client Name: ").append(request.getClientName()).append("\n");
                        message.append("Description: ").append(request.getDescription()).append("\n");
                        message.append("Budget: ").append(request.getBudget()).append("\n");
                        message.append("Status: ").append(request.getStatus()).append("\n");
                        message.append("Budget Comment: ").append(request.getBudgetComment()).append("\n");

                        int option = JOptionPane.showConfirmDialog(frame,
                                message.toString() + "\nDo you approve this request?",
                                "Admin Manager Finalization", JOptionPane.YES_NO_OPTION);

                        if (option == JOptionPane.YES_OPTION) {
                            request.setFinalized(true);
                            request.setStatus("APPROVED");
                            JOptionPane.showMessageDialog(frame, "Event request finalized and approved.");
                        } else {
                            request.setStatus("DISAPPROVED");
                            JOptionPane.showMessageDialog(frame, "Event request disapproved.");
                        }
                    }
                }
            }
        }
        dataManager.saveUsers(authService.getUsers());  // Save the updated state after processing requests
    }
}
