package com.example.guestreservationapp.mainpage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guestreservationapp.MyPointsActivity;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.databinding.FragmentProfileBinding;
import com.example.guestreservationapp.myprofile.MyProfileActivity;
import com.example.guestreservationapp.reservation.ReservationHistoryActivity;
import com.example.guestreservationapp.settings.SettingsActivity;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        binding.frameBtnMyProfile.setOnClickListener(viewMyProfile -> {
            Intent intent = new Intent(getContext(), MyProfileActivity.class);
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

        binding.frameBtnMyPoint.setOnClickListener(viewMyPoint -> {
            Intent intent = new Intent(getContext(), MyPointsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return view;
    }
}