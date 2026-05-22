package com.example.subsmanager.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.subsmanager.database.AppDatabase;
import com.example.subsmanager.model.Subscription;
import com.example.subsmanager.utils.NotificationHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentReminderWorker extends Worker {

    public PaymentReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        List<Subscription> subs = db.subscriptionDao().getAll();

        String today = new SimpleDateFormat("d/M/yyyy", Locale.getDefault()).format(new Date());

        for (Subscription sub : subs) {
            if (today.equals(sub.nextPaymentDate)) {
                NotificationHelper.showNotification(
                        getApplicationContext(),
                        "Recordatorio de Pago: " + sub.name,
                        "Hoy vence tu suscripción de $" + sub.cost
                );
            }
        }

        return Result.success();
    }
}