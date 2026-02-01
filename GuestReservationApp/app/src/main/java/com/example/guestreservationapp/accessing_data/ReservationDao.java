package com.example.guestreservationapp.accessing_data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.restaurant_reservation_lib.entity.Reservation;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface ReservationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(Reservation reservation);

    @Update
    void updateItem(Reservation reservation);

    @Delete
    void deleteItem(Reservation reservation);

    // Get reservations if they are not Cancel
    @Query("SELECT * FROM reservation ORDER BY date ASC")
    LiveData<List<Reservation>> getAllReservations();
}
