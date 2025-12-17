package com.example.restaurant_reservation_lib;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;

@Entity(tableName = "menuItem")
public class MenuItem {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String foodName;
    private double price;
    private int imageRes;  // dummy
    private List<String> menuTime;
    private List<String> mealType;
    private String category;
    private boolean isAvaiable;
    private boolean isPromotion;

    @TypeConverters(DateConverter.class)
    private Date createdAt;
    @TypeConverters(DateConverter.class)
    private Date updatedAt;

    // Constructor
    public MenuItem(String foodName, double price, List<String> menuTime, List<String> mealType, String category, boolean isAvaiable, boolean isPromotion) {
        this.foodName = foodName;
        this.price = price;
        this.imageRes = R.drawable.photo_icon;
        this.menuTime = menuTime;
        this.mealType = mealType;
        this.category = category;
        this.isAvaiable = isAvaiable;
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

    public List<String> getMenuTime() {
        return menuTime;
    }

    public List<String> getMealType() {
        return mealType;
    }

    public String getCategory() {
        return category;
    }

    public boolean isPromotion() {
        return isPromotion;
    }
}