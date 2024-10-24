package com.sep.system.user;

public abstract class User {
    // Fields
    private String username;
    private String password;
    private Role role;

    // Constructor
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // Abstract Method for Login
    public abstract boolean login(String username, String password);

    // Method to Display User Info
    public void displayUserInfo() {
        System.out.println("Username: " + username);
        System.out.println("Role: " + role);
    }
}