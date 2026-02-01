package com.example.adminreservationmanagementapp.reservations;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminreservationmanagementapp.databinding.FragmentPendingListBinding;
import com.example.restaurant_reservation_lib.entity.Reservation;

import java.util.ArrayList;
import java.util.List;

public class PendingListFragment extends Fragment {
    private FragmentPendingListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPendingListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}