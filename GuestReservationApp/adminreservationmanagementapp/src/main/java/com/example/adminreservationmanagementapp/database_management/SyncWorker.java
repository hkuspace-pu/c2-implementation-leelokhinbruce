package com.example.adminreservationmanagementapp.database_management;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.adminreservationmanagementapp.accessing_data.MenuItemDao;
import com.example.restaurant_reservation_lib.ApiClient;
import com.example.restaurant_reservation_lib.accessing_data.MenuApi;
import com.example.restaurant_reservation_lib.entity.MenuItem;
import com.example.restaurant_reservation_lib.request.MenuItemRequest;
import com.example.restaurant_reservation_lib.session_management.SessionManager;

import java.util.List;

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

        // Store all menu items from Local Database to pending list if syncAction != 0 (NONE)
        // Pending items are used to take action to sync with remote DB
        List<MenuItem> pending = dao.getPendingItemsSync();

        String token = new SessionManager().getAccessToken();
        MenuApi api = ApiClient.getClient(token).create(MenuApi.class);

        try {
            // Push changes -> pulls updates (change MySQL -> update SQLite via MySQL data)
            for (MenuItem item : pending) {
                int action = item.getSyncAction();

                // 1. Verify Sync action in the item to decide how to interact with the server DB
                // 2. Get response from the server after calls API request
                // 3. If success -> update local DB based on the response result
                if (action == 1) {  // CREATE
                    Response<MenuItem> res = api.addMenuItem(item).execute();

                    if (res.isSuccessful() && res.body() != null) {
                        // Update local DB: set serverId and clear syncAction
                        MenuItem serverItem = res.body();
                        item.setId(serverItem.getServerId());
                        item.setSyncAction(0);  // Server confirmed
                        dao.updateItem(item);  // update in local DB
                    } else
                        // server returns error -> log & try later
                        return Result.retry();
                }
                else if (action == 2) {  // UPDATE
                    Response<MenuItem> res = api.editMenuItem(item.getServerId(), item).execute();

                    if (res.isSuccessful()) {
                        item.setSyncAction(0);
                        dao.updateItem(item);  // update in local DB
                    } else return Result.retry();
                } else if (action == 3) {  // DELETE
                    Response<Void> res = api.deleteMenuItem(item.getServerId()).execute();

                    if (res.isSuccessful()) {
                        dao.deleteItem(item);  // delete in local DB
                    } else return Result.retry();
                }
            }

            // PULL remote changes (get MySQL -> SQLite)
            Response<List<MenuItem>> res = api.getAllMenuItems().execute();

            if (res.isSuccessful() && res.body() != null) {
                for (MenuItem serverItem : res.body()) {
                        MenuItem localItem = dao.getByServerId(serverItem.getServerId());

                    if (localItem == null) {
                        // Insert the server item if it doesn't exist in local DB
                        dao.insertItem(serverItem);
                    } else if (serverItem.getUpdatedAt().after(localItem.getUpdatedAt())) {
                        // Update the local item which is newer than server item
                        serverItem.setId(localItem.getId());  // Set server ID
                        dao.updateItem(serverItem);
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
