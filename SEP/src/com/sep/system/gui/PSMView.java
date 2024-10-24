package com.sep.system.gui;

import com.sep.system.controller.SEPController;
import com.sep.system.tasks.Task;
import com.sep.system.user.SimpleUser;

import javax.swing.*;
import java.awt.*;
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
        psmPanel = new JPanel(new GridLayout(8, 1)); // Adjusted rows to accommodate new buttons

        JLabel welcomeLabel = new JLabel("Production Service Manager Menu", SwingConstants.CENTER);
        psmPanel.add(welcomeLabel);

        // Button to create a task for a sub-team
        JButton createTaskButton = new JButton("Create Task for Sub-Team");
        createTaskButton.addActionListener(e -> showCreateTaskDialog());
        psmPanel.add(createTaskButton);

        // Button to create a new sub-team
        JButton createSubTeamButton = new JButton("Create Sub-Team");
        createSubTeamButton.addActionListener(e -> showCreateSubTeamDialog());
        psmPanel.add(createSubTeamButton);

        // Button to create a Simple User
        JButton createUserButton = new JButton("Create Simple User");
        createUserButton.addActionListener(e -> showCreateUserDialog());
        psmPanel.add(createUserButton);

        // Button to view tasks created by this PSM
        JButton viewTasksButton = new JButton("View My Tasks");
        viewTasksButton.addActionListener(e -> showMyTasks());
        psmPanel.add(viewTasksButton);

        // Button to view sub-teams
        JButton viewSubTeamsButton = new JButton("View Sub-Teams");
        viewSubTeamsButton.addActionListener(e -> controller.handleViewSubTeams());
        psmPanel.add(viewSubTeamsButton);

        // Button to log out
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> controller.handleLogout());
        psmPanel.add(logoutButton);

        taskDetailsArea = new JTextArea(15, 50);
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

    // Show a dialog to create a new sub-team
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

    // Show a dialog to create a new Simple User
    private void showCreateUserDialog() {
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField departmentField = new JTextField();
        JTextField subTeamField = new JTextField();

        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField,
                "Department:", departmentField,
                "Sub-Team:", subTeamField
        };

        int option = JOptionPane.showConfirmDialog(psmPanel, message, "Create Simple User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String department = departmentField.getText();
            String subTeam = subTeamField.getText();
            controller.handleCreateSimpleUser(username, password, department, subTeam); // Call the controller method
        }
    }

    // Method to display all tasks created by this PSM
    private void showMyTasks() {
        List<Task> tasks = controller.getTasksCreatedByPSM(); // Get tasks created by this PSM
        if (tasks.isEmpty()) {
            taskDetailsArea.setText("No tasks created by you.");
        } else {
            StringBuilder taskInfo = new StringBuilder();
            for (Task task : tasks) {
                taskInfo.append(task.toString()).append("\n\n");
            }
            taskDetailsArea.setText(taskInfo.toString());
        }
    }

    public JPanel getPanel() {
        return psmPanel;
    }
}
