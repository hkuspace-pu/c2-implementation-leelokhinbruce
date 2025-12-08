package com.example.adminreservationmanagementapp.mainpage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminreservationmanagementapp.R;
import com.example.adminreservationmanagementapp.databinding.FragmentMoreBinding;
import com.example.adminreservationmanagementapp.reservations.ReservationHistoryActivity;
import com.example.adminreservationmanagementapp.settings.AccountDetailsActivity;
import com.example.adminreservationmanagementapp.settings.SettingsActivity;

public class MoreFragment extends Fragment {
    private FragmentMoreBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMoreBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        binding.frameBtnAccountDetails.setOnClickListener(viewAccountDetails -> {
            Intent intent = new Intent(getContext(), AccountDetailsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        binding.frameBtnHistory.setOnClickListener(viewHistory -> {
            Intent intent = new Intent(getContext(), ReservationHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        binding.frameBtnSettings.setOnClickListener(viewSettings -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return view;
    }
}