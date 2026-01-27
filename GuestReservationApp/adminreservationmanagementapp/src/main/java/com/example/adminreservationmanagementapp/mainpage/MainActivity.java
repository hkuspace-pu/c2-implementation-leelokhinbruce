package com.example.adminreservationmanagementapp.mainpage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.adminreservationmanagementapp.R;
import com.example.adminreservationmanagementapp.Staff;
import com.example.adminreservationmanagementapp.accessing_data.StaffInfoApi;
import com.example.adminreservationmanagementapp.databinding.ActivityMainBinding;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.SessionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends SessionManager {
    private ActivityMainBinding binding;
    private Fragment reservationsFragment, menuFragment, notificationsFragment, moreFragment;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Fetch user data
        executorService.execute(this::fetchUserData);

        reservationsFragment = new ReservationsFragment();
        menuFragment = new MenuFragment();
        notificationsFragment = new NotificationsFragment();
        moreFragment = new MoreFragment();

        // Set by default fragment
        if (savedInstanceState == null) {
            setCurrentFragment(reservationsFragment);
        }

        // Menu bottom bar
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

    // Fetch user data
    private void fetchUserData() {
        String token = getAccessToken();
        // getClient(token): pass the token to the Authorization header
        StaffInfoApi api = ApiClient.getClient(token).create(StaffInfoApi.class);
        Call<Staff> call = api.getStaffData();

        call.enqueue(new Callback<Staff>() {
            @Override
            public void onResponse(Call<Staff> call, Response<Staff> response) {
                if (response.isSuccessful()) {
                    Staff staff = response.body();
                    Staff.init(staff);
                    Log.d("MainActivity fetchUserData()", "User instance: " + staff.getEmail());
                } else {
                    Log.e("MainActivity fetchUserData()", "Failed to fetch user data: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Staff> call, Throwable t) {
                Log.e("MainActivity fetchUserData()", "Network error: " + t.getMessage());
            }
        });
    }
}