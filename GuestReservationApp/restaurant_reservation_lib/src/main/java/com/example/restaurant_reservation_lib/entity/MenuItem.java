package com.example.restaurant_reservation_lib.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.restaurant_reservation_lib.converter.DateConverter;

import java.util.Date;

// Local/Room Entity for SQLite
@Entity(tableName = "menuItem")
public class MenuItem {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private Long serverId;  // id for MySQL
    private String foodName, category, mealTime;
    private double price;
    private boolean isAvailable, isPromotion;

    // Store image as base64 or path to local file
//    private String image;

    @TypeConverters(DateConverter.class)
    private Date updatedAt;

    // Sync action: 0 = NONE, 1 = CREATE, 2 = UPDATE, 3 = DELETE
    private int syncAction;  // Tells the sync worker what to do

    //    Public no-arg constructor REQUIRED by Room database
//    Reason: Room needs a public constructor to instantiate the entity when reading from the database
    public MenuItem() {
    }

    // Private Constructor - only access via Builder
    private MenuItem(Builder builder) {
        this.serverId = builder.serverId;
        this.foodName = builder.foodName;
        this.category = builder.category;
        this.mealTime = builder.mealTime;
        this.price = builder.price;
        this.isAvailable = builder.isAvailable;
        this.isPromotion = builder.isPromotion;
//        this.image = builder.image;
        this.updatedAt = builder.updateAt;
        this.syncAction = builder.syncAction;
    }

    // Getter
    public long getId() {
        return id;
    }

    public Long getServerId() {
        return serverId;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getCategory() {
        return category;
    }

    public String getMealTime() {
        return mealTime;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

//    public String getImage() {
//        return image;
//    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public int getSyncAction() {
        return syncAction;
    }

    // Setter
    public void setId(long id) {
        this.id = id;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setPromotion(boolean promotion) {
        isPromotion = promotion;
    }

//    public void setImage(String image) {
//        this.image = image;
//    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSyncAction(int syncAction) {
        this.syncAction = syncAction;
    }

    // Static class Builder (manufacturer)
    public static class Builder {
        // instance fields
        // Mandatory fields
        private final String foodName, category, mealTime;
        private final double price;
        private final boolean isAvailable, isPromotion;
        private final Date updateAt;
        private final int syncAction;

        // Optional fields
        private Long serverId;
//        private String image;

        // Builder constructor (mandatory)
        public Builder(String foodName, String category, String mealTime, double price,
                       boolean isAvailable, boolean isPromotion, Date updateAt, int syncAction) {
            this.foodName = foodName;
            this.category = category;
            this.mealTime = mealTime;
            this.price = price;
            this.isAvailable = isAvailable;
            this.isPromotion = isPromotion;
            this.updateAt = updateAt;
            this.syncAction = syncAction;
        }

        // Setter methods
        public Builder setServerId(Long serverId) {
            this.serverId = serverId;
            return this;
        }

//        public Builder setImage(String image) {
//            this.image = image;
//            return this;
//        }

        // Build method: deal with outer class; to return outer instance
        public MenuItem build() {
            return new MenuItem(this);
        }
    }
}