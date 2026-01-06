package com.example.guestreservationapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.guestreservationapp.AppDatabase;
import com.example.guestreservationapp.accessing_data.MenuItemDao;
import com.example.restaurant_reservation_lib.entity.MenuItem;

import java.util.List;

public class MenuItemRepository {
    private MenuItemDao menuItemDao;
    private LiveData<List<MenuItem>> allMenuItems;

    // Constructor
    public MenuItemRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);  // Get an instance of the database
        menuItemDao = db.menuItemDao();  // Get an instance of the DAO
        allMenuItems = menuItemDao.getAllMenuItems();  // Use DAO's method to interact with the database - QUERY all menu item data
    }

    // Get all menu items from the list
    public LiveData<List<MenuItem>> getAllMenuItems() {
        return allMenuItems;
    }
}
