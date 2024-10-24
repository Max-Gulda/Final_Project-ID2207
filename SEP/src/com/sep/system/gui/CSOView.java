package com.sep.system.gui;

import javax.swing.*;
import java.awt.*;
import com.sep.system.controller.SEPController;

public class CSOView {
    private JPanel csoPanel;
    private SEPController controller;

    public CSOView(SEPController controller) {
        this.controller = controller;
        initCSOPanel();
    }

    private void initCSOPanel() {
        csoPanel = new JPanel(new GridLayout(4, 1));

        JLabel welcomeLabel = new JLabel("CSO Menu", SwingConstants.CENTER);
        csoPanel.add(welcomeLabel);

        // Button to create a new event request
        JButton createEventRequestButton = new JButton("Create Event Request");
        createEventRequestButton.addActionListener(e -> showCreateEventRequestDialog());
        csoPanel.add(createEventRequestButton);

        // Button to view existing event requests created by the CSO
        JButton viewEventRequestsButton = new JButton("View My Event Requests");
        viewEventRequestsButton.addActionListener(e -> controller.handleViewMyEventRequests());
        csoPanel.add(viewEventRequestsButton);

        // Button to log out
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> controller.handleLogout());
        csoPanel.add(logoutButton);
    }

    // Show a dialog for creating a new event request and pass the input to the controller
    private void showCreateEventRequestDialog() {
        JTextField eventNameField = new JTextField();
        JTextField clientNameField = new JTextField();
        JTextField descriptionField = new JTextField();
        JTextField budgetField = new JTextField();

        Object[] message = {
                "Event Name:", eventNameField,
                "Client Name:", clientNameField,
                "Description:", descriptionField,
                "Budget:", budgetField
        };

        int option = JOptionPane.showConfirmDialog(csoPanel, message, "Create Event Request", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String eventName = eventNameField.getText();
            String clientName = clientNameField.getText();
            String description = descriptionField.getText();
            double budget;

            try {
                budget = Double.parseDouble(budgetField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(csoPanel, "Invalid budget value. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Call controller method to create the event request
            controller.handleCreateEventRequest(eventName, clientName, description, budget);
        }
    }

    public JPanel getPanel() {
        return csoPanel;
    }
}
