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
//    @ColumnInfo(name = "menu_item_id")
    private long menuItemId;
//    @ColumnInfo(name = "meal_type_id")
    private long mealTypeId;

    public MenuMealType(long menuItemId, long mealTypeId) {
        this.menuItemId = menuItemId;
        this.mealTypeId = mealTypeId;
    }

    public long getMenuItemId() {
        return menuItemId;
    }

    public long getMealTypeId() {
        return mealTypeId;
    }
}
