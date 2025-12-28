package com.example.restaurant_reservation_lib.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "menuMealTime",
        primaryKeys = {"menuItemId", "mealTimeId"},
        foreignKeys = {
        // parentColumns: target entity's primary key
                // childColumns: this entity's foreign key
                @ForeignKey(entity = MenuItem.class, parentColumns = "id", childColumns = "menuItemId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = MealTime.class, parentColumns = "id", childColumns = "mealTimeId", onDelete = ForeignKey.CASCADE)
        })
public class MenuMealTime {
//    @ColumnInfo(name = "menu_item_id")
    private long menuItemId;
//    @ColumnInfo(name = "meal_time_id")
    private long mealTimeId;

    public MenuMealTime(long menuItemId, long mealTimeId) {
        this.menuItemId = menuItemId;
        this.mealTimeId = mealTimeId;
    }

    public long getMenuItemId() {
        return menuItemId;
    }

    public long getMealTimeId() {
        return mealTimeId;
    }
}
