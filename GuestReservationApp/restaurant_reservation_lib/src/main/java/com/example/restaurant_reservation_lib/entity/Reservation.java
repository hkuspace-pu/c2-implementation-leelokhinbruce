package com.example.restaurant_reservation_lib.entity;

import java.io.Serializable;

public class Reservation implements Serializable {
    private final String date, time, status, bookingNo;
    private final int guestCount;

    // Private Constructor - forces use of Builder
    public Reservation(Builder builder) {
        this.date = builder.date;
        this.time = builder.time;
        this.guestCount = builder.guestCount;
        this.status = builder.status != null ? builder.status : "Pending";
        this.bookingNo = builder.bookingNo;
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

    // Builder Class
    public static class Builder {
        private String date, time, status, bookingNo;
        private int guestCount;

        // Builder Constructor (mandatory)
        public Builder(String date, String time, int guestCount, String bookingNo) {
            this.date = date;
            this.time = time;
            this.guestCount = guestCount;
            this.bookingNo = bookingNo;
        }

        // Setter methods (optional call)

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        // Build method: deal with outer class; to return outer instance
        public Reservation build() {
            return new Reservation(this);
        }
    }
}
