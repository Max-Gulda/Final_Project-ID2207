package com.sep.system.util;

import com.sep.system.auth.AuthenticationService;
import com.sep.system.user.ProductionServiceManager;
import com.sep.system.user.User;
import java.util.List;

public class SessionManager {
    private AuthenticationService authService;

    // Constructor initializes the system
    public SessionManager() {
        // Load user data
        List<User> users = DataManager.loadUsers();
        if (users == null) {
            users = AuthenticationService.initializeDefaultUsers(); // New method to initialize sample users
        }
        authService = new AuthenticationService(users);
        for (User U: users) {
            if(U instanceof ProductionServiceManager){
                ((ProductionServiceManager) U).setAuthService(authService);
            }
        }
    }

    // Get AuthenticationService instance
    public AuthenticationService getAuthService() {
        return authService;
    }

    // Save user data before the system exits
    public void saveOnExit() {
        DataManager.saveUsers(authService.getUsers());
    }
}