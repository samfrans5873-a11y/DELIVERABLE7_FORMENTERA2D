package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.Scanner;

public class Authentication {
    private final UserDao userDao;
    private final Scanner scanner;

    public Authentication() {
        this.userDao = new UserDao();
        this.scanner = new Scanner(System.in);
    }

    public void showAuthMenu() {
        int choice;
        do {
            System.out.println("\n=== AUTHENTICATION & ACCESS CONTROL ===");
            System.out.println("[1] Register New Account");
            System.out.println("[2] Login");
            System.out.println("[3] Back to Main Menu");
            System.out.print("Select an option: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Select an option: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    handleRegistration();
                    break;
                case 2:
                    handleLogin();
                    break;
                case 3:
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 3);
    }

    private void handleRegistration() {
        System.out.println("\n--- REGISTER NEW ACCOUNT ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        System.out.print("Select Role: [1] ADMIN | [2] CITIZEN | [3] AUDITOR\nEnter choice: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid choice. Enter choice (1-3): ");
            scanner.next();
        }
        int roleChoice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        String role;
        switch (roleChoice) {
            case 1:
                role = "ADMIN";
                break;
            case 2:
                role = "CITIZEN";
                break;
            case 3:
                role = "AUDITOR";
                break;
            default:
                role = "CITIZEN";
                System.out.println("Invalid role choice. Defaulted to CITIZEN.");
        }

        boolean success = userDao.registerUser(username, email, password, role);
        if (success) {
            System.out.println("[SUCCESS] User registered successfully as " + role + " (Status: PENDING)!");
        } else {
            System.out.println("[ERROR] Registration failed. Username may already exist.");
        }
    }

    public void handleLogin() {
        System.out.println("\n--- SYSTEM LOGIN ---");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        User user = userDao.authenticateUser(username, password);

        if (user == null) {
            System.out.println("Authentication failed: Invalid username or password.");
            return;
        }

        if ("PENDING".equalsIgnoreCase(user.getStatus())) {
            System.out.println("Authentication failed: Your account is pending administrator approval.");
            return;
        }

        System.out.println("Login successful! Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
        routeUser(user);
    }

    private void routeUser(User user) {
        String role = user.getRole().toUpperCase();
        switch (role) {
            case "ADMIN":
                AdminMenu.showAdminMenu(user);
                break;
            case "CITIZEN":
                System.out.println("Redirecting to Citizen Dashboard (Open-Budget Tracking)...");
                break;
            case "AUDITOR":
                System.out.println("Redirecting to Auditor Dashboard (Anti-Corruption Reports)...");
                break;
            default:
                System.out.println("Unknown role assigned. Cannot route interface.");
        }
    }
}