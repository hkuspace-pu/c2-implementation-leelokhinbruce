package com.example.adminreservationmanagementapp.mainpage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.adminreservationmanagementapp.databinding.FragmentReservationsBinding;
import com.example.adminreservationmanagementapp.reservations.ReservationHistoryActivity;
import com.example.adminreservationmanagementapp.reservations.ReservationPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ReservationsFragment extends Fragment {
    private FragmentReservationsBinding binding;
    private ReservationPagerAdapter pagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReservationsBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        binding.btnHistory.setOnClickListener(viewHistory -> {
            Intent intent = new Intent(getContext(), ReservationHistoryActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup ViewPager2
        pagerAdapter = new ReservationPagerAdapter(this);
        binding.viewPagerReservationList.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPagerReservationList, (tab, position) -> {
            if (position == 0) {
                tab.setText("Pending");
            } else {
                tab.setText("Confirmed");
            }
        }).attach();
    }
}