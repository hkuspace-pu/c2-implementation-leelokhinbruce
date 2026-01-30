package com.example.restaurant_reservation_lib.request;

import java.util.Date;

public class MenuItemRequest {
    private Long id;  // server id (nullable or create)
    private String foodName, category, mealTime;
    private double price;
    private boolean isAvailable, isPromotion;
//    private String imgUrl;
    private Date lastModified;

    // Constructor
    public MenuItemRequest(Long id, String foodName, String category, String mealTime, double price,
                           boolean isAvailable, boolean isPromotion,
                           Date lastModified) {
        this.id = id;
        this.foodName = foodName;
        this.category = category;
        this.mealTime = mealTime;
        this.price = price;
        this.isAvailable = isAvailable;
        this.isPromotion = isPromotion;
//        this.imgUrl = imgUrl;
        this.lastModified = lastModified;
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

    public boolean getIsAvailable() {
        return isAvailable;
    }

    public boolean getIsPromotion() {
        return isPromotion;
    }

//    public String getImgUrl() {
//        return imgUrl;
//    }

    public Date getLastModified() {
        return lastModified;
    }
}
