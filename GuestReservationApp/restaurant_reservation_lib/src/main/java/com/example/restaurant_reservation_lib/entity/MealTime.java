package com.example.restaurant_reservation_lib.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mealTime")
public class MealTime {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "meal_time_id")
    public long id;

    @ColumnInfo(name = "time_of_menu")
    public String timeOfMenu;
}
