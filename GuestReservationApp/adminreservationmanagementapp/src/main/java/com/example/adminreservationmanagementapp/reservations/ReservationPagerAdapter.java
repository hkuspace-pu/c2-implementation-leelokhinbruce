package com.example.adminreservationmanagementapp.reservations;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class ReservationPagerAdapter extends FragmentStateAdapter {


    public ReservationPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                new PendingListFragment();
            case 1:
                new ConfirmedListFragment();
            default:
                new PendingListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;  // Pending and Confirmed lists
    }
}
