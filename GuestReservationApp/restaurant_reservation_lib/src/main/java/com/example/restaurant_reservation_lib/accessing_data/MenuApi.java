package com.example.restaurant_reservation_lib.accessing_data;

import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.example.restaurant_reservation_lib.request.MenuItemRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MenuApi {
    @GET("/api/menu")
    Call<List<MenuItemRequest>> pullAllMenuItems();

    // For Testing
    @GET("/api/menu")
    Call<List<MenuItem>> getAllMenuItems();

    @GET("/api/menu/{mealTime}")
    Call<List<MenuItem>> getMenuItemsWithMealTime(@Path("mealTime") String mealTime);

    @POST("/api/menu")
    Call<MenuItemRequest> addMenuItem(@Body MenuItemRequest dto);

    @PUT("/api/menu/{id}")
    Call<MenuItemRequest> editMenuItem(@Path("id") long id, @Body MenuItemRequest dto);

    @DELETE("/api/menu/{id}")
    Call<Void> deleteMenuItem(@Path("id") long id);
}
