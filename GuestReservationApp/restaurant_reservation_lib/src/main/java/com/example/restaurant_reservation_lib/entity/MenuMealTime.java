package com.example.restaurant_reservation_lib.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "menuMealTime",
        primaryKeys = {"menu_item_id", "meal_time_id"},
        foreignKeys = {
        // parentColumns: target entity's primary key
                // childColumns: this entity's foreign key
                @ForeignKey(entity = MenuItem.class, parentColumns = "menu_item_id", childColumns = "menu_item_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = MealTime.class, parentColumns = "meal_time_id", childColumns = "meal_time_id", onDelete = ForeignKey.CASCADE)
        })
public class MenuMealTime {
    @ColumnInfo(name = "menu_item_id")
    public long menuItemId;
    @ColumnInfo(name = "meal_time_id")
    public long mealTimeId;
}
