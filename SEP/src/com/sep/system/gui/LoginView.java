package com.sep.system.gui;

import javax.swing.*;
import java.awt.*;
import com.sep.system.controller.SEPController;

public class LoginView {
    private JPanel loginPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private SEPController controller;

    public LoginView(SEPController controller) {
        this.controller = controller;
        initLoginPanel();
    }

    private void initLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the SEP System!", SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(welcomeLabel, gbc);

        // Username label and field
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);

        // Password label and field
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        loginPanel.add(loginButton, gbc);

        // Exit button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> controller.exitSystem());
        gbc.gridy = 4;
        loginPanel.add(exitButton, gbc);
    }

    // Handle login button click
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(loginPanel, "Please enter both username and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
        } else {
            controller.handleLogin(username, password);  // Pass the input to the controller
        }
    }

    public JPanel getPanel() {
        return loginPanel;
    }
}
