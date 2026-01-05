package com.example.restaurant_reservation_lib.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "menuMealType",
        primaryKeys = {"menuItemId", "mealTypeId"},
        foreignKeys = {
                @ForeignKey(entity = MenuItem.class, parentColumns = "id", childColumns = "menuItemId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = MealType.class, parentColumns = "id", childColumns = "mealTypeId", onDelete = ForeignKey.CASCADE)
        })
public class MenuMealType {
    private long menuItemId;
    private int mealTypeId;

    public MenuMealType(long menuItemId, int mealTypeId) {
        this.menuItemId = menuItemId;
        this.mealTypeId = mealTypeId;
    }

    public long getMenuItemId() {
        return menuItemId;
    }

    public int getMealTypeId() {
        return mealTypeId;
    }
}
