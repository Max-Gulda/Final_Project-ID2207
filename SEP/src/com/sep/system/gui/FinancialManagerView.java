// File: com/sep/system/gui/FinancialManagerView.java
package com.sep.system.gui;

import com.sep.system.controller.SEPController;

import javax.swing.*;
import java.awt.*;

public class FinancialManagerView {
    private JPanel fmPanel;
    private SEPController controller;
    private JTextArea requestDetailsArea;

    public FinancialManagerView(SEPController controller) {
        this.controller = controller;
        initFMPanel();
    }

    private void initFMPanel() {
        fmPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("Financial Manager Menu", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        fmPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Button to process event requests
        JButton processRequestsButton = new JButton("Process Event Requests");
        processRequestsButton.addActionListener(e -> controller.handleProcessEventRequests());
        buttonPanel.add(processRequestsButton);

        // Button to log out
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> controller.handleLogout());
        buttonPanel.add(logoutButton);

        fmPanel.add(buttonPanel, BorderLayout.CENTER);

        // Text area to display request details
        requestDetailsArea = new JTextArea(10, 50);
        requestDetailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(requestDetailsArea);
        fmPanel.add(scrollPane, BorderLayout.SOUTH);
    }

    public JPanel getPanel() {
        return fmPanel;
    }
}
