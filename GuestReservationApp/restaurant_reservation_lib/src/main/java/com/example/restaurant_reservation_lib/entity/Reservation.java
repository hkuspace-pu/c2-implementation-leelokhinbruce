package com.example.restaurant_reservation_lib.entity;

public class Reservation {
    private static volatile Reservation instance;  // Volatile for thread-safety

    private String date, time, status, reason, occasion, specialOffer;
    private String bookingNo;
    private int guestCount;

    // Private Constructor - only called from Builder or getInstance
    private Reservation(Builder builder) {
        this.date = builder.date;
        this.time = builder.time;
        this.guestCount = builder.guestCount;
        this.status = builder.status;
        this.bookingNo = builder.bookingNo;
        this.occasion = builder.occasion;
        this.specialOffer = builder.specialOffer;
    }

    // Lazy initialization
    // static method to provide access to the single instance
    // synchronized: ensures that only one thread can access the getInstance() at a time, preventing multiple instances
    public static synchronized Reservation getInstance() {
        if (instance == null)
            instance = new Builder("default_date", "default_time", 1, "Pending", "default_bookingNo").build();  // Create instance if it doesn't exist

        return instance;
    }

    // Create/reset singleton with Builder
    public static Reservation init(Builder builder) {
        instance = builder.build();
        return instance;
    }

    // Getters
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getGuestCount() {
        return guestCount;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getOccasion() {
        return occasion;
    }

    public String getSpecialOffer() {
        return specialOffer;
    }

    // Setters
    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setGuestCount(int guestCount) {
        this.guestCount = guestCount;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public void setSpecialOffer(String specialOffer) {
        this.specialOffer = specialOffer;
    }

    // Builder Class
    public static class Builder {
        private String date, time, status, reason, occasion, specialOffer;
        private final String bookingNo;
        private int guestCount;

        // Builder Constructor (mandatory)
        public Builder(String date, String time, int guestCount, String status, String bookingNo) {
            this.date = date;
            this.time = time;
            this.guestCount = guestCount;
            this.status = status;
            this.bookingNo = bookingNo;
        }

        // Setter methods (optional call)
        public Builder setReason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder setOccasion(String occasion) {
            this.occasion = occasion;
            return this;
        }

        public Builder setSpecialOffer(String specialOffer) {
            this.specialOffer = specialOffer;
            return this;
        }

        // Build method: deal with outer class; to return outer instance
        public Reservation build() {
            return new Reservation(this);
        }
    }
}
