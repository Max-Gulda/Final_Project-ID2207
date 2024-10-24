package com.sep.system.user;

import com.sep.system.requests.EventRequest;

public class AdminManager extends User {

    public AdminManager(String username, String password) {
        super(username, password, Role.ADMIN_MANAGER);
    }

    @Override
    public boolean login(String username, String password) {
        return this.getUsername().equals(username) && this.getPassword().equals(password);
    }

    public void receiveEventRequest(EventRequest request) {
        System.out.println("Admin Manager received event request: " + request.getEventName());
    }

    public void finalizeEventRequest(EventRequest request, boolean approve) {
        if ("APPROVED".equals(request.getStatus()) && request.getBudgetComment() != null && !request.getBudgetComment().isEmpty()) {
            if (approve) {
                request.setFinalized(true);
                request.setStatus("APPROVED");
                System.out.println("Event request approved by Admin Manager: " + request.getEventName());
            } else {
                request.setStatus("DISAPPROVED");
                System.out.println("Event request disapproved by Admin Manager: " + request.getEventName());
            }
        } else {
            System.out.println("Cannot finalize request. Request must be APPROVED by SCSO and have a budget comment.");
        }
    }
}
