package com.sep.system.tests;

import com.sep.system.requests.FinancialRequest;
import com.sep.system.user.ProductionServiceManager;

public class FinancialRequestTest {

    public static void main(String[] args) {
        testFinancialRequest();
    }

    public static void testFinancialRequest() {
        // Step 1: Setup a mock ProductionServiceManager for testing
        ProductionServiceManager psm = new ProductionServiceManager("psmUser", "psmPass");

        // Step 2: Create a FinancialRequest with initial details
        String initialDetails = "Request funding for new project";
        FinancialRequest financialRequest = new FinancialRequest(initialDetails, psm);

        // Step 3: Validate initial state of FinancialRequest
        assert initialDetails.equals(financialRequest.getRequestDetails()) : "Request details do not match";
        assert "PENDING".equals(financialRequest.getStatus()) : "Initial status should be PENDING";
        assert psm.equals(financialRequest.getRequester()) : "Requester should be the assigned PSM";

        // Step 4: Update and verify the status of the FinancialRequest
        financialRequest.setStatus("APPROVED");
        assert "APPROVED".equals(financialRequest.getStatus()) : "Status should be updated to APPROVED";

        // Step 5: Test the equality method
        FinancialRequest sameRequest = new FinancialRequest(initialDetails, psm);
        assert financialRequest.equals(sameRequest) : "Requests with the same details and requester should be equal";

        FinancialRequest differentRequest = new FinancialRequest("Different details", psm);
        assert !financialRequest.equals(differentRequest) : "Requests with different details should not be equal";

        System.out.println("FinancialRequest tests passed successfully.");
    }
}
