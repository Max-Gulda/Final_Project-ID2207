package com.sep.system.tests;

import com.sep.system.auth.AuthenticationService;
import com.sep.system.user.*;

import java.util.List;

public class RecruitmentWorkflowTest {

    public static void main(String[] args) {
        // Step 0: Initialize AuthenticationService with default users
        List<User> defaultUsers = AuthenticationService.initializeDefaultUsers();
        AuthenticationService authService = new AuthenticationService(defaultUsers);

        // Retrieve an HRManager and a ProductionServiceManager (psm) from the default users
        HRManager hrManager = (HRManager) defaultUsers.stream()
                .filter(user -> user instanceof HRManager)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("HR Manager not found in default users"));

        ProductionServiceManager psm = (ProductionServiceManager) defaultUsers.stream()
                .filter(user -> user instanceof ProductionServiceManager)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("PSM not found in default users"));

        // Assign the authService to the PSM to allow user creation
        psm.setAuthService(authService);

        // Step 1: PSM creates a new sub-team
        String subTeamName = "DevTeam";
        psm.createSubTeam(subTeamName);
        assert psm.getSubTeams().containsKey(subTeamName) : "Sub-team should be created";

        // Step 2: PSM requests HR to recruit a new staff member for the sub-team
        System.out.println("Requesting HR to recruit staff for the sub-team.");

        // Step 3: HR creates and adds a SimpleUser to the specified sub-team
        String newUsername = "newDev";
        String newPassword = "devPass";
        String department = "Development";
        hrManager.login("hr", "hr");  // Simulate HR login
        SimpleUser newUser = authService.createSimpleUser(newUsername, newPassword, department, subTeamName, psm);

        // Add the newly recruited user to the sub-team
        psm.addMemberToSubTeam(newUser, subTeamName);

        // Validate the new user is added to the sub-team
        SubTeam devTeam = new SubTeam(subTeamName, psm);
        devTeam.addMember(newUser);
        assert devTeam.getMembers().contains(newUser) : "New user should be added to the sub-team";

        // Step 4: Validate if the sub-team in PSM contains the new recruit
        assert psm.getSubTeams().get(subTeamName).contains(newUser) : "Sub-team should contain the new recruit";

        System.out.println("Staff recruitment workflow test passed successfully.");
    }
}

