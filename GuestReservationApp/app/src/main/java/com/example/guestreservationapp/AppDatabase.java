package com.example.guestreservationapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.guestreservationapp.accessing_data.MenuItemDao;
import com.example.restaurant_reservation_lib.entity.MenuItem;

// Build Database
@Database(entities = {MenuItem.class},
version = 3, exportSchema = false)  // Annotated with a @Database annotation
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
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "menuItem_and_reservation_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    // Callback: call when database is created
    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Insert initial data on first creation
            db.execSQL("DROP TABLE IF EXISTS mealTime");
            db.execSQL("DROP TABLE IF EXISTS menuMealTime");
            db.execSQL("DROP TABLE IF EXISTS mealType");
            db.execSQL("DROP TABLE IF EXISTS menuMealType");

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
