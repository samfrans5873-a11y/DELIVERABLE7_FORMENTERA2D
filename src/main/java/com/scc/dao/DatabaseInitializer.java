package com.scc.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        String schemaScript =
                "PRAGMA foreign_keys = ON;\n" +

                        "CREATE TABLE IF NOT EXISTS users (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    username TEXT UNIQUE NOT NULL,\n" +
                        "    email TEXT NOT NULL,\n" +
                        "    password TEXT NOT NULL,\n" +
                        "    role TEXT NOT NULL,\n" +
                        "    status TEXT NOT NULL DEFAULT 'PENDING'\n" +
                        ");\n" +

                        "CREATE TABLE IF NOT EXISTS budget_allocations (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    project_name TEXT NOT NULL,\n" +
                        "    department TEXT NOT NULL,\n" +
                        "    allocated_amount REAL NOT NULL,\n" +
                        "    disbursed_amount REAL NOT NULL,\n" +
                        "    fiscal_year INTEGER NOT NULL,\n" +
                        "    status TEXT NOT NULL\n" +
                        ");\n" +

                        "CREATE TABLE IF NOT EXISTS corruption_reports (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    title TEXT NOT NULL,\n" +
                        "    description TEXT NOT NULL,\n" +
                        "    budget_id INTEGER,\n" +
                        "    reported_by_user_id INTEGER,\n" +
                        "    status TEXT NOT NULL DEFAULT 'SUBMITTED',\n" +
                        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                        "    FOREIGN KEY (budget_id) REFERENCES budget_allocations(id) ON DELETE SET NULL,\n" +
                        "    FOREIGN KEY (reported_by_user_id) REFERENCES users(id) ON DELETE CASCADE\n" +
                        ");\n" +

                        "CREATE TABLE IF NOT EXISTS audit_logs (\n" +
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        "    user_id INTEGER,\n" +
                        "    action_performed TEXT NOT NULL,\n" +
                        "    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" +
                        "    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL\n" +
                        ");\n" +

                        "INSERT OR IGNORE INTO users (id, username, email, password, role, status) \n" +
                        "VALUES (1, 'admin', 'admin@system.com', 'admin123', 'ADMIN', 'APPROVED');";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(schemaScript);
            System.out.println("Database and tables initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
}