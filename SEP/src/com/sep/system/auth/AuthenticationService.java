package com.sep.system.auth;

import com.sep.system.user.*;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationService {
    private List<User> users;

    public AuthenticationService(List<User> users) {
        this.users = users;
    }

    public static List<User> initializeDefaultUsers() {
        List<User> users = new ArrayList<>();
        users.add(new CSO("cso", "cso"));
        users.add(new SCSO("scso", "scso"));
        users.add(new AdminManager("am", "am"));
        users.add(new HRManager("hr", "hr"));
        users.add(new FinancialManager("fm", "fm"));
        users.add(new ProductionServiceManager("sm", "sm"));
        users.add(new ProductionServiceManager("pm", "pm"));
        return users;
    }

    public void createUser(String username, String password, Role role) {
        User newUser = null;
        switch (role) {
            case CSO:
                newUser = new CSO(username, password);
                break;
            case ADMIN_MANAGER:
                newUser = new AdminManager(username, password);
                break;
            case SCSO:
                newUser = new SCSO(username, password);
                break;
            case HR_MANAGER:
                newUser = new HRManager(username, password);
                break;
            case FINANCIAL_MANAGER:
                newUser = new FinancialManager(username, password);
                break;
            case PRODUCTION_SERVICE_MANAGER:
                newUser = new ProductionServiceManager(username, password);
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        users.add(newUser);
        System.out.println("User created successfully: " + newUser.getUsername());
    }

    // New method to create a SimpleUser
    public SimpleUser createSimpleUser(String username, String password, String department, String subTeam, ProductionServiceManager manager) {
        SimpleUser newUser = new SimpleUser(username, password, department, subTeam, manager);
        users.add(newUser);
        System.out.println("SimpleUser created successfully: " + newUser.getUsername());
        return newUser;
    }

    // Add a method to add an already created user
    public void addUser(User user) {
        users.add(user);
        System.out.println("User added successfully: " + user.getUsername());
    }

    public User authenticate(String username, String password) {
        for (User user : users) {
            if (user.login(username, password)) {
                return user;
            }
        }
        return null; // Invalid credentials
    }

    public List<User> getUsers() {
        return users;
    }
}
