package com.example.subsmanager;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    Switch switchGeneral;
    Switch switchNetflix;
    Switch switchSpotify;
    Switch switchDisney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        switchGeneral = findViewById(R.id.switchGeneral);


        switchGeneral.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(isChecked) {

                Toast.makeText(
                        this,
                        "Notificaciones activadas",
                        Toast.LENGTH_SHORT
                ).show();

            } else {

                Toast.makeText(
                        this,
                        "Notificaciones desactivadas",
                        Toast.LENGTH_SHORT
                ).show();

            }

        });

    }

}