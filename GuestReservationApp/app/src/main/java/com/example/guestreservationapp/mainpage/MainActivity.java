package com.example.guestreservationapp.mainpage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.guestreservationapp.Guest;
import com.example.guestreservationapp.R;
import com.example.guestreservationapp.accessing_data.GuestInfoApi;
import com.example.guestreservationapp.databinding.ActivityMainBinding;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.session_management.SessionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends SessionManager {
    private ActivityMainBinding binding;
    private Fragment homeFragment, menuFragment, reservationsFragment, discoverFragment, profileFragment;
    private ExecutorService executorService;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        View view = binding.getRoot();  // get a reference to the root view of the corresponding layout file
        setContentView(view);  // make it the active view on the screen

        // Creates a thread pool with a single worker thread to make sure threads will be executed sequentially
        executorService = Executors.newSingleThreadExecutor();
        // Main thread handler
        mainHandler = new Handler(Looper.getMainLooper());

        // Fetch user data
        fetchUserData();

        // screens of menu
        homeFragment = new HomeFragment();
        menuFragment = new MenuFragment();
        reservationsFragment = new ReservationsFragment();
        discoverFragment = new DiscoverFragment();
        profileFragment = new ProfileFragment();

        // Set by default fragment
        if (savedInstanceState == null)
            setCurrentFragment(homeFragment);  // Load the initial fragment

        binding.bottomNavView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                setCurrentFragment(homeFragment);
                return true;
            } else if (itemId == R.id.menu) {
                setCurrentFragment(menuFragment);
                return true;
            } else if (itemId == R.id.reservations) {
                setCurrentFragment(reservationsFragment);
                return true;
            } else if (itemId == R.id.discover) {
                setCurrentFragment(discoverFragment);
                return true;
            } else if (itemId == R.id.profile) {
                setCurrentFragment(profileFragment);
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
        GuestInfoApi api = ApiClient.getClient(token).create(GuestInfoApi.class);
        Call<Guest> call = api.getGuestData();
        Log.d("MainActivity fetchUserData()", "Token: " + token);

        call.enqueue(new Callback<Guest>() {
            @Override
            public void onResponse(Call<Guest> call, Response<Guest> response) {
                Log.d("MainActivity fetchUserData()", "Response: " + response);
                if (response.isSuccessful()) {
                    Guest guest = response.body();
                    Guest.init(guest);
                    Log.d("MainActivity fetchUserData()", "Fetch user data: " + guest.getEmail());
                } else {
                    // response.code(): return HTTP status code
                    Log.e("MainActivity fetchUserData()", "Failed to fetch user data: " + response.code() + " " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Guest> call, Throwable t) {
                Log.e("MainActivity fetchUserData()", "Network error: " + t.getMessage());
            }
        });
    }
}