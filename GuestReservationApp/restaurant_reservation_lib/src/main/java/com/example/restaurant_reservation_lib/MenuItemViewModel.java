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
        repository = new MenuItemRepository(app);
        allMenuItems = repository.getAllMenuItems();
    }
}
