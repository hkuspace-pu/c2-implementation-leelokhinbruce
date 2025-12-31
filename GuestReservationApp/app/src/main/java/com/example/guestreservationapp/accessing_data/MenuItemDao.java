package com.example.guestreservationapp.accessing_data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.example.restaurant_reservation_lib.entity.MenuMealType;

import java.util.List;

//@Dao
@Dao
public interface MenuItemDao {
    // Get all menu item data
    @Query("SELECT * FROM menuItem ORDER BY category ASC")
    LiveData<List<MenuItem>> getAllMenuItems();
}
