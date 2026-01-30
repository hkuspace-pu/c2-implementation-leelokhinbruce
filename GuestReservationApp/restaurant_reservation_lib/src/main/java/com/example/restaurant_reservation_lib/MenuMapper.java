package com.example.restaurant_reservation_lib;

import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.example.restaurant_reservation_lib.request.MenuItemRequest;

public class MenuMapper {
    // Local data -> Request
    public static MenuItemRequest toDto(MenuItem menuItem) {
        MenuItemRequest dto = new MenuItemRequest(
                menuItem.getServerId(),
                menuItem.getFoodName(),
                menuItem.getCategory(),
                menuItem.getMealTime(),
                menuItem.getPrice(),
                menuItem.isAvailable(),
                menuItem.isPromotion(),
//                menuItem.getImage(),
                menuItem.getUpdatedAt());
        return dto;
    }

    public static MenuItem fromDto(MenuItemRequest dto) {
        MenuItem menuItem = new MenuItem.Builder(
                dto.getFoodName(),
                dto.getCategory(),
                dto.getMealTime(),
                dto.getPrice(),
                dto.getIsAvailable(),
                dto.getIsPromotion(),
                dto.getLastModified(),
                0  // nothing pending after pull from server
        ).build();

        return menuItem;
    }
}
