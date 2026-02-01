package com.example.adminreservationmanagementapp.database_management;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.adminreservationmanagementapp.accessing_data.MenuItemDao;
import com.example.restaurant_reservation_lib.converter.DateConverter;
import com.example.restaurant_reservation_lib.entity.MenuItem;

// Build Database
@Database(entities = {MenuItem.class},
        version = 6, exportSchema = false)  // Annotated with a @Database annotation
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    // Returns an instance of the database class
    private static volatile AppDatabase INSTANCE;

    // Returns an abstract for the DAO class (MenuItemDao)
    public abstract MenuItemDao menuItemDao();

    // Getting an instance for the database
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // For creating an instance for the database
                    // We are creating a database builder and passing
                    // the database class with the database name
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "menuItem_and_reservation_database")
                            // fallbackToDestructiveMigration(): Drop old DB, recreate with new schema, fix crash instantly
                            .fallbackToDestructiveMigration()
                            // Build the database
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
