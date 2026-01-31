package com.example.restaurant_reservation_lib.request;

import java.util.Date;

public class MenuItemRequest {
    private Long id;  // server id (nullable or create)
    private String foodName, category, mealTime;
    private double price;
    private boolean isAvailable, isPromotion;
    private Date updatedAt;

    // Constructor
    public MenuItemRequest(Long id, String foodName, String category, String mealTime, double price,
                           boolean isAvailable, boolean isPromotion, Date updatedAt) {
        this.id = id;
        this.foodName = foodName;
        this.category = category;
        this.mealTime = mealTime;
        this.price = price;
        this.isAvailable = isAvailable;
        this.isPromotion = isPromotion;
        this.updatedAt = updatedAt;
    }

    // Getter
    public Long getId() {
        return id;
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

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
