package com.sep.system.tests;

import com.sep.system.requests.StaffRecruitmentRequest;
import com.sep.system.user.ProductionServiceManager;

public class StaffRecruitmentRequestTest {

    public static void main(String[] args) {
        testStaffRecruitmentRequest();
    }

    public static void testStaffRecruitmentRequest() {
        // Step 1: Setup a mock ProductionServiceManager for testing
        ProductionServiceManager psm = new ProductionServiceManager("psmUser", "psmPass");

        // Step 2: Create a StaffRecruitmentRequest with initial details
        String initialDetails = "Recruit staff for new project";
        StaffRecruitmentRequest staffRequest = new StaffRecruitmentRequest(initialDetails, psm);

        // Step 3: Validate initial state of StaffRecruitmentRequest
        assert initialDetails.equals(staffRequest.getRequestDetails()) : "Request details do not match";
        assert "PENDING".equals(staffRequest.getStatus()) : "Initial status should be PENDING";
        assert psm.equals(staffRequest.getRequester()) : "Requester should be the assigned PSM";

        // Step 4: Update and verify the status of the StaffRecruitmentRequest
        staffRequest.setStatus("APPROVED");
        assert "APPROVED".equals(staffRequest.getStatus()) : "Status should be updated to APPROVED";

        System.out.println("StaffRecruitmentRequest tests passed successfully.");
    }
}

