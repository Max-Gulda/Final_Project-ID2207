package com.sep.system.gui;

import javax.swing.*;
import java.awt.*;
import com.sep.system.controller.SEPController;
import com.sep.system.tasks.Task;
import com.sep.system.user.SimpleUser;
import com.sep.system.user.User;

import java.util.List;

public class SimpleUserView {
    private JPanel simpleUserPanel;
    private SEPController controller;
    private JList<Task> taskList; // JList to display tasks
    private DefaultListModel<Task> taskListModel; // Model for the JList

    public SimpleUserView(SEPController controller) {
        this.controller = controller;
        initSimpleUserPanel();
    }

    private void initSimpleUserPanel() {
        simpleUserPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Add some padding

        // Welcome label
        JLabel welcomeLabel = new JLabel("Simple User Menu", SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; // Span across columns
        simpleUserPanel.add(welcomeLabel, gbc);

        // Button to view tasks assigned to this user
        JButton viewTasksButton = new JButton("View My Tasks");
        viewTasksButton.addActionListener(e -> {
            // Fetch and display the tasks
            List<Task> tasks = controller.getAssignedTasksForUser();
            updateTaskList(tasks); // Update the task list with the fetched tasks
        });
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; // Reset grid width
        simpleUserPanel.add(viewTasksButton, gbc);

        // JList for displaying tasks
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        JScrollPane taskScrollPane = new JScrollPane(taskList);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; // Span across columns
        simpleUserPanel.add(taskScrollPane, gbc);

        // Label and TextField for budget comment
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; // Reset grid width
        simpleUserPanel.add(new JLabel("Budget Comment:"), gbc);
        JTextField budgetCommentField = new JTextField();
        gbc.gridx = 1;
        simpleUserPanel.add(budgetCommentField, gbc);

        // Button to add budget comment
        JButton addCommentButton = new JButton("Add Budget Comment");
        addCommentButton.addActionListener(e -> {
            Task selectedTask = taskList.getSelectedValue(); // Get the selected task
            String comment = budgetCommentField.getText();
            if (selectedTask != null && !comment.trim().isEmpty()) {
                controller.handleAddBudgetComment(selectedTask, comment);
                budgetCommentField.setText(""); // Clear the input field
            } else {
                JOptionPane.showMessageDialog(simpleUserPanel, "Please select a task and enter a comment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; // Reset grid width to span
        simpleUserPanel.add(addCommentButton, gbc);

        // Label and TextField for plan
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 1; // Reset grid width
        simpleUserPanel.add(new JLabel("Plan:"), gbc);
        JTextField planField = new JTextField();
        gbc.gridx = 1;
        simpleUserPanel.add(planField, gbc);

        // Button to add plan
        JButton addPlanButton = new JButton("Add Plan");
        addPlanButton.addActionListener(e -> {
            Task selectedTask = taskList.getSelectedValue(); // Get the selected task
            String plan = planField.getText();
            if (selectedTask != null && !plan.trim().isEmpty()) {
                SimpleUser simpleUser = (SimpleUser) controller.getLoggedInUser(); // Retrieve the logged-in user
                simpleUser.addPlanToTask(selectedTask, plan); // Call the addPlanToTask method
                planField.setText(""); // Clear the input field
            } else {
                JOptionPane.showMessageDialog(simpleUserPanel, "Please select a task and enter a plan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; // Reset grid width to span
        simpleUserPanel.add(addPlanButton, gbc);

        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> controller.handleLogout());
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; // Reset grid width to span
        simpleUserPanel.add(logoutButton, gbc);
    }

    public JPanel getPanel() {
        return simpleUserPanel;
    }

    // Method to update the task list with new tasks
    private void updateTaskList(List<Task> tasks) {
        taskListModel.clear(); // Clear existing tasks in the model
        for (Task task : tasks) {
            taskListModel.addElement(task); // Add each task to the model
        }
    }
}
