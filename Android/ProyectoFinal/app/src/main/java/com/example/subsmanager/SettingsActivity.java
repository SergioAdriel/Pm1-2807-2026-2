package com.example.subsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    MaterialSwitch switchGeneral;
    MaterialSwitch switchDarkMode;
    MaterialCardView cardCredits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchGeneral = findViewById(R.id.switchGeneral);
        switchDarkMode = findViewById(R.id.switchDarkMode);
        cardCredits = findViewById(R.id.cardCredits);

        // Configurar estado actual del modo oscuro basado en el sistema si no hay preferencia guardada
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        if (currentMode == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM || currentMode == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED) {
            switchDarkMode.setChecked(false); // O podrías detectar si el sistema está en oscuro
        } else {
            switchDarkMode.setChecked(currentMode == AppCompatDelegate.MODE_NIGHT_YES);
        }

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        switchGeneral.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(this, "Notificaciones activadas", Toast.LENGTH_SHORT).show();
                // Notificación de prueba
                com.example.subsmanager.utils.NotificationHelper.showNotification(
                        this,
                        "SubsManager",
                        "¡Las notificaciones están activas!"
                );
            } else {
                Toast.makeText(this, "Notificaciones desactivadas", Toast.LENGTH_SHORT).show();
            }
        });

        cardCredits.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, CreditsActivity.class));
        });
    }
}
