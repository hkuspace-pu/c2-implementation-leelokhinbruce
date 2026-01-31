package com.example.guestreservationapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.guestreservationapp.databinding.ActivitySpecificMenuBinding;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.accessing_data.MenuApi;
import com.example.restaurant_reservation_lib.adapter.MenuItemAdapter;
import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.example.restaurant_reservation_lib.session_management.SessionManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpecificMenuActivity extends AppCompatActivity {
    private ActivitySpecificMenuBinding binding;
    public static final String MENU_TITLE = "MENU_TITLE";
    private String mealTime;
    private MenuItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecificMenuBinding.inflate(getLayoutInflater());  // create a instance of the binding class
        setContentView(binding.getRoot());  // make it the active view on the screen

        // Get the string from MenuFragment
        mealTime = getIntent().getStringExtra(MENU_TITLE);
        binding.textMenuTitle.setText(mealTime);

        // Close the Activity
        binding.imgBtnBack.setOnClickListener(viewBack -> finish());

        // Set RecycleView
        binding.recycleMenu.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleMenu.setHasFixedSize(true);

        // Assign the menu items to the adapter
        adapter = new MenuItemAdapter(false);  // false - option button is gone
        // Set the adapter instance to the RecycleView to inflate the items
        binding.recycleMenu.setAdapter(adapter);

        // Fetch menu items from server database
        fetchMenuItems();
    }

    private void fetchMenuItems() {
        String token = new SessionManager(getApplicationContext()).getAccessToken();
        MenuApi api = ApiClient.getClient(token).create(MenuApi.class);
        Call<List<MenuItem>> call = api.getAllMenuItems();

        call.enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                Log.d("SpecificMenuActivity", "API code = " + response.code());
                Log.d("SpecificMenuActivity", "API raw = " + response.raw());

                if (response.isSuccessful() && response.body() != null) {
                    // Filter by mealTime
                    List<MenuItem> filtered = new ArrayList<>();
                    for (MenuItem item : response.body()) {
                        if (mealTime.equals(item.getMealTime())) {
                            filtered.add(item);
                        }
                    }
                    // Add filtered items to the adapter
                    adapter.setMenuItems(filtered);
                } else {
                    Log.e("SpecificMenuActivity", "Failed to get menu items: " +
                            response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                Log.e("SpecificMenuActivity", "Network error: " + t.getMessage());
            }
        });
    }
}