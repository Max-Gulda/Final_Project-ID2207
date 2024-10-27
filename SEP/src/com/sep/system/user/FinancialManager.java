package com.sep.system.user;

import com.sep.system.requests.EventRequest;
import com.sep.system.requests.FinancialRequest;

import java.util.ArrayList;
import java.util.List;

public class FinancialManager extends User {

    private List<FinancialRequest> receivedFinancialRequests;
    public FinancialManager(String username, String password) {
        super(username, password, Role.FINANCIAL_MANAGER);
        this.receivedFinancialRequests = new ArrayList<>();
    }

    @Override
    public boolean login(String username, String password) {
        return this.getUsername().equals(username) && this.getPassword().equals(password);
    }

    public void receiveEventRequest(EventRequest request) {
        System.out.println("Financial Manager received event request: " + request.getEventName());
    }

    public void addBudgetComment(EventRequest request, String comment, AdminManager am) {
        if ("APPROVED".equals(request.getStatus())) {
            request.setBudgetComment(comment);
            am.receiveEventRequest(request);
            System.out.println("Budget comment added and request sent to Admin Manager: " + request.getEventName());
        } else {
            System.out.println("Cannot add budget comment. Request must be APPROVED by SCSO first.");
        }
    }

    public void receiveFinancialRequest(FinancialRequest request) {
        receivedFinancialRequests.add(request);
        System.out.println("Financial Manager received financial request from PSM: " + request.getRequester().getUsername());
    }

    // Method to get all received financial requests
    public List<FinancialRequest> getReceivedFinancialRequests() {
        return receivedFinancialRequests;
    }

    // Method to process a financial request
    public void processFinancialRequest(FinancialRequest request, boolean approve) {
        if ("PENDING".equals(request.getStatus())) {
            if (approve) {
                request.setStatus("APPROVED");
                System.out.println("Financial request approved: " + request.getRequestDetails());
            } else {
                request.setStatus("DISAPPROVED");
                System.out.println("Financial request disapproved: " + request.getRequestDetails());
            }
        } else {
            System.out.println("Cannot process request. Request must be in PENDING state.");
        }
    }
}
