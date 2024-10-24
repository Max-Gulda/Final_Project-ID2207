package com.sep.system.gui;

import com.sep.system.auth.AuthenticationService;
import com.sep.system.controller.SEPController;
import com.sep.system.util.DataManager;

import javax.swing.*;
import java.awt.*;

public class SEPSystemGUI {
    private AuthenticationService authService;
    private DataManager dataManager;
    private JFrame frame;
    private JPanel cards;  // The CardLayout container
    private CardLayout cardLayout;
    private SEPController controller;

    public SEPSystemGUI(AuthenticationService authService, DataManager dataManager) {
        this.authService = authService;
        this.dataManager = dataManager;
    }

    public void createAndShowGUI() {
        // Initialize the frame and the CardLayout
        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("SEP System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 500);

            cardLayout = new CardLayout();
            cards = new JPanel(cardLayout);

            // Initialize the controller and pass required objects
            controller = new SEPController(authService, dataManager, frame, cardLayout, cards);

            // Add different views to the card layout
            addViewsToCardLayout();

            // Display the login view first
            cardLayout.show(cards, "Login");

            frame.add(cards);
            frame.setVisible(true);
        });
    }

    private void addViewsToCardLayout() {
        // Add Login view
        LoginView loginView = new LoginView(controller);
        cards.add(loginView.getPanel(), "Login");

        // Add AdminManager view
        AdminManagerView adminManagerView = new AdminManagerView(controller);
        cards.add(adminManagerView.getPanel(), "AdminManager");

        // Add PSM view
        PSMView psmView = new PSMView(controller);
        cards.add(psmView.getPanel(), "PSM");

        // Add SCSO view
        SCSOView scsoView = new SCSOView(controller);
        cards.add(scsoView.getPanel(), "SCSO");

        // Add SimpleUser view
        SimpleUserView simpleUserView = new SimpleUserView(controller);
        cards.add(simpleUserView.getPanel(), "SimpleUser");

        // Add more views as needed based on roles (e.g., CSO, FinancialManager, etc.)
    }
}
