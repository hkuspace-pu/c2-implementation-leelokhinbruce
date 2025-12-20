package com.example.restaurant_reservation_lib.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mealType")
public class MealType {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "meal_type_id")
    public long id;

    @ColumnInfo(name = "meal_type")
    public String type;
}
