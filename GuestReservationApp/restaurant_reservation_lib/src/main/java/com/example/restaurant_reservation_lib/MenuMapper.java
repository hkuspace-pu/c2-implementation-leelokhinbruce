package com.example.restaurant_reservation_lib;

import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.example.restaurant_reservation_lib.request.MenuItemRequest;

public class MenuMapper {
    // Local item -> Request DTO
    public static MenuItemRequest toDto(MenuItem menuItem) {
        return new MenuItemRequest(
                menuItem.getServerId(),
                menuItem.getFoodName(),
                menuItem.getCategory(),
                menuItem.getMealTime(),
                menuItem.getPrice(),
                menuItem.isAvailable(),
                menuItem.isPromotion(),
                menuItem.getUpdatedAt());
    }

    // DTO -> local item
    public static MenuItem fromDto(MenuItemRequest dto) {

        return new MenuItem.Builder(
                dto.getFoodName(),
                dto.getCategory(),
                dto.getMealTime(),
                dto.getPrice(),
                dto.isAvailable(),
                dto.isPromotion(),
                dto.getUpdatedAt(),
                0  // nothing pending after pull from server
        ).build();
    }
}
