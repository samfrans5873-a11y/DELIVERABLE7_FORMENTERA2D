package com.scc;

import com.scc.dao.DatabaseInitializer;
import com.scc.ui.Authentication;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Guard application with DatabaseInitializer
        DatabaseInitializer.initializeDatabase();

        Scanner scanner = new Scanner(System.in);
        Authentication auth = new Authentication();

        int choice;
        do {
            System.out.println("\n=== OPEN-BUDGET ANTI-CORRUPTION TRACKING SYSTEM ===");
            System.out.println("[1] Authentication & Access Control");
            System.out.println("[2] Exit System");
            System.out.print("Select an option: ");

            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Select an option: ");
                scanner.next();
            }
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    auth.showAuthMenu();
                    break;
                case 2:
                    System.out.println("Exiting Open-Budget Anti-Corruption Tracking System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } while (choice != 2);

        scanner.close();
    }
}