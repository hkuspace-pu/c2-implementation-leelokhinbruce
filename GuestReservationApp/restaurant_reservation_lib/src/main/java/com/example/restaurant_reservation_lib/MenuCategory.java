package com.example.restaurant_reservation_lib;

import java.util.List;

public class MenuCategory {
    private String title;
    private List<MenuItem> items;

    public MenuCategory(String title, List<MenuItem> items) {
        this.title = title;
        this.items = items;
    }

    // Getter
    public String getTitle() {
        return title;
    }

    public List<MenuItem> getItems() {
        return items;
    }
}
