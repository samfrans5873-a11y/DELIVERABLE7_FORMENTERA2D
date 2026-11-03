package com.scc.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String role;
    private String status; // 'PENDING' or 'APPROVED'

    public User(int id, String username, String email, String password, String role, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public User(int id, String username, String email, String role, String status) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
}