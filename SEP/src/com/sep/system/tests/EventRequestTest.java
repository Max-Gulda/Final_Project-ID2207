package com.sep.system.tests;

import com.sep.system.requests.EventRequest;

public class EventRequestTest {

    public static void main(String[] args) {
        testConstructorInitializesFieldsCorrectly();
        testSetStatus();
        testSetBudgetComment();
        testSetFinalized();
        testToString();
        System.out.println("All tests passed.");
    }

    public static void testConstructorInitializesFieldsCorrectly() {
        EventRequest eventRequest = new EventRequest("Annual Gala", "ACME Corp", "Corporate gala event", 50000.0);

        assert "Annual Gala".equals(eventRequest.getEventName()) : "Event name initialization failed";
        assert "ACME Corp".equals(eventRequest.getClientName()) : "Client name initialization failed";
        assert "Corporate gala event".equals(eventRequest.getDescription()) : "Description initialization failed";
        assert eventRequest.getBudget() == 50000.0 : "Budget initialization failed";
        assert "PENDING".equals(eventRequest.getStatus()) : "Default status should be PENDING";
        assert "".equals(eventRequest.getBudgetComment()) : "Default budget comment should be empty";
        assert !eventRequest.isFinalized() : "Default finalized status should be false";
    }

    public static void testSetStatus() {
        EventRequest eventRequest = new EventRequest("Annual Gala", "ACME Corp", "Corporate gala event", 50000.0);

        eventRequest.setStatus("APPROVED");
        assert "APPROVED".equals(eventRequest.getStatus()) : "Status should be APPROVED after setting it";

        eventRequest.setStatus("DISAPPROVED");
        assert "DISAPPROVED".equals(eventRequest.getStatus()) : "Status should be DISAPPROVED after setting it";
    }

    public static void testSetBudgetComment() {
        EventRequest eventRequest = new EventRequest("Annual Gala", "ACME Corp", "Corporate gala event", 50000.0);

        eventRequest.setBudgetComment("Budget is acceptable");
        assert "Budget is acceptable".equals(eventRequest.getBudgetComment()) : "Budget comment did not update correctly";
    }

    public static void testSetFinalized() {
        EventRequest eventRequest = new EventRequest("Annual Gala", "ACME Corp", "Corporate gala event", 50000.0);

        eventRequest.setFinalized(true);
        assert eventRequest.isFinalized() : "Finalized status should be true after setting it";

        eventRequest.setFinalized(false);
        assert !eventRequest.isFinalized() : "Finalized status should be false after setting it back";
    }

    public static void testToString() {
        EventRequest eventRequest = new EventRequest("Annual Gala", "ACME Corp", "Corporate gala event", 50000.0);
        String expected = "EventRequest{" +
                "eventName='Annual Gala'" +
                ", clientName='ACME Corp'" +
                ", description='Corporate gala event'" +
                ", budget=50000.0" +
                ", status='PENDING'" +
                ", budgetComment=''" +
                ", finalized=false" +
                '}';

        assert expected.equals(eventRequest.toString()) : "toString method did not return expected result";
    }
}

