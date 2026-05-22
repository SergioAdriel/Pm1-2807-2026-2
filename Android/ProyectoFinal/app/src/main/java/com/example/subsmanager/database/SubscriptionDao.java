package com.example.subsmanager.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.subsmanager.model.Subscription;

import java.util.List;

@Dao
public interface SubscriptionDao {
    @Query("SELECT * FROM subscriptions")
    List<Subscription> getAll();

    @Insert
    void insert(Subscription subscription);

    @Update
    void update(Subscription subscription);

    @Delete
    void delete(Subscription subscription);
}