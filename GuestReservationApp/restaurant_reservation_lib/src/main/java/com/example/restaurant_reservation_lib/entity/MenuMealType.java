package com.example.restaurant_reservation_lib.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "menuMealType",
        primaryKeys = {"menu_item_id", "meal_type_id"},
        foreignKeys = {
                @ForeignKey(entity = MenuItem.class, parentColumns = "menu_item_id", childColumns = "menu_item_id", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = MealType.class, parentColumns = "meal_type_id", childColumns = "meal_type_id", onDelete = ForeignKey.CASCADE)
        })
public class MenuMealType {
    @ColumnInfo(name = "menu_item_id")
    public long menuItemId;
    @ColumnInfo(name = "meal_type_id")
    public long mealTypeId;
}
