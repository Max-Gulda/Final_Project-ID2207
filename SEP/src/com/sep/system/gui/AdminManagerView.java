package com.sep.system.gui;

import javax.swing.*;
import java.awt.*;
import com.sep.system.controller.SEPController;
import com.sep.system.user.Role;

public class AdminManagerView {
    private JPanel adminManagerPanel;
    private SEPController controller;

    public AdminManagerView(SEPController controller) {
        this.controller = controller;
        initAdminManagerPanel();
    }

    private void initAdminManagerPanel() {
        adminManagerPanel = new JPanel(new GridLayout(6, 1));

        JLabel welcomeLabel = new JLabel("Admin Manager Menu", SwingConstants.CENTER);
        adminManagerPanel.add(welcomeLabel);

        // Button to create a new user
        JButton createUserButton = new JButton("Create New User");
        createUserButton.addActionListener(e -> showCreateUserDialog());
        adminManagerPanel.add(createUserButton);

        // Button to process event requests
        JButton processEventButton = new JButton("Process Event Requests");
        processEventButton.addActionListener(e -> controller.handleProcessEventRequests());
        adminManagerPanel.add(processEventButton);

        // Button to finalize event requests
        JButton finalizeEventRequestsButton = new JButton("Finalize Event Requests");
        finalizeEventRequestsButton.addActionListener(e -> controller.handleFinalizeEventRequests());
        adminManagerPanel.add(finalizeEventRequestsButton);

        // Button to log out
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> controller.handleLogout());
        adminManagerPanel.add(logoutButton);
    }

    // Show a dialog for creating a new user and pass the input to the controller
    private void showCreateUserDialog() {
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JPasswordField();
        String[] roles = {"CSO", "Admin Manager", "SCSO", "HR Manager", "Financial Manager", "Production Service Manager"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);

        Object[] message = {
                "Username:", usernameField,
                "Password:", passwordField,
                "Role:", roleComboBox
        };

        int option = JOptionPane.showConfirmDialog(adminManagerPanel, message, "Create New User", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            String role = roleComboBox.getSelectedItem().toString();

            // Call controller method to create the user with input values
            controller.handleCreateUser(username, password, Role.valueOf(role.toUpperCase().replace(" ", "_")));
        }
    }

    public JPanel getPanel() {
        return adminManagerPanel;
    }
}
