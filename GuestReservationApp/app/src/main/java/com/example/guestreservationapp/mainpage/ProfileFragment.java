package com.example.guestreservationapp.mainpage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guestreservationapp.Guest;
import com.example.guestreservationapp.MyPointsActivity;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.accessing_data.AuthApi;
import com.example.guestreservationapp.databinding.FragmentProfileBinding;
import com.example.guestreservationapp.myprofile.MyProfileActivity;
import com.example.guestreservationapp.reservation.ReservationHistoryActivity;
import com.example.guestreservationapp.settings.SettingsActivity;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.SessionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        // Get guest instance
        Guest guest = Guest.getInstance();
        // Set user data to layouts
        String fullName = String.format("%s %s", guest.getFirstName(), guest.getLastName());
        binding.textName.setText(fullName);

        // Go to My Profile
        binding.frameBtnMyProfile.setOnClickListener(viewMyProfile -> {
            Intent intent = new Intent(getContext(), MyProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Go to History
        binding.frameBtnHistory.setOnClickListener(viewHistory -> {
            Intent intent = new Intent(getContext(), ReservationHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Go to Settings
        binding.frameBtnSettings.setOnClickListener(viewSettings -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Go to My Point
        binding.frameBtnMyPoint.setOnClickListener(viewMyPoint -> {
            Intent intent = new Intent(getContext(), MyPointsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return view;
    }
}