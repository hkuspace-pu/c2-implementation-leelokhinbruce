package com.example.adminreservationmanagementapp.reservations;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminreservationmanagementapp.databinding.FragmentPendingListBinding;

import java.util.ArrayList;
import java.util.List;

public class PendingListFragment extends Fragment {
    private FragmentPendingListBinding binding;
    private PendingReservationAdapter adapter;
    private List<Reservation> reservationList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPendingListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecycleView();  // Initialize RecycleView

        // Sample - load pending data
        reservationList.add(new Reservation.Builder("Today", "18:30", 2, "247").build());

        adapter.notifyDataSetChanged();  // Notify RecycleView to conduct entire updating
    }

    private void setupRecycleView() {
        adapter = new PendingReservationAdapter(reservationList);
        binding.recyclePending.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclePending.setAdapter(adapter);
    }
}