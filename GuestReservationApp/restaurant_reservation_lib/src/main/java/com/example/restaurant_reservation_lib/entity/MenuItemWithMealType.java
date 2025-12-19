package com.example.restaurant_reservation_lib.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class MenuItemWithMealType {
    @Embedded public MenuItem menuItem;
    @Relation(
            parentColumn = "menuItemId",
            entityColumn = "mealTypeId",
            associateBy = @Junction(MenuMealType.class)
    )
    public List<MealType> mealTypes;
}
