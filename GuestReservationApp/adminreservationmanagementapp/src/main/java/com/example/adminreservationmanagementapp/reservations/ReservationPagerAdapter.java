package com.example.adminreservationmanagementapp.reservations;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.adminreservationmanagementapp.mainpage.ReservationsFragment;

import java.util.List;

public class ReservationPagerAdapter extends FragmentStateAdapter {


    public ReservationPagerAdapter(@NonNull ReservationsFragment fragment) {
        super(fragment.getChildFragmentManager(), fragment.getViewLifecycleOwner().getLifecycle());
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? new PendingListFragment() : new ConfirmedListFragment();
    }

    @Override
    public int getItemCount() {
        return 2;  // Pending and Confirmed lists
    }
}
