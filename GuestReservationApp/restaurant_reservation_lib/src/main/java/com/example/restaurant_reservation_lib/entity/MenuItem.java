package com.example.restaurant_reservation_lib.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.restaurant_reservation_lib.DateConverter;
import com.example.restaurant_reservation_lib.R;

import java.util.Date;

@Entity(tableName = "menuItem")
public class MenuItem {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String foodName;
    private double price;
    private int imageRes;  // dummy
    private String category;
    private boolean isAvailable;
    private boolean isPromotion;

    @TypeConverters(DateConverter.class)
    private Date createdAt;
    @TypeConverters(DateConverter.class)
    private Date updatedAt;

    // Constructor
    public MenuItem(String foodName, double price, String category, boolean isAvailable, boolean isPromotion) {
        this.foodName = foodName;
        this.price = price;
        this.imageRes = R.drawable.photo_icon;
        this.category = category;
        this.isAvailable = isAvailable;
        this.isPromotion = isPromotion;
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

    public int getImageRes() {
        return imageRes;
    }

    public String getCategory() {
        return category;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    // Setter
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}