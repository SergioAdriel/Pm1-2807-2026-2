package com.example.subsmanager;

import android.app.Application;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.subsmanager.worker.PaymentReminderWorker;

import java.util.concurrent.TimeUnit;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Programar recordatorios cada 24 horas
        PeriodicWorkRequest reminderRequest =
                new PeriodicWorkRequest.Builder(PaymentReminderWorker.class, 24, TimeUnit.HOURS)
                        .build();

        WorkManager.getInstance(this).enqueue(reminderRequest);
    }
}