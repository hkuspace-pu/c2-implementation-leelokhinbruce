package com.example.restaurant_reservation_lib.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Entity(tableName = "reservation")
public class Reservation {
    @PrimaryKey
    @NotNull
    private String id;

    private String date, time, status, occasion, specialOffer, reason;
    private String bookingNo;
    private int partySize;

    // Sync action: 0 = NONE, 1 = CREATE, 2 = UPDATE, 3 = DELETE
    private int syncAction;  // Tells the sync worker what to do

    //    Public no-arg constructor REQUIRED by Room database
//    Reason: Room needs a public constructor to instantiate the entity when reading from the database
    public Reservation() {}

    // Constructor
    public Reservation(String date, String time, String status, String occasion, String specialOffer,
                       String reason, String bookingNo, int partySize, int syncAction) {
        this.id = UUID.randomUUID().toString();  // auto-generate UUID
        this.date = date;
        this.time = time;
        this.status = status;
        this.occasion = occasion;
        this.specialOffer = specialOffer;
        this.reason = reason;
        this.bookingNo = bookingNo;
        this.partySize = partySize;
        this.syncAction = syncAction;
    }

    // Getter
    @NonNull
    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public String getOccasion() {
        return occasion;
    }

    public String getSpecialOffer() {
        return specialOffer;
    }

    public String getReason() {
        return reason;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public int getPartySize() {
        return partySize;
    }

    public int getSyncAction() {
        return syncAction;
    }

    // Setter
    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public void setSpecialOffer(String specialOffer) {
        this.specialOffer = specialOffer;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public void setPartySize(int partySize) {
        this.partySize = partySize;
    }

    public void setSyncAction(int syncAction) {
        this.syncAction = syncAction;
    }
}
