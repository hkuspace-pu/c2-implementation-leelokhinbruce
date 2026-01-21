package com.example.guestreservationapp;

import com.example.restaurant_reservation_lib.entity.User;

public class Guest extends User {
    private static volatile Guest instance;  // volatile for thread-safety
    private String firstName, lastName, gender;

    // Constructor
    public Guest(String username, String email, String password, String phoneNumber, String firstName, String lastName, String gender) {
        super(username, email, password, phoneNumber);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    // Lazy initialization: creation of instance when required
    public static synchronized Guest getInstance() {
        if (instance == null) {
            instance = new Guest("username", "email", "password", "phone", "firstName", "lastName", "Rather not say");
        }

        return instance;
    }

    // Create/reset singleton
    public static Guest init(Guest guest) {
        instance = guest;
        return instance;
    }

    // Getter
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    // Setter
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
