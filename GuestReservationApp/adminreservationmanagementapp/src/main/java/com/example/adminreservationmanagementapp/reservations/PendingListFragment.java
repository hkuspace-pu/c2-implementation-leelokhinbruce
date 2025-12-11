package com.example.adminreservationmanagementapp.reservations;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminreservationmanagementapp.databinding.FragmentPendingListBinding;
import com.example.restaurant_reservation_lib.Reservation;

import java.util.ArrayList;
import java.util.List;

public class PendingListFragment extends Fragment {
    private FragmentPendingListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPendingListBinding.inflate(inflater, container, false);

        List<Reservation> reservationList = new ArrayList<>();
        // Sample Data
        reservationList.add(new Reservation.Builder(
                "Today", "18:30", 3, "367").build());
        reservationList.add(new Reservation.Builder(
                "Nov 8 Thu", "18:00", 2, "823").build());

        // Assign the reservation list to the adapter
        ReservationAdapter adapter = new ReservationAdapter(reservationList);

        // Set the LayoutManager that the RecycleView will use
        binding.recyclePending.setLayoutManager(new LinearLayoutManager(getContext()));
        // Set the adapter instance to the RecycleView to inflate the items
        binding.recyclePending.setAdapter(adapter);

        return binding.getRoot();
    }
}