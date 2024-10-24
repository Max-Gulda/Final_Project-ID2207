package com.sep.system.user;

import com.sep.system.requests.EventRequest;

import java.util.ArrayList;
import java.util.List;

public class CSO extends User {
    private List<EventRequest> eventRequests;

    public CSO(String username, String password) {
        super(username, password, Role.CSO);
        this.eventRequests = new ArrayList<>();
    }

    @Override
    public boolean login(String username, String password) {
        return this.getUsername().equals(username) && this.getPassword().equals(password);
    }

    public void createEventRequest(String eventName, String clientName, String description, double budget) {
        EventRequest newRequest = new EventRequest(eventName, clientName, description, budget);
        eventRequests.add(newRequest);
        System.out.println("Created event request: " + newRequest);
    }

    public void sendEventRequestToSCSO(EventRequest request, SCSO scso) {
        if (eventRequests.contains(request) && "PENDING".equals(request.getStatus())) {
            scso.receiveEventRequest(request);
            System.out.println("Event request sent to SCSO: " + request.getEventName());
        } else {
            System.out.println("Cannot send request. Request must be in PENDING state.");
        }
    }

    public List<EventRequest> getEventRequests() {
        return eventRequests;
    }
}
