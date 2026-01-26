package com.example.adminreservationmanagementapp;

import com.example.restaurant_reservation_lib.entity.User;

public class Staff extends User {
    private static volatile Staff instance;  // volatile for thread-safety
    private String jobTitle, workingLocation;

    // Constructor
    public Staff(String username, String email, String password, String phoneNumber, String jobTitle, String workingLocation) {
        super(username, email, password, phoneNumber);
        this.jobTitle = jobTitle;
        this.workingLocation = workingLocation;
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

    public String getJobTitle() {
        return jobTitle;
    }

    public String getWorkingLocation() {
        return workingLocation;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setWorkingLocation(String workingLocation) {
        this.workingLocation = workingLocation;
    }

    // Clear Guest instance data
    public static void resetInstance() {
        instance = null;
    }
}
