package com.example.restaurant_reservation_lib.entity;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.restaurant_reservation_lib.DateConverter;
import com.example.restaurant_reservation_lib.PhotoConverter;
import com.example.restaurant_reservation_lib.R;

import java.util.Date;

@Entity(tableName = "menuItem")
public class MenuItem {
    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "menu_item_id")
    private long id;

//    @ColumnInfo(name = "food_name")
    private String foodName;
    private double price;
    private String category;
//    @ColumnInfo(name = "is_available")
    private boolean isAvailable;
//    @ColumnInfo(name = "is_promotion")
    private boolean isPromotion;

    @TypeConverters(PhotoConverter.class)
    private Bitmap image;  // Stored as BLOB in SQLite

//    @ColumnInfo(name = "created_at")
    @TypeConverters(DateConverter.class)
    private Date createdAt;

//    @ColumnInfo(name = "updated_at")
    @TypeConverters(DateConverter.class)
    private Date updatedAt;

    // Constructor
    public MenuItem(String foodName, double price, Bitmap image, String category, boolean isAvailable, boolean isPromotion) {
        this.foodName = foodName;
        this.price = price;
//        this.imageRes = R.drawable.photo_icon;
        this.image = image;  // file path of the photo
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

//    public int getImageRes() {
//        return imageRes;
//    }

    public Bitmap getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
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

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setPromotion(boolean promotion) {
        isPromotion = promotion;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}