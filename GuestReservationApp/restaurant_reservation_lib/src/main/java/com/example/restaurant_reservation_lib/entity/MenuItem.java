package com.example.restaurant_reservation_lib.entity;

import android.graphics.Bitmap;
import android.os.Build;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.restaurant_reservation_lib.converter.DateConverter;
import com.example.restaurant_reservation_lib.converter.PhotoConverter;

import java.util.Date;

@Entity(tableName = "menuItem")
public class MenuItem {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String foodName, category, mealTime;
    private double price;
    private boolean isAvailable, isPromotion;

    @TypeConverters(PhotoConverter.class)
    private Bitmap image;  // Stored as BLOB in SQLite

    @TypeConverters(DateConverter.class)
    private Date createdAt;

    @TypeConverters(DateConverter.class)
    private Date updatedAt;

//    Public no-arg constructor REQUIRED by Room database
//    Reason: Room needs a public constructor to instantiate the entity when reading from the database
    public MenuItem() {

    }

    // Private Constructor - only access via Builder
    private MenuItem(Builder builder) {
        this.foodName = builder.foodName;
        this.price = builder.price;
        this.category = builder.category;
        this.mealTime = builder.mealTime;
        this.isPromotion = builder.isPromotion;
        this.image = builder.image;
        this.createdAt = builder.createAt;
        this.updatedAt = builder.updateAt;
        this.isAvailable = builder.isAvailable;
    }

    // Getter
    public long getId() {
        return id;
    }

    public String getFoodName() {
        return foodName;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getMealTime() {
        return mealTime;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public Bitmap getImage() {
        return image;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Setter
    public void setId(long id) {
        this.id = id;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setMealTime(String mealTime) {
        this.mealTime = mealTime;
    }

    public void setPromotion(boolean promotion) {
        isPromotion = promotion;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // Static class Builder (manufacturer)
    public static class Builder {
        // instance fields
        // Mandatory fields
        private final String foodName, category, mealTime;
        private final double price;
        private final boolean isAvailable, isPromotion;
        private final Date createAt;

        // Optional fields
        private Bitmap image;
        private Date updateAt;

        // Builder constructor (mandatory)
        public Builder(String foodName, double price, String category, String mealTime, boolean isPromotion, Date createAt, boolean isAvailable) {
            this.foodName = foodName;
            this.price = price;
            this.category = category;
            this.mealTime = mealTime;
            this.isPromotion = isPromotion;
            this.createAt = createAt;
            this.isAvailable = isAvailable;
        }

        // Setter methods (optional)
        public Builder setImage(Bitmap image) {
            this.image = image;
            return this;
        }

        public Builder setUpdateAt(Date updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        // Build method: deal with outer class; to return outer instance
        public MenuItem build() {
            return new MenuItem(this);
        }
    }
}