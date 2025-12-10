package com.example.adminreservationmanagementapp.mainpage;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.adminreservationmanagementapp.R;
import com.example.adminreservationmanagementapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Fragment reservationsFragment, menuFragment, notificationsFragment, moreFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        reservationsFragment = new ReservationsFragment();
        menuFragment = new MenuFragment();
        notificationsFragment = new NotificationsFragment();
        moreFragment = new MoreFragment();

        // Set by default fragment
        if (savedInstanceState == null) {
            setCurrentFragment(reservationsFragment);
        }

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.reservations) {
                setCurrentFragment(reservationsFragment);
                return true;
            } else if (itemId == R.id.menu) {
                setCurrentFragment(menuFragment);
                return true;
            } else if (itemId == R.id.notifications) {
                setCurrentFragment(notificationsFragment);
                return true;
            } else if (itemId == R.id.more) {
                setCurrentFragment(moreFragment);
                return true;
            }

            return false;
        });
    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainContainer, fragment)
                .commit();
    }
}