package com.example.subsmanager.model;

public class Plan {
    public String service;
    public String planName;
    public String price;
    public String features;
    public float rating;

    public Plan(String service, String planName, String price, String features, float rating) {
        this.service = service;
        this.planName = planName;
        this.price = price;
        this.features = features;
        this.rating = rating;
    }
}