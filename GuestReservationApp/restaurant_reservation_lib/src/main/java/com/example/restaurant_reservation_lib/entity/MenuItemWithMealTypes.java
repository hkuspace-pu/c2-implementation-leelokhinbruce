package com.example.restaurant_reservation_lib.entity;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class MenuItemWithMealTypes {
    @Embedded public MenuItem menuItem;
    @Relation(
            parentColumn = "id",
            entityColumn = "id",
            associateBy = @Junction(
                    value = MenuMealType.class,
                    parentColumn = "menuItemId",
                    entityColumn = "mealTypeId"
            )
    )
    public List<MealType> mealTypes;
}
