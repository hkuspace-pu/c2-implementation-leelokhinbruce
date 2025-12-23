package com.example.restaurant_reservation_lib;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.restaurant_reservation_lib.accessing_data.MenuItemDao;
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
    public abstract MenuItemDao menuItemDao();  // Returns an instance of the DAO class (MenuItemDao)

    private static volatile AppDatabase INSTANCE;

//    public static final Migration MIGRATION_1_2  = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase db) {
//            super.migrate(db);
//            db.execSQL("ALTER TABLE MenuItem ADD COLUMN");
//        }
//    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Create an instance of the database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "menuItem_and_reservation_database")
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
