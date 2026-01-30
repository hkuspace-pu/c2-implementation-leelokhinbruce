package com.example.adminreservationmanagementapp.database_management;

import android.app.Application;
import android.content.Context;

import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

// Schedule sync safely for SyncWorker via WorkManager
public class SyncHelper {
    // Enqueue a one-time sync task immediately
    public static void enqueueImmediateSync(Context context) {

        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncWorker.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS).build();

        // Schedule the work
        WorkManager.getInstance(context)
                .enqueueUniqueWork(
                        "menu_sync",
                        ExistingWorkPolicy.KEEP,  // Keep the work if it's already enqueued
                        work);  // The work to be executed
    }

    // Call once in Application.onCreate()
    // Schedule a sync every 15 mins
    public static void schedulePeriodicSync() {
        PeriodicWorkRequest periodic = new PeriodicWorkRequest
                .Builder(SyncWorker.class, 15, TimeUnit.MINUTES).build();

        WorkManager.getInstance()
                .enqueueUniquePeriodicWork("menu_periodic_sync", ExistingPeriodicWorkPolicy.KEEP, periodic);
    }
}
