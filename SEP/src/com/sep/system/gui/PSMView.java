// File: com/sep/system/gui/PSMView.java
package com.sep.system.gui;

import com.sep.system.controller.SEPController;
import com.sep.system.requests.FinancialRequest;
import com.sep.system.requests.StaffRecruitmentRequest;
import com.sep.system.tasks.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PSMView {
    private JPanel psmPanel;
    private SEPController controller;
    private JTextArea taskDetailsArea;

    public PSMView(SEPController controller) {
        this.controller = controller;
        initPSMPanel();
    }

    private void initPSMPanel() {
        psmPanel = new JPanel(new BorderLayout());

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5)); // 0 rows, 1 column, 5px gaps
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        JLabel welcomeLabel = new JLabel("Production Service Manager Menu", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        psmPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Button to create a task for a sub-team
        JButton createTaskButton = new JButton("Create Task for Sub-Team");
        createTaskButton.addActionListener(e -> showCreateTaskDialog());
        buttonPanel.add(createTaskButton);

        // Button to create a new sub-team
        JButton createSubTeamButton = new JButton("Create Sub-Team");
        createSubTeamButton.addActionListener(e -> showCreateSubTeamDialog());
        buttonPanel.add(createSubTeamButton);

        // Button to create a Simple User
        JButton createUserButton = new JButton("Create Simple User");
        createUserButton.addActionListener(e -> showCreateUserDialog());
        buttonPanel.add(createUserButton);

        // Button to view tasks that need review
        JButton viewTasksButton = new JButton("View Tasks Needing Review");
        viewTasksButton.addActionListener(e -> showMyTasks());
        buttonPanel.add(viewTasksButton);

        // Button to request staff recruitment
        JButton requestStaffButton = new JButton("Request Staff Recruitment");
        requestStaffButton.addActionListener(e -> showRequestStaffDialog());
        buttonPanel.add(requestStaffButton);

        // Button to view recruitment request status
        JButton viewRequestStatusButton = new JButton("View Recruitment Request Status");
        viewRequestStatusButton.addActionListener(e -> showRecruitmentRequestStatus());
        buttonPanel.add(viewRequestStatusButton);

        // Button to view sub-teams
        JButton viewSubTeamsButton = new JButton("View Sub-Teams");
        viewSubTeamsButton.addActionListener(e -> controller.handleViewSubTeams());
        buttonPanel.add(viewSubTeamsButton);

        JButton requestFinancialButton = new JButton("Request Financial Approval");
        requestFinancialButton.addActionListener(e -> showFinancialRequestDialog());
        buttonPanel.add(requestFinancialButton);

        // Button to view financial request status
        JButton viewFinancialRequestsButton = new JButton("View Financial Requests");
        viewFinancialRequestsButton.addActionListener(e -> showFinancialRequestsStatus());
        buttonPanel.add(viewFinancialRequestsButton);

        // Button to log out
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> controller.handleLogout());
        buttonPanel.add(logoutButton);

        psmPanel.add(buttonPanel, BorderLayout.CENTER);

        // Text area to display task details and recruitment request status
        taskDetailsArea = new JTextArea(10, 50);
        taskDetailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taskDetailsArea);
        psmPanel.add(scrollPane, BorderLayout.SOUTH);
    }

    private void showCreateTaskDialog() {
        JTextField taskDescriptionField = new JTextField();
        JTextField budgetField = new JTextField();

        // Get sub-teams managed by this PSM
        String[] subTeams = controller.getSubTeamsForPSM();
        JComboBox<String> subTeamComboBox = new JComboBox<>(subTeams);

        Object[] message = {
                "Task Description:", taskDescriptionField,
                "Budget:", budgetField,
                "Assign to Sub-Team:", subTeamComboBox
        };

        int option = JOptionPane.showConfirmDialog(psmPanel, message, "Create Task for Sub-Team", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String taskDescription = taskDescriptionField.getText();
            double budget;

            try {
                budget = Double.parseDouble(budgetField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(psmPanel, "Invalid budget value. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedSubTeam = (String) subTeamComboBox.getSelectedItem();
            controller.handleCreateTaskForSubTeam(taskDescription, budget, selectedSubTeam);
        }
    }

    private void showCreateSubTeamDialog() {
        JTextField subTeamNameField = new JTextField();

        Object[] message = {
                "Sub-Team Name:", subTeamNameField
        };

        int option = JOptionPane.showConfirmDialog(psmPanel, message, "Create Sub-Team", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String subTeamName = subTeamNameField.getText();
            controller.handleCreateSubTeam(subTeamName); // Call the controller method
        }
    }

    private void showCreateUserDialog() {
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField departmentField = new JTextField();

        // Get sub-teams managed by this PSM
        String[] subTeams = controller.getSubTeamsForPSM();
        JComboBox<String> subTeamComboBox = new JComboBox<>(subTeams);

        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField,
                "Department:", departmentField,
                "Sub-Team:", subTeamComboBox
        };

        int option = JOptionPane.showConfirmDialog(psmPanel, message, "Create Simple User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String department = departmentField.getText();
            String subTeam = (String) subTeamComboBox.getSelectedItem();
            controller.handleCreateSimpleUser(username, password, department, subTeam); // Call the controller method
        }
    }

    private void showMyTasks() {
        List<Task> tasks = controller.getTasksCreatedByPSM(); // Get tasks created by this PSM
        List<Task> tasksNeedingReview = new ArrayList<>();

        // Filter tasks that need review
        for (Task task : tasks) {
            if (task.needsReview()) {
                tasksNeedingReview.add(task);
            }
        }

        if (tasksNeedingReview.isEmpty()) {
            taskDetailsArea.setText("No tasks needing review.");
        } else {
            StringBuilder taskInfo = new StringBuilder();
            for (Task task : tasksNeedingReview) {
                taskInfo.append(task.toString()).append("\n\n");
            }
            taskDetailsArea.setText(taskInfo.toString());
        }
    }

    private void showRequestStaffDialog() {
        JTextArea requestDetailsArea = new JTextArea(5, 20);
        Object[] message = {
                "Request Details:", new JScrollPane(requestDetailsArea)
        };

        int option = JOptionPane.showConfirmDialog(psmPanel, message, "Staff Recruitment Request", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String details = requestDetailsArea.getText();
            controller.handleStaffRecruitmentRequest(details);
        }
    }

    private void showRecruitmentRequestStatus() {
        List<StaffRecruitmentRequest> requests = controller.getStaffRecruitmentRequestsByPSM();

        if (requests.isEmpty()) {
            taskDetailsArea.setText("No staff recruitment requests.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (StaffRecruitmentRequest request : requests) {
            sb.append("Details:\n").append(request.getRequestDetails()).append("\n");
            sb.append("Status: ").append(request.getStatus()).append("\n\n");
        }
        taskDetailsArea.setText(sb.toString());
    }

    private void showFinancialRequestDialog() {
        JTextArea requestDetailsArea = new JTextArea(5, 20);
        Object[] message = {
                "Request Details:", new JScrollPane(requestDetailsArea)
        };

        int option = JOptionPane.showConfirmDialog(psmPanel, message, "Financial Request", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String details = requestDetailsArea.getText();
            controller.handleFinancialRequest(details);
        }
    }

    // Method to display the status of financial requests
    private void showFinancialRequestsStatus() {
        List<FinancialRequest> requests = controller.getFinancialRequestsByPSM();

        if (requests.isEmpty()) {
            taskDetailsArea.setText("No financial requests.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (FinancialRequest request : requests) {
            sb.append("Details:\n").append(request.getRequestDetails()).append("\n");
            sb.append("Status: ").append(request.getStatus()).append("\n\n");
        }
        taskDetailsArea.setText(sb.toString());
    }

    public JPanel getPanel() {
        return psmPanel;
    }

}
