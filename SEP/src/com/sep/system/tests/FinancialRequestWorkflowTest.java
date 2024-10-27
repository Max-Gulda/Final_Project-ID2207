package com.sep.system.tests;

import com.sep.system.auth.AuthenticationService;
import com.sep.system.requests.FinancialRequest;
import com.sep.system.user.FinancialManager;
import com.sep.system.user.ProductionServiceManager;
import com.sep.system.user.User;

import java.util.List;

public class FinancialRequestWorkflowTest {

    public static void main(String[] args) {
        // Step 0: Initialize the AuthenticationService with default users
        List<User> defaultUsers = AuthenticationService.initializeDefaultUsers();
        AuthenticationService authService = new AuthenticationService(defaultUsers);

        // Retrieve a ProductionServiceManager (PSM) and a FinancialManager (FM) from the default users
        ProductionServiceManager psm = (ProductionServiceManager) defaultUsers.stream()
                .filter(user -> user instanceof ProductionServiceManager)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Production Service Manager not found in default users"));

        FinancialManager fm = (FinancialManager) defaultUsers.stream()
                .filter(user -> user instanceof FinancialManager)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Financial Manager not found in default users"));

        // Assign the AuthenticationService to the PSM
        psm.setAuthService(authService);

        // Step 1: PSM creates a financial request
        String financialRequestDetails = "Requesting funds for new equipment";
        psm.createFinancialRequest(financialRequestDetails);

        // Ensure the request was added to PSM's financial request list
        List<FinancialRequest> psmRequests = psm.getFinancialRequests();
        assert !psmRequests.isEmpty() : "PSM should have created a financial request";
        FinancialRequest request = psmRequests.getFirst();
        assert "PENDING".equals(request.getStatus()) : "Request status should be PENDING initially";

        // Step 2: FM receives and processes the financial request
        fm.receiveFinancialRequest(request);

        // Ensure FM received the request
        List<FinancialRequest> fmReceivedRequests = fm.getReceivedFinancialRequests();
        assert fmReceivedRequests.contains(request) : "FM should have received the financial request";

        // Step 3: FM approves the request
        fm.processFinancialRequest(request, true);

        // Validate that the request's status is updated to "APPROVED"
        assert "APPROVED".equals(request.getStatus()) : "Request status should be APPROVED after FM's approval";

        // Step 4: PSM updates the status of the financial request in its own list
        psm.setStatusFinancialRequest(request);

        // Validate that the PSM's version of the request is also updated to "APPROVED"
        assert "APPROVED".equals(psm.getFinancialRequests().getFirst().getStatus()) :
                "PSM's request status should reflect the APPROVED status from FM";

        System.out.println("Financial request workflow test passed successfully.");
    }
}

