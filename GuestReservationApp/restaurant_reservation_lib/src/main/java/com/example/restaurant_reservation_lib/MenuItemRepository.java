package com.example.restaurant_reservation_lib;

import android.app.Application;
import android.view.Menu;

import androidx.lifecycle.LiveData;
import com.example.restaurant_reservation_lib.accessing_data.MenuItemDao;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuItemRepository {
    private MenuItemDao menuItemDao;
    private LiveData<List<MenuItem>> allMenuItems;
    private ExecutorService executorService;

    public MenuItemRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);
        menuItemDao = db.menuItemDao();  // Get an instance of the DAO
        allMenuItems = menuItemDao.getAllMenuItems();  // Use DAO's method to interact with the database - QUERY all menu item data
        executorService = Executors.newSingleThreadExecutor();
    }

    // Get menu items from the list
    public LiveData<List<MenuItem>> getAllMenuItems() {
        return allMenuItems;
    }

    public void insertMenuItem(MenuItem menuItem) {
        executorService.execute(() -> {
            menuItem.setCreatedAt(new Date());
            menuItemDao.insertItem(menuItem);
        });
    }

    public void updateMenuItem(MenuItem menuItem) {
        executorService.execute(() -> {
            menuItem.setUpdatedAt(new Date());
            menuItemDao.updateItem(menuItem);
        });
    }

    public void deleteMenuItem(MenuItem menuItem) {
        executorService.execute(() -> menuItemDao.deleteItem(menuItem));
    }
}
