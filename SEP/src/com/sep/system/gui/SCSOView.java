package com.sep.system.gui;

import javax.swing.*;
import java.awt.*;
import com.sep.system.controller.SEPController;

public class SCSOView {
    private JPanel scsoPanel;
    private SEPController controller;

    public SCSOView(SEPController controller) {
        this.controller = controller;
        initSCSOPanel();
    }

    private void initSCSOPanel() {
        scsoPanel = new JPanel(new GridLayout(3, 1));

        JLabel welcomeLabel = new JLabel("Senior Client Service Officer Menu", SwingConstants.CENTER);
        scsoPanel.add(welcomeLabel);

        // Button to process event requests
        JButton processEventRequestsButton = new JButton("Process Event Requests");
        processEventRequestsButton.addActionListener(e -> controller.handleProcessEventRequests());
        scsoPanel.add(processEventRequestsButton);

        // Button to log out
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> controller.handleLogout());
        scsoPanel.add(logoutButton);
    }

    public JPanel getPanel() {
        return scsoPanel;
    }
}
