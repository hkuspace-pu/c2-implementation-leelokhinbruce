package com.example.restaurant_reservation_lib.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "menuMealTime",
        primaryKeys = {"menuItemId", "mealTimeId"},
        foreignKeys = {
                @ForeignKey(entity = MenuItem.class, parentColumns = "id", childColumns = "menuItemId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = MealTime.class, parentColumns = "id", childColumns = "mealTimeId", onDelete = ForeignKey.CASCADE)
        })
public class MenuMealTime {
    public long menuItemId;
    public long mealTimeId;
}
