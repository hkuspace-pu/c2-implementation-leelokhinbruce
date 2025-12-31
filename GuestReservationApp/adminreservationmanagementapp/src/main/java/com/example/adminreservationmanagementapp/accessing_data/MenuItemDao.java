package com.example.adminreservationmanagementapp.accessing_data;

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
@androidx.room.Dao
public interface MenuItemDao {
    // Add new menu item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(MenuItem menuItem);

    @Insert
    void insertMenuMealType(MenuMealType junction);

    // Edit a menu item
    @Update
    void updateItem(MenuItem menuItem);

    // Remove a menu item
    @Delete
    void deleteItem(MenuItem menuItem);

    // Get all menu item data
    @Query("SELECT * FROM menuItem ORDER BY category ASC")
    LiveData<List<MenuItem>> getAllMenuItems();
}
