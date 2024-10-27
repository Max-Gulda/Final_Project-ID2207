package com.sep.system.user;

public class HRManager extends User {

    // Constructor
    public HRManager(String username, String password) {
        super(username, password, Role.HR_MANAGER);
    }

    // Implement the login method
    @Override
    public boolean login(String username, String password) {
        // Example logic for login validation (you would replace this with your actual logic)
        return this.getUsername().equals(username) && this.getPassword().equals(password);
    }

}