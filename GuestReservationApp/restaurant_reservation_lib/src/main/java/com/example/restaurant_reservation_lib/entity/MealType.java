package com.example.restaurant_reservation_lib.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mealType")
public class MealType {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String type;

    public MealType(String type, long id) {
        this.type = type;
        this.id = id;
    }
}
