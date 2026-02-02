package com.example.adminreservationmanagementapp.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.adminreservationmanagementapp.database_management.AppDatabase;
import com.example.adminreservationmanagementapp.accessing_data.MenuItemDao;
import com.example.adminreservationmanagementapp.database_management.SyncHelper;
import com.example.restaurant_reservation_lib.entity.MenuItem;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuItemRepository {
    private final MenuItemDao dao;
    private final LiveData<List<MenuItem>> allMenuItems;
    private final ExecutorService executorService;
    private final Application app;

    // Constructor
    public MenuItemRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);  // Get an instance of the database
        dao = db.menuItemDao();  // Get an instance of the DAO
        allMenuItems = dao.getMenuItemsWithCategory();  // Use DAO's method to interact with the database - QUERY all menu item data
        this.executorService = Executors.newSingleThreadExecutor();
        this.app = app;
    }

    // Get all menu items from the list
    public LiveData<List<MenuItem>> getAllMenuItems() {
        return allMenuItems;
    }

    // CREATE
    public void insertMenuItem(MenuItem menuItem) {
        menuItem.setSyncAction(1);  // CREATE
        executorService.execute(() -> dao.insertItem(menuItem));
        // Enqueue WorkManager sync immediately
        SyncHelper.enqueueImmediateSync(app);  // Schedules a OneTimeWorkRequest for SyncWorker
    }

    // UPDATE
    public void updateMenuItem(MenuItem menuItem) {
        if (menuItem.getServerId() == null || menuItem.getServerId() == -1)
            // The menu item doesn't exist in MySQL
            menuItem.setSyncAction(1);  // CREATE
        else
            menuItem.setSyncAction(2);  // UPDATE

        menuItem.setUpdatedAt(new Date());
        executorService.execute(() -> dao.updateItem(menuItem));
        SyncHelper.enqueueImmediateSync(app);
    }

    // DELETE
    public void deleteMenuItem(MenuItem menuItem) {
        menuItem.setSyncAction(3);  // DELETE
        // Update the item to set its syncAction = 3 in local DB
        executorService.execute(() -> dao.deleteItem(menuItem));
        SyncHelper.enqueueImmediateSync(app);
    }
}
