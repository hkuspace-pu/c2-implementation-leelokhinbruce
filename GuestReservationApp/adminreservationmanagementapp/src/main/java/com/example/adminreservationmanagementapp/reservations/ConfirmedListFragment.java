package com.example.adminreservationmanagementapp.reservations;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        ReservationAdapter adapter = new ReservationAdapter();
        binding.recycleConfirmed.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recycleConfirmed.setAdapter(adapter);

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(new Reservation.Builder("Today", "18:30", 2, "247")
                .setStatus("Pending")
                .build());

        adapter.submitList(reservationList);

        return binding.getRoot();
    }
}