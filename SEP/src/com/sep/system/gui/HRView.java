// File: com/sep/system/gui/HRView.java
package com.sep.system.gui;

import com.sep.system.requests.StaffRecruitmentRequest;
import com.sep.system.controller.SEPController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HRView {
    private JPanel hrPanel;
    private SEPController controller;
    private JTextArea requestDetailsArea;

    public HRView(SEPController controller) {
        this.controller = controller;
        initHRPanel();
    }

    private void initHRPanel() {
        hrPanel = new JPanel(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel welcomeLabel = new JLabel("HR Manager Menu", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        hrPanel.add(welcomeLabel, BorderLayout.NORTH);

        JButton processRequestsButton = new JButton("Process Recruitment Requests");
        processRequestsButton.addActionListener(e -> showRecruitmentRequests());
        buttonPanel.add(processRequestsButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> controller.handleLogout());
        buttonPanel.add(logoutButton);

        hrPanel.add(buttonPanel, BorderLayout.CENTER);

        requestDetailsArea = new JTextArea(10, 50);
        requestDetailsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(requestDetailsArea);
        hrPanel.add(scrollPane, BorderLayout.SOUTH);
    }

    private void showRecruitmentRequests() {
        List<StaffRecruitmentRequest> requests = controller.getStaffRecruitmentRequests();

        if (requests.isEmpty()) {
            requestDetailsArea.setText("No staff recruitment requests.");
            return;
        }

        for (StaffRecruitmentRequest request : requests) {
            if ("PENDING".equals(request.getStatus())) {
                String message = "Request from: " + request.getRequester().getUsername() + "\n"
                        + "Details:\n" + request.getRequestDetails() + "\n\n"
                        + "Approve this request?";
                int option = JOptionPane.showConfirmDialog(hrPanel, message, "Staff Recruitment Request", JOptionPane.YES_NO_CANCEL_OPTION);

                if (option == JOptionPane.YES_OPTION) {
                    request.setStatus("APPROVED");
                    JOptionPane.showMessageDialog(hrPanel, "Request approved.");
                } else if (option == JOptionPane.NO_OPTION) {
                    request.setStatus("DISAPPROVED");
                    JOptionPane.showMessageDialog(hrPanel, "Request disapproved.");
                } else {
                    // Cancelled, do nothing
                }
            }
        }

        updateRequestDetailsArea();
    }

    private void updateRequestDetailsArea() {
        List<StaffRecruitmentRequest> requests = controller.getStaffRecruitmentRequests();
        StringBuilder sb = new StringBuilder();
        for (StaffRecruitmentRequest request : requests) {
            sb.append("Request from: ").append(request.getRequester().getUsername()).append("\n");
            sb.append("Details:\n").append(request.getRequestDetails()).append("\n");
            sb.append("Status: ").append(request.getStatus()).append("\n\n");
        }
        requestDetailsArea.setText(sb.toString());
    }

    public JPanel getPanel() {
        return hrPanel;
    }
}
