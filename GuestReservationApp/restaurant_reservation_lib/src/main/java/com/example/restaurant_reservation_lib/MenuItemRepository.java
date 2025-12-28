package com.example.restaurant_reservation_lib;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.restaurant_reservation_lib.accessing_data.MenuItemDao;
import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.example.restaurant_reservation_lib.entity.MenuMealTime;
import com.example.restaurant_reservation_lib.entity.MenuMealType;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuItemRepository {
    private MenuItemDao menuItemDao;
    private LiveData<List<MenuItem>> allMenuItems;

    public MenuItemRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);  // Get an instance of the database
        menuItemDao = db.menuItemDao();  // Get an instance of the DAO
        allMenuItems = menuItemDao.getAllMenuItems();  // Use DAO's method to interact with the database - QUERY all menu item data
    }

    // Get menu items from the list
    public LiveData<List<MenuItem>> getAllMenuItems() {
        return allMenuItems;
    }

    public long insertMenuItem(MenuItem menuItem) {
//        menuItem.setCreatedAt(new Date());
        return menuItemDao.insertItem(menuItem);
    }

    public void insertMenuMealTime(MenuMealTime menuMealTime) {
        menuItemDao.insertMenuMealTime(menuMealTime);
    }

    public void insertMenuMealType(MenuMealType menuMealType) {
        menuItemDao.insertMenuMealType(menuMealType);
    }

    public void updateMenuItem(MenuItem menuItem) {
        menuItem.setUpdatedAt(new Date());
        menuItemDao.updateItem(menuItem);
    }

    public void deleteMenuItem(MenuItem menuItem) {
        menuItemDao.deleteItem(menuItem);
    }
}
