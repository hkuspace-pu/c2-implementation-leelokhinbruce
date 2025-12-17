package com.example.restaurant_reservation_lib.accessing_data;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurant_reservation_lib.MenuItem;

import java.util.List;

public interface MenuItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(MenuItem menuItem);

    @Update
    void updateItem(MenuItem menuItem);

    @Delete
    void deleteItem(MenuItem menuItem);

//    @Query("SELECT * FROM menuItem ORDER BY updated_at DESC")
//    LiveData<List<MenuItem>> getAllMenuItems();
}
