package com.example.subsmanager.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "subscriptions")
public class Subscription {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public double cost;
    public String frequency;
    public String nextPaymentDate;

    public Subscription(String name, double cost, String frequency, String nextPaymentDate) {
        this.name = name;
        this.cost = cost;
        this.frequency = frequency;
        this.nextPaymentDate = nextPaymentDate;
    }
}
