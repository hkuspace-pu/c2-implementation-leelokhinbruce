package com.example.restaurant_reservation_lib;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MenuItemViewModel extends AndroidViewModel {
    private MenuItemRepository repository;
    private LiveData<List<MenuItem>> allMenuItems;

    public MenuItemViewModel(Application app) {
        super(app);
        repository = new MenuItemRepository(app);  // New an instance of the repository for calling its methods
        allMenuItems = repository.getAllMenuItems();  // Get all menu items from the repository
    }

    public void insertMenuItem(MenuItem menuItem) {
        repository.insertMenuItem(menuItem);
    }

    public void updateMenuItem(MenuItem menuItem) {
        repository.updateMenuItem(menuItem);
    }

    public void deleteMenuItem(MenuItem menuItem) {
        repository.deleteMenuItem(menuItem);
    }

    public LiveData<List<MenuItem>> getAllMenuItems() {
        return allMenuItems;
    }
}
