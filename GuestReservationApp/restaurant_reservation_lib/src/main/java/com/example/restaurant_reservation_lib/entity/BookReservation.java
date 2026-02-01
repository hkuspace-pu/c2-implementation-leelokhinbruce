package com.example.restaurant_reservation_lib.entity;

// Used for Booking a new reservation
public class BookReservation {
    private static volatile BookReservation instance;  // Volatile for thread-safety

    private String date, time, occasion, specialOffer;
    private final String bookingNo;
    private int guestCount;  // party size

    // Private Constructor - only called from Builder or getInstance
    BookReservation(Builder builder) {
        this.date = builder.date;
        this.time = builder.time;
        this.guestCount = builder.guestCount;
        this.bookingNo = builder.bookingNo;
        this.occasion = builder.occasion;
        this.specialOffer = builder.specialOffer;
    }

    // Lazy initialization
    // static method to provide access to the single instance
    // synchronized: ensures that only one thread can access the getInstance() at a time, preventing multiple instances
    public static synchronized BookReservation getInstance() {
        if (instance == null)
            instance = new Builder("default_date", "default_time", 1, "default_bookingNo").build();  // Create instance if it doesn't exist

        return instance;
    }

    // Create/reset singleton with Builder
    public static BookReservation init(Builder builder) {
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
        private final String date, time;
        private final String bookingNo;
        private final int guestCount;

        private String occasion, specialOffer;

        // Builder Constructor (mandatory)
        public Builder(String date, String time, int guestCount, String bookingNo) {
            this.date = date;
            this.time = time;
            this.guestCount = guestCount;
            this.bookingNo = bookingNo;
        }

        // Setter methods (optional call)
        public Builder setOccasion(String occasion) {
            this.occasion = occasion;
            return this;
        }

        public Builder setSpecialOffer(String specialOffer) {
            this.specialOffer = specialOffer;
            return this;
        }

        // Build method: deal with outer class; to return outer instance
        public BookReservation build() {
            return new BookReservation(this);
        }
    }

    // Clear instance data
    public static void resetInstance() {
        instance = null;
    }
}
