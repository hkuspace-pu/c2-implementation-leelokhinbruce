package com.example.adminreservationmanagementapp.accessing_data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Upsert;

import com.example.restaurant_reservation_lib.entity.MenuItem;

import java.util.List;

@Dao
public interface MenuItemDao {
    // Add new menu item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(MenuItem menuItem);

    // Edit a menu item
    @Upsert
    void updateItem(MenuItem menuItem);

    // Remove a menu item
    @Delete
    void deleteItem(MenuItem menuItem);

    // Get all menu item data
    @Query("SELECT * FROM menuItem ORDER BY category ASC")
    LiveData<List<MenuItem>> getMenuItemsWithCategory();

    // Sync-related
    @Query("SELECT * FROM menuItem WHERE syncAction != 0")
    List<MenuItem> getPendingItemsSync();

    // Get remote menu items to bring local DB up-to-date
    @Query("SELECT * FROM menuItem WHERE serverId = :serverId LIMIT 1")
    MenuItem getByServerId(Long serverId);
}
