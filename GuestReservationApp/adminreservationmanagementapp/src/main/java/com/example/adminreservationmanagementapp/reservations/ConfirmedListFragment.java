package com.example.adminreservationmanagementapp.reservations;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminreservationmanagementapp.R;
import com.example.adminreservationmanagementapp.databinding.FragmentConfirmedListBinding;
import com.example.adminreservationmanagementapp.databinding.FragmentPendingListBinding;

import java.util.ArrayList;
import java.util.List;

public class ConfirmedListFragment extends Fragment {
    private FragmentConfirmedListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConfirmedListBinding.inflate(inflater, container, false);

        List<Reservation> reservationList = new ArrayList<>();
        // Sample Data
        reservationList.add(new Reservation.Builder(
                "Today", "18:30", 3, "367").build());
        reservationList.add(new Reservation.Builder(
                "Nov 8 Thu", "18:00", 2, "823").build());

        // Assign the reservation list to the adapter
        ReservationAdapter adapter = new ReservationAdapter(reservationList);
        // Set the LayoutManager that the RecycleView will use
        binding.recycleConfirmed.setLayoutManager(new LinearLayoutManager(getContext()));
        // Set the adapter instance to the RecycleView to inflate the items
        binding.recycleConfirmed.setAdapter(adapter);

        return binding.getRoot();
    }
}