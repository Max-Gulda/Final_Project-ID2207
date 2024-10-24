package com.sep.system.util;

import com.sep.system.user.*;
import java.io.*;
import java.util.*;

public class DataManager {
    private static final String USERS_FILE = "users_data.csv";
    private static final String SUBTEAMS_FILE = "sub_teams_data.csv";

    // Save users to CSV file
    public static void saveUsers(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            writer.println("username,password,role,department,subTeam,manager"); // Add manager column
            for (User user : users) {
                if (user instanceof SimpleUser) {
                    SimpleUser simpleUser = (SimpleUser) user;
                    writer.printf("%s,%s,%s,%s,%s,%s%n",
                            simpleUser.getUsername(),
                            simpleUser.getPassword(),
                            simpleUser.getRole().name(),
                            simpleUser.getDepartment(),
                            simpleUser.getSubTeam(),
                            simpleUser.getManager() != null ? simpleUser.getManager().getUsername() : "N/A"); // Save manager username
                } else {
                    writer.printf("%s,%s,%s,N/A,N/A,N/A%n", user.getUsername(), user.getPassword(), user.getRole().name());
                }
            }
            System.out.println("Users saved successfully to CSV.");
        } catch (IOException e) {
            System.err.println("Error saving users to CSV: " + e.getMessage());
        }

        // Save sub-teams for ProductionServiceManagers
        saveSubTeams(users);
    }

    // Load users from CSV file
    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line = reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) { // Update length check for manager
                    String username = parts[0];
                    String password = parts[1];
                    Role role = Role.valueOf(parts[2]);
                    String department = parts[3];
                    String subTeam = parts[4];
                    String managerUsername = parts[5]; // Retrieve manager username

                    // Create user based on role
                    User user = createUserFromRole(username, password, role, department, subTeam, managerUsername, users);
                    users.add(user);
                }
            }

            System.out.println("Users loaded successfully from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading users from CSV: " + e.getMessage());
        }

        // Load sub-teams and link to ProductionServiceManagers
        loadSubTeams(users);

        return users.isEmpty() ? null : users;
    }

    // Save sub-teams to CSV file
    private static void saveSubTeams(List<User> users) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SUBTEAMS_FILE))) {
            writer.println("subTeamName,managerUsername,memberUsername");
            for (User user : users) {
                if (user instanceof ProductionServiceManager) {
                    ProductionServiceManager manager = (ProductionServiceManager) user;
                    Map<String, List<SimpleUser>> subTeams = manager.getSubTeams();
                    for (Map.Entry<String, List<SimpleUser>> entry : subTeams.entrySet()) {
                        String subTeamName = entry.getKey();
                        for (SimpleUser member : entry.getValue()) {
                            writer.printf("%s,%s,%s%n", subTeamName, manager.getUsername(), member.getUsername());
                        }
                    }
                }
            }
            System.out.println("Sub-teams saved successfully to CSV.");
        } catch (IOException e) {
            System.err.println("Error saving sub-teams to CSV: " + e.getMessage());
        }
    }

    // Load sub-teams from CSV file and link to the appropriate ProductionServiceManagers
    private static void loadSubTeams(List<User> users) {
        try (BufferedReader reader = new BufferedReader(new FileReader(SUBTEAMS_FILE))) {
            String line = reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String subTeamName = parts[0];
                    String managerUsername = parts[1];
                    String memberUsername = parts[2];

                    // Find the manager with the given username
                    for (User user : users) {
                        if (user instanceof ProductionServiceManager && user.getUsername().equals(managerUsername)) {
                            ProductionServiceManager manager = (ProductionServiceManager) user;

                            // Ensure the sub-team exists before adding members
                            if (!manager.getSubTeams().containsKey(subTeamName)) {
                                manager.createSubTeam(subTeamName);
                            }

                            // Find the member with the given username
                            for (User memberUser : users) {
                                if (memberUser instanceof SimpleUser && memberUser.getUsername().equals(memberUsername)) {
                                    manager.addMemberToSubTeam((SimpleUser) memberUser, subTeamName);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }
            }

            System.out.println("Sub-teams loaded successfully from CSV.");
        } catch (IOException e) {
            System.err.println("Error loading sub-teams from CSV: " + e.getMessage());
        }
    }

    // Helper method to create user instances based on role
    private static User createUserFromRole(String username, String password, Role role, String department, String subTeam, String managerUsername, List<User> users) {
        switch (role) {
            case CSO:
                return new CSO(username, password);
            case ADMIN_MANAGER:
                return new AdminManager(username, password);
            case SCSO:
                return new SCSO(username, password);
            case HR_MANAGER:
                return new HRManager(username, password);
            case FINANCIAL_MANAGER:
                return new FinancialManager(username, password);
            case PRODUCTION_SERVICE_MANAGER:
                return new ProductionServiceManager(username, password);
            case SIMPLE_USER:
                ProductionServiceManager manager = (ProductionServiceManager) findUserByUsername(users, managerUsername);
                return new SimpleUser(username, password, department, subTeam, manager); // Pass the manager
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    // Helper method to find user by username
    private static User findUserByUsername(List<User> users, String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
