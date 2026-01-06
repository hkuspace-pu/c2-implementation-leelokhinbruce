package com.example.guestreservationapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.guestreservationapp.repository.MenuItemRepository;
import com.example.restaurant_reservation_lib.entity.MenuItem;

import java.util.List;

public class MenuItemViewModel extends AndroidViewModel {
    private MenuItemRepository repository;
    private LiveData<List<MenuItem>> allMenuItems;

    // Constructor
    public MenuItemViewModel(Application app) {
        super(app);
        repository = new MenuItemRepository(app);  // New an instance of the repository for calling its methods
        allMenuItems = repository.getAllMenuItems();  // Get all menu items from the repository
    }

    public LiveData<List<MenuItem>> getAllMenuItems() {
        return allMenuItems;
    }
}
