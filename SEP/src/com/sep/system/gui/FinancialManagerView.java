// File: com/sep/system/gui/FinancialManagerView.java
package com.sep.system.gui;

import com.sep.system.controller.SEPController;
import com.sep.system.requests.FinancialRequest;

import javax.swing.*;
import java.awt.*;
import java.util.List;


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

        JButton processFinancialRequestsButton = new JButton("Process Financial Requests");
        processFinancialRequestsButton.addActionListener(e -> showFinancialRequests());
        buttonPanel.add(processFinancialRequestsButton);

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

    private void showFinancialRequests() {
        List<FinancialRequest> requests = controller.getFinancialRequestsForFM();

        if (requests.isEmpty()) {
            requestDetailsArea.setText("No financial requests to process.");
            return;
        }

        for (FinancialRequest request : requests) {
            if ("PENDING".equals(request.getStatus())) {
                String message = "Request from: " + request.getRequester().getUsername() + "\n"
                        + "Details:\n" + request.getRequestDetails() + "\n\n"
                        + "Approve this financial request?";
                int option = JOptionPane.showConfirmDialog(fmPanel, message, "Financial Request", JOptionPane.YES_NO_CANCEL_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    controller.handleProcessFinancialRequest(request, true);
                } else if (option == JOptionPane.NO_OPTION) {
                    controller.handleProcessFinancialRequest(request, false);
                }
            }
        }

        updateFinancialRequestDetailsArea();
    }

    private void updateFinancialRequestDetailsArea() {
        List<FinancialRequest> requests = controller.getFinancialRequestsForFM();
        StringBuilder sb = new StringBuilder();
        for (FinancialRequest request : requests) {
            sb.append("Request from: ").append(request.getRequester().getUsername()).append("\n");
            sb.append("Details:\n").append(request.getRequestDetails()).append("\n");
            sb.append("Status: ").append(request.getStatus()).append("\n\n");
        }
        requestDetailsArea.setText(sb.toString());
    }

    public JPanel getPanel() {
        return fmPanel;
    }
}
