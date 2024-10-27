package com.sep.system.tests;

import com.sep.system.requests.EventRequest;
import com.sep.system.user.AdminManager;
import com.sep.system.user.CSO;
import com.sep.system.user.FinancialManager;
import com.sep.system.user.SCSO;

public class EventWorkflowTest {

    public static void main(String[] args) {
        // Initialize the users involved in the workflow
        CSO cso = new CSO("csoUser", "csoPass");
        SCSO scso = new SCSO("scsoUser", "scsoPass");
        FinancialManager fm = new FinancialManager("fmUser", "fmPass");
        AdminManager am = new AdminManager("amUser", "amPass");

        // Step 1: CSO creates an event request
        cso.createEventRequest("Annual Conference", "Global Corp", "Annual corporate conference event", 100000.0);
        EventRequest request = cso.getEventRequests().get(0);
        assert "PENDING".equals(request.getStatus()) : "Initial status should be PENDING";

        // Step 2: CSO sends the request to SCSO
        cso.sendEventRequestToSCSO(request, scso);
        assert cso.getEventRequests().contains(request) : "Request should still be in CSO's list";

        // Step 3: SCSO approves the request and sends it to Financial Manager
        scso.approveOrDisapproveEventRequest(request, true, fm);
        assert "APPROVED".equals(request.getStatus()) : "Status should be APPROVED after SCSO approval";

        // Step 4: Financial Manager adds a budget comment and sends it to Admin Manager
        fm.addBudgetComment(request, "Budget approved within limits", am);
        assert "Budget approved within limits".equals(request.getBudgetComment()) : "Budget comment should be set by Financial Manager";

        // Step 5: Admin Manager finalizes the request
        am.finalizeEventRequest(request, true);
        assert request.isFinalized() : "Request should be finalized after Admin Manager's approval";
        assert "APPROVED".equals(request.getStatus()) : "Final status should be APPROVED";

        System.out.println("Workflow test passed successfully.");
    }
}

