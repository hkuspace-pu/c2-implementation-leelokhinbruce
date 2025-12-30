package com.example.restaurant_reservation_lib;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.restaurant_reservation_lib.accessing_data.MenuItemDao;
import com.example.restaurant_reservation_lib.converter.DateConverter;
import com.example.restaurant_reservation_lib.entity.MealTime;
import com.example.restaurant_reservation_lib.entity.MealType;
import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.example.restaurant_reservation_lib.entity.MenuMealTime;
import com.example.restaurant_reservation_lib.entity.MenuMealType;

// Build Database
@Database(entities = {MenuItem.class, MealType.class, MealTime.class,
        MenuMealType.class, MenuMealTime.class},
        version = 1, exportSchema = false)  // Annotated with a @Database annotation
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    // Returns an instance of the database class
//    private static volatile AppDatabase INSTANCE;
    private static AppDatabase INSTANCE;

    // Returns an abstract for the DAO class (MenuItemDao)
    public abstract MenuItemDao menuItemDao();

    // Getting an instance for the database
    public static synchronized AppDatabase getDatabase(Context context) {
//        if (INSTANCE == null) {
//            synchronized (AppDatabase.class) {
        if (INSTANCE == null) {
            // For creating an instance for the database
            // We are creating a database builder and passing
            // the database class with the database name
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "menuItem_and_reservation_database")
                    // Add fall back to destructive migration to the database
                    .fallbackToDestructiveMigration()
                    // Add call back to the database
                    .addCallback(roomCallback)
                    // Build the database
                    .build();
        }
//            }
//        }

        return INSTANCE;
    }

    // Callback: call when database is created
    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Insert initial data on first creation
            // Insert data for MealTime
            db.execSQL("DROP TABLE IF EXISTS MealTime");
            // Insert data for MealType
            db.execSQL("INSERT INTO MealType (id, type) VALUES (1, 'Normal Meal')");
            db.execSQL("INSERT INTO MealType (id, type) VALUES (2, 'Large Meal')");
            db.execSQL("INSERT INTO MealType (id, type) VALUES (3, 'Special Large Meal')");

            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };

    // Async task class: performs task in background
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(AppDatabase instance) {
            MenuItemDao dao = instance.menuItemDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
