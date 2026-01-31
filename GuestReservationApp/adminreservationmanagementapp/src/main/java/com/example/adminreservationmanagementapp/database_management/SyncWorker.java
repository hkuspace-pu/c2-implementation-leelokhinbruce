package com.example.adminreservationmanagementapp.database_management;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.adminreservationmanagementapp.accessing_data.MenuItemDao;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.MenuMapper;
import com.example.restaurant_reservation_lib.accessing_data.MenuApi;
import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.example.restaurant_reservation_lib.request.MenuItemRequest;
import com.example.restaurant_reservation_lib.session_management.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

// The core sync logic which use WorkManager to find pending items
// and process them sequentially with sync Retrofit calls
// Works directly to MenuItem and Reservation Entities
public class SyncWorker extends Worker {

    public SyncWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        MenuItemDao dao = db.menuItemDao();

        String token = new SessionManager(getApplicationContext()).getAccessToken();
        MenuApi api = ApiClient.getClient(token).create(MenuApi.class);  // Safe to call API

        try {
            // Menu items from SQLite -> pending list if syncAction != 0 (NONE)
            // Pending items are used to take action to sync with remote DB
            List<MenuItem> pending = dao.getPendingItemsSync();

            // Push pending changes -> pulls updates (change MySQL -> update SQLite via MySQL data)
            for (MenuItem item : pending) {
                int action = item.getSyncAction();

                // local item -> DTO request
                MenuItemRequest dto = MenuMapper.toDto(item);
                Log.d("SyncWorker", "Local Promotion: " + dto.isAvailable());
                Log.d("SyncWorker", "Server Id: " + item.getServerId());
                Log.d("SyncWorker", "Action: " + action);

                // 1. Verify Sync action in the item to decide how to interact with the server DB
                // 2. Get response from the server after calls API request
                // 3. If success -> update local DB based on the response result
                // CREATE
                if (action == 1) {
                    Response<MenuItemRequest> res = api.addMenuItem(dto).execute();
                    Log.d("SyncWorker", "CREATE Response: " + res.body());

                    if (res.isSuccessful() && res.body() != null) {
                        // Update local DB: set serverId and clear syncAction
                        MenuItemRequest serverItem = res.body();
                        Log.d("SyncWorker", "Server Promotion: " + serverItem.isPromotion());
                        item.setServerId(serverItem.getId());  // Save serverId as sync done
                        item.setSyncAction(0);  // Server confirmed
                        dao.updateItem(item);  // update in local DB
                    } else
                        // server returns error -> log & try later
                        return Result.retry();
                }
                // UPDATE
                else if (action == 2) {
                    Response<MenuItemRequest> res = api.editMenuItem(item.getServerId(), dto).execute();
                    Log.d("SyncWorker", "UPDATE Response: " + res.body());

                    if (res.isSuccessful()) {
                        item.setSyncAction(0);
                        dao.updateItem(item);  // update in local DB
                    } else return Result.retry();
                }
                // DELETE
                else if (action == 3) {
                    Response<Void> res = api.deleteMenuItem(item.getServerId()).execute();
                    Log.d("SyncWorker", "DELETE Response: " + res.body());

                    if (res.isSuccessful()) {
                        dao.deleteItem(item);
                        Log.d("SyncWorker", "Delete menu item from server database");
                    } else {
                        Log.d("SyncWorker", "The menu item not found in the server database");
                    }
                }
            }

            // PULL remote and merge (get MySQL -> SQLite)
            // In case other staff already added/edited/deleted other menu items
            Response<List<MenuItemRequest>> res = api.pullAllMenuItems().execute();

            if (res.isSuccessful() && res.body() != null) {
                for (MenuItemRequest serverItem : res.body()) {
                    // Find whether local DB has the item from MySQL
                    MenuItem localItem = dao.getByServerId(serverItem.getId());

                    if (localItem == null) {
                        // Insert the server items if they don't exist in local DB
                        MenuItem newLocalItem = MenuMapper.fromDto(serverItem);
                        dao.insertItem(newLocalItem);
                    } else if (serverItem.getUpdatedAt().after(localItem.getUpdatedAt())) {
                        // If server updateAt >= local updateAt
                        MenuItem updatedLocalItem = MenuMapper.fromDto(serverItem);
                        updatedLocalItem.setId(localItem.getId());  // keep local PK, avoiding build another item
                        dao.updateItem(updatedLocalItem);
                    }
                }
            }

            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.retry();
        }
    }
}
