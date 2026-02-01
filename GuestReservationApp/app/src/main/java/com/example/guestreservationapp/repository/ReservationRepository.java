package com.example.guestreservationapp.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.guestreservationapp.AppDatabase;
import com.example.guestreservationapp.accessing_data.ReservationDao;
import com.example.restaurant_reservation_lib.entity.Reservation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReservationRepository {
    private final ReservationDao dao;
    private final LiveData<List<Reservation>> allReservations;
    private final ExecutorService executorService;

    // Constructor
    public ReservationRepository(Application app) {
        AppDatabase db = AppDatabase.getDatabase(app);  // Get an instance of the database
        dao = db.reservationDao();  // Get an instance of the DAO
        allReservations = dao.getAllReservations();  // Use DAO's method to interact with the database
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Reservation>> getAllReservations() {
        return allReservations;
    }

    // CREATE
    public void insertReservation(Reservation reservation) {
        executorService.execute(() -> dao.insertItem(reservation));
    }

    // UPDATE
    public void updateReservation(Reservation reservation) {
        executorService.execute(() -> dao.updateItem(reservation));
    }

    // DELETE
    public void deleteReservation(Reservation reservation) {
        executorService.execute(() -> dao.deleteItem(reservation));
    }
}
