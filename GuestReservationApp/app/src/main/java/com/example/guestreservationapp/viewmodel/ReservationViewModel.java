package com.example.guestreservationapp.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.guestreservationapp.repository.ReservationRepository;
import com.example.restaurant_reservation_lib.entity.Reservation;

import java.util.List;

public class ReservationViewModel extends AndroidViewModel {
    private final ReservationRepository repository;
    private final LiveData<List<Reservation>> allReservations;

    public ReservationViewModel(Application app) {
        super(app);
        repository = new ReservationRepository(app);  // New an instance of the repository for calling its methods
        allReservations = repository.getAllReservations();  // Get all reservations from the repository
    }

    public void createReservation(Reservation reservation) {
        repository.insertReservation(reservation);
    }

    public void updateReservation(Reservation reservation) {
        repository.updateReservation(reservation);
    }

    public void cancelReservation(Reservation reservation) {
        repository.deleteReservation(reservation);
    }

    public LiveData<List<Reservation>> getAllReservations() {
        return allReservations;
    }
}
