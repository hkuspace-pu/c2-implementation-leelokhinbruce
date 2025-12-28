package com.example.restaurant_reservation_lib;

import android.content.Context;

import androidx.annotation.NonNull;
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
    public abstract MenuItemDao menuItemDao();  // Returns an instance of the DAO class (MenuItemDao)

    private static volatile AppDatabase INSTANCE;

    private static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);

                    // Insert initial data on first creation
                    // Insert data for MealTime
                    db.execSQL("INSERT INTO MealTime (id, timeOfMenu) VALUES (1, 'Breakfast')");
                    db.execSQL("INSERT INTO MealTime (id, timeOfMenu) VALUES (2, 'Lunch')");
                    db.execSQL("INSERT INTO MealTime (id, timeOfMenu) VALUES (3, 'Tea Time')");
                    db.execSQL("INSERT INTO MealTime (id, timeOfMenu) VALUES (4, 'Dinner')");
                    // Insert data for MealType
                    db.execSQL("INSERT INTO MealTime (id, type) VALUES (1, 'Normal Meal')");
                    db.execSQL("INSERT INTO MealTime (id, type) VALUES (2, 'Large Meal')");
                    db.execSQL("INSERT INTO MealTime (id, type) VALUES (3, 'Special Large Meal')");
                }
            };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Create an instance of the database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "menuItem_and_reservation_database")
                            .addCallback(sRoomDatabaseCallback)  // add callback
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
