package com.example.subsmanager.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.subsmanager.model.Subscription;

@Database(entities = {Subscription.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract SubscriptionDao subscriptionDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "subs_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}