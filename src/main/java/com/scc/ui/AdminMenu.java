package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDao userDao = new UserDao();

    public static void showAdminMenu(User currentAdmin) {
        int choice;
        do {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. View ALL System Users");
            System.out.println("2. View Pending Registrations");
            System.out.println("3. Approve User Account");
            System.out.println("4. Update User Role & Status");
            System.out.println("5. Logout / Return to Main Menu");

            choice = readIntInput("Enter choice: ");

            switch (choice) {
                case 1:
                    viewAllUsers();
                    break;
                case 2:
                    viewPendingUsers();
                    break;
                case 3:
                    approveUserWorkflow();
                    break;
                case 4:
                    updateUserWorkflow(currentAdmin);
                    break;
                case 5:
                    System.out.println("Logging out from Admin session...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 5);
    }

    private static void viewAllUsers() {
        List<User> users = userDao.getAllUsers();
        System.out.println("\n--- ALL SYSTEM USERS ---");
        System.out.printf("%-5s %-15s %-25s %-10s %-10s%n", "ID", "Username", "Email", "Role", "Status");
        System.out.println("------------------------------------------------------------------");
        for (User u : users) {
            System.out.printf("%-5d %-15s %-25s %-10s %-10s%n",
                    u.getId(), u.getUsername(), u.getEmail(), u.getRole(), u.getStatus());
        }
    }

    private static void viewPendingUsers() {
        List<User> pendingUsers = userDao.getPendingUsers();
        System.out.println("\n--- PENDING REGISTRATIONS ---");
        if (pendingUsers.isEmpty()) {
            System.out.println("No pending users found.");
            return;
        }
        System.out.printf("%-5s %-15s %-25s %-10s%n", "ID", "Username", "Email", "Role");
        System.out.println("---------------------------------------------------------");
        for (User u : pendingUsers) {
            System.out.printf("%-5d %-15s %-25s %-10s%n",
                    u.getId(), u.getUsername(), u.getEmail(), u.getRole());
        }
    }

    private static void approveUserWorkflow() {
        viewPendingUsers();
        System.out.print("Enter User ID to approve (or 0 to cancel): ");
        int userId = readIntInput("");
        if (userId == 0) return;

        boolean success = userDao.approveUser(userId);
        if (success) {
            System.out.println("User account approved successfully!");
        } else {
            System.out.println("Failed to approve user. Check if ID is valid.");
        }
    }

    private static void updateUserWorkflow(User currentAdmin) {
        viewAllUsers();
        System.out.print("\nEnter User ID to update (or 0 to cancel): ");
        int targetUserId = readIntInput("");

        if (targetUserId == 0) {
            System.out.println("Update cancelled.");
            return;
        }

        // Self-Edit Restriction Rule
        if (targetUserId == currentAdmin.getId()) {
            System.out.println("WARNING: Modifying the currently active admin account is restricted.");
            return;
        }

        String newRole = readValidRole();
        String newStatus = readValidStatus();

        boolean success = userDao.updateUserRoleAndStatus(targetUserId, newRole, newStatus);
        if (success) {
            System.out.println("User updated successfully to Role: " + newRole + " and Status: " + newStatus);
        } else {
            System.out.println("Failed to update user. Please verify the User ID.");
        }
    }

    // --- INPUT VALIDATION WRAPPERS ---

    private static int readIntInput(String prompt) {
        if (!prompt.isEmpty()) {
            System.out.print(prompt);
        }
        while (!scanner.hasNextInt()) {
            System.out.print("[ERROR] Invalid input. Please enter a valid number (or 0 to cancel): ");
            scanner.next(); // Consume invalid input
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return value;
    }

    private static String readValidRole() {
        while (true) {
            System.out.print("Enter new role (ADMIN, CITIZEN, AUDITOR): ");
            String role = scanner.nextLine().trim().toUpperCase();
            if (role.equals("ADMIN") || role.equals("CITIZEN") || role.equals("AUDITOR")) {
                return role;
            }
            System.out.println("[ERROR] Invalid role. Please choose between ADMIN, CITIZEN, or AUDITOR.");
        }
    }

    private static String readValidStatus() {
        while (true) {
            System.out.print("Enter new status (APPROVED, PENDING): ");
            String status = scanner.nextLine().trim().toUpperCase();
            if (status.equals("APPROVED") || status.equals("PENDING")) {
                return status;
            }
            System.out.println("[ERROR] Invalid status. Please choose between APPROVED or PENDING.");
        }
    }
}