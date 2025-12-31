package com.example.adminreservationmanagementapp.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.adminreservationmanagementapp.AppDatabase;
import com.example.adminreservationmanagementapp.accessing_data.MenuItemDao;
import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.example.restaurant_reservation_lib.entity.MenuMealType;

import java.util.Date;
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

    public void insertMenuItem(MenuItem menuItem) {
//        menuItem.setCreatedAt(new Date());
//        return menuItemDao.insertItem(menuItem);
        new InsertItemAsyncTask(menuItemDao).execute(menuItem);
    }

    public void insertMenuMealType(MenuMealType menuMealType) {
//        menuItemDao.insertMenuMealType(menuMealType);
        new InsertMenuMealTypeAsyncTask(menuItemDao).execute(menuMealType);
    }

    public void updateMenuItem(MenuItem menuItem) {
        menuItem.setUpdatedAt(new Date());
//        menuItemDao.updateItem(menuItem);
        new UpdateItemAsyncTask(menuItemDao).execute(menuItem);
    }

    public void deleteMenuItem(MenuItem menuItem) {
//        menuItemDao.deleteItem(menuItem);
        new DeleteItemAsyncTask(menuItemDao).execute(menuItem);
    }

    // --- Async task methods ---
    private static class InsertItemAsyncTask extends AsyncTask<MenuItem, Void, Void> {
        private MenuItemDao dao;

        private InsertItemAsyncTask(MenuItemDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(MenuItem... menuItems) {
            // Insert the menu item in dao
            dao.insertItem(menuItems[0]);
            return null;
        }
    }

    private static class InsertMenuMealTypeAsyncTask extends AsyncTask<MenuMealType, Void, Void> {
        private MenuItemDao dao;

        private InsertMenuMealTypeAsyncTask(MenuItemDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(MenuMealType... menuMealTypes) {
            dao.insertMenuMealType(menuMealTypes[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<MenuItem, Void, Void> {
        private MenuItemDao dao;

        private UpdateItemAsyncTask(MenuItemDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(MenuItem... menuItems) {
            dao.updateItem(menuItems[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends AsyncTask<MenuItem, Void, Void> {
        private MenuItemDao dao;

        private DeleteItemAsyncTask(MenuItemDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(MenuItem... menuItems) {
            dao.deleteItem(menuItems[0]);
            return null;
        }
    }
}
