package com.example.adminreservationmanagementapp;

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
        version = 5, exportSchema = false)  // Annotated with a @Database annotation
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
                            // Add fall back to destructive migration to the database
//                            .fallbackToDestructiveMigration()
                            .addMigrations(new Migration(4, 5) {
                                @Override
                                public void migrate(@NonNull SupportSQLiteDatabase db) {
                                    db.execSQL("DROP TABLE IF EXISTS mealType");
                                    db.execSQL("DROP TABLE IF EXISTS menuMealType");
                                }
                            })
                            // Add call back to the database
                            .addCallback(roomCallback)
                            // Build the database
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
