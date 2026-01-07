package com.example.guestreservationapp;

import com.example.restaurant_reservation_lib.entity.User;

public class Guest extends User {
    private String firstName, lastName, avatar;
    private char gender;

    public Guest(String email, String password, String phoneNumber, String firstName, String lastName, String avatar, char gender) {
        super(email, password, phoneNumber);
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }
}
