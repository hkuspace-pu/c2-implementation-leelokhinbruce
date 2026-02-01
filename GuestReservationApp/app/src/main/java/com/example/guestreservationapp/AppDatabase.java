package com.example.guestreservationapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.guestreservationapp.accessing_data.ReservationDao;
import com.example.restaurant_reservation_lib.entity.Reservation;

// Build Database
@Database(entities = {Reservation.class},
version = 5, exportSchema = false)  // Annotated with a @Database annotation
public abstract class AppDatabase extends RoomDatabase {
    // Returns an instance of the database class
    private static volatile AppDatabase INSTANCE;

    // Returns an abstract for the DAO class (ReservationDao)
    public abstract ReservationDao reservationDao();

    // Getting an instance for the database
    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "menuItem_and_reservation_database")
                            .fallbackToDestructiveMigration()
//                            .addCallback(roomCallback)
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
            db.execSQL("DROP TABLE IF EXISTS menuItem");
        }
    };
}
