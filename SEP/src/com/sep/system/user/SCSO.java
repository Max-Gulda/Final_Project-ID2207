package com.sep.system.user;

import com.sep.system.requests.EventRequest;

public class SCSO extends User {

    public SCSO(String username, String password) {
        super(username, password, Role.SCSO);
    }

    @Override
    public boolean login(String username, String password) {
        return this.getUsername().equals(username) && this.getPassword().equals(password);
    }

    public void receiveEventRequest(EventRequest request) {
        System.out.println("SCSO received event request: " + request.getEventName());
    }

    public void approveOrDisapproveEventRequest(EventRequest request, boolean approve, FinancialManager fm) {
        if ("PENDING".equals(request.getStatus())) {
            if (approve) {
                request.setStatus("APPROVED");
                fm.receiveEventRequest(request);
                System.out.println("Event request approved by SCSO: " + request.getEventName());
            } else {
                request.setStatus("DISAPPROVED");
                System.out.println("Event request disapproved by SCSO: " + request.getEventName());
            }
        } else {
            System.out.println("Cannot process request. Request must be in PENDING state.");
        }
    }
}
