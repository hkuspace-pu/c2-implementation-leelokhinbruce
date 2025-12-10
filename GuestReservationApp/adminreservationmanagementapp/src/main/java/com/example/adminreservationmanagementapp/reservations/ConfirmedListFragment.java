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
    private ReservationAdapter adapter;
    private List<Reservation> reservationList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConfirmedListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // Sample data
//        reservationList.add(new Reservation.Builder("Today", "18:30", 2, "247").build());
//
//        // Set LayoutManager
//        binding.recycleConfirmed.setLayoutManager(new LinearLayoutManager(requireContext()));
//
//        // Set Adapter
//        adapter = new ReservationAdapter(reservationList);
//        binding.recycleConfirmed.setAdapter(adapter);
//    }
}