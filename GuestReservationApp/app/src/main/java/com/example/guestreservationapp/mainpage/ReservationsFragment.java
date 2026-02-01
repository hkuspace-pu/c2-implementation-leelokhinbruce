package com.example.guestreservationapp.mainpage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.FragmentReservationsBinding;
import com.example.guestreservationapp.reservation.ReservationActivity;
import com.example.guestreservationapp.reservation.ReservationHistoryActivity;
import com.example.guestreservationapp.viewmodel.ReservationViewModel;
import com.example.restaurant_reservation_lib.adapter.ReservationAdapter;
import com.example.restaurant_reservation_lib.entity.Reservation;

public class ReservationsFragment extends Fragment {
    private FragmentReservationsBinding binding;
    private ReservationViewModel reservationViewModel;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReservationsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        // Set recycleView
        binding.recycleReservation.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recycleReservation.setHasFixedSize(true);

        // Initialize an adapter for the recycle view
        final ReservationAdapter adapter = new ReservationAdapter();

        // Set item action listener for item of recycle view
        // Adapter as a listener to listen which menu item is being activated (clicked)
        // Display dialog when clicked a hidden button on an item

        // Close open actions when tapping outside any item
        binding.getRoot().setOnTouchListener((view1, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                adapter.closeCurrentOpenItem();
            return false;  // Allow touch to pass through
        });

        // Set the adapter instance to the RecycleView to inflate the items
        binding.recycleReservation.setAdapter(adapter);

        // Initialize ViewModel
        reservationViewModel = new ViewModelProvider(this).get(ReservationViewModel.class);
        // Add reservations to the adapter
        reservationViewModel.getAllReservations().observe(getViewLifecycleOwner(), adapter::setReservations);

        // History button click
        binding.btnHistory.setOnClickListener(viewHistory -> {
            Intent intent = new Intent(getContext(), ReservationHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Book reservation button click
        binding.btnBook.setOnClickListener(viewBook -> {
            Intent intent = new Intent(getContext(), ReservationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return view;
    }
}