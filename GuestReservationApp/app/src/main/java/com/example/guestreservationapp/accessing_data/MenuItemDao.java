package com.example.guestreservationapp.accessing_data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.restaurant_reservation_lib.entity.MenuItem;

import java.util.List;

//@Dao
@Dao
public interface MenuItemDao {
    // Get all menu item data
    @Query("SELECT * FROM menuItem ORDER BY category ASC")
    LiveData<List<MenuItem>> getAllMenuItems();
}
