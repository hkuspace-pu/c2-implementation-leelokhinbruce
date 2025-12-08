package com.example.guestreservationapp.mainpage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.guestreservationapp.NotificationsActivity;
import com.example.guestreservationapp.databinding.FragmentHomeBinding;
import com.example.guestreservationapp.myprofile.MyProfileActivity;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        binding.imgBtnProfile.setOnClickListener(viewProfile -> {
            Intent intent = new Intent(getContext(), MyProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        binding.imgBtnNotification.setOnClickListener(viewNotification -> {
            Intent intent = new Intent(getContext(), NotificationsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return view;
    }
}