package com.example.adminreservationmanagementapp;

import com.example.restaurant_reservation_lib.entity.User;

public class Staff extends User {
    private static volatile Staff instance;  // volatile for thread-safety
    private String position, workingBranch;

    // Constructor
    public Staff(String username, String email, String password, String phoneNumber, String position, String workingBranch) {
        super(username, email, password, phoneNumber);
        this.position = position;
        this.workingBranch = workingBranch;
    }

    // Lazy initialization: creation of instance when required
    public static synchronized Staff getInstance() {
        if (instance == null) {
            // default instance without any data (except gender)
            instance = new Staff(null, null, null, null, null, null);
        }

        return instance;
    }

    // Create/reset singleton
    public static Staff init(Staff staff) {
        instance = staff;
        return instance;
    }

    public String getPosition() {
        return position;
    }

    public String getWorkingBranch() {
        return workingBranch;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setWorkingBranch(String workingBranch) {
        this.workingBranch = workingBranch;
    }

    // Clear Guest instance data
    public static void resetInstance() {
        instance = null;
    }
}
