package com.example.adminreservationmanagementapp.reservations;

public class Reservation {
    private final String date, time, specialOffer, status, reason, bookingNo;
    private final int guestCount;

    // Private Constructor - forces use of Builder
    public Reservation(Builder builder) {
        this.date = builder.date;
        this.time = builder.time;
        this.guestCount = builder.guestCount;
        this.specialOffer = builder.specialOffer != null ? builder.specialOffer : "";
        this.status = builder.status != null ? builder.status : "Pending";
        this.reason = builder.reason != null ? builder.reason : "";
        this.bookingNo = builder.bookingNo;
    }

    // Getters
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getSpecialOffer() {
        return specialOffer;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public int getGuestCount() {
        return guestCount;
    }
    public boolean isPending() {
        return "Pending".equals(status);
    }
    public boolean isConfirmed() {
        return "Confirmed".equals(status);
    }

    // Builder Class
    public static class Builder {
        private String date, time, specialOffer, status, reason, bookingNo;
        private int guestCount;

        // Builder Constructor (mandatory)
        public Builder(String date, String time, int guestCount, String bookingNo) {
            this.date = date;
            this.time = time;
            this.guestCount = guestCount;
            this.bookingNo = bookingNo;
        }

        // Setter methods (optional call)
        public Builder setSpecialOffer(String specialOffer) {
            this.specialOffer = specialOffer;
            return this;
        }

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setReason(String reason) {
            this.reason = reason;
            return this;
        }

        // Build method: deal with outer class; to return outer instance
        public Reservation build() {
            return new Reservation(this);
        }
    }
}
