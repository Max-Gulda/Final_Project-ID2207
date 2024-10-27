// File: com/sep/system/requests/FinancialRequest.java
package com.sep.system.requests;

import com.sep.system.user.ProductionServiceManager;

public class FinancialRequest {
    private String requestDetails;
    private String status; // PENDING, APPROVED, DISAPPROVED
    private ProductionServiceManager requester;

    public FinancialRequest(String requestDetails, ProductionServiceManager requester) {
        this.requestDetails = requestDetails;
        this.status = "PENDING";
        this.requester = requester;
    }

    // Getters and setters
    public String getRequestDetails() {
        return requestDetails;
    }

    public String getStatus() {
        return status;
    }

    public ProductionServiceManager getRequester() {
        return requester;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        FinancialRequest that = (FinancialRequest) obj;
        return requestDetails.equals(that.requestDetails) && requester.equals(that.requester);
    }

}
