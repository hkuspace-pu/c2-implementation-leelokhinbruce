package com.example.restaurant_reservation_lib.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mealTime")
public class MealTime {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String timeOfMenu;

    public MealTime(long id, String timeOfMenu) {
        this.id = id;
        this.timeOfMenu = timeOfMenu;
    }
}
