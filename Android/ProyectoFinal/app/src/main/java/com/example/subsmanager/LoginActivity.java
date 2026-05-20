package com.example.subsmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(v -> {

            Intent intent =
                    new Intent(LoginActivity.this,
                            MainActivity.class);

            startActivity(intent);

        });

    }
}