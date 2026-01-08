package com.example.adminreservationmanagementapp;

import com.example.restaurant_reservation_lib.entity.User;

public class Staff extends User {
    private final String username;
    private String role, workingLocation;

    public Staff(String email, String password, String phoneNumber, String username, String role, String workingLocation) {
        super(email, password, phoneNumber);
        this.username = username;
        this.role = role;
        this.workingLocation = workingLocation;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getWorkingLocation() {
        return workingLocation;
    }

    public void setWorkingLocation(String workingLocation) {
        this.workingLocation = workingLocation;
    }
}
