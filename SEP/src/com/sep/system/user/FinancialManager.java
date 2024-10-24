package com.sep.system.user;

import com.sep.system.requests.EventRequest;

public class FinancialManager extends User {

    public FinancialManager(String username, String password) {
        super(username, password, Role.FINANCIAL_MANAGER);
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
}
