package com.example.subsmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    LinearLayout containerSubs;

    Button addButton;
    Button creditsButton;

    BottomNavigationView bottomNav;

    TextView resumenTxt;

    int total = 0;

    String fechaSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        containerSubs = findViewById(R.id.containerSubs);

        addButton = findViewById(R.id.addButton);

        creditsButton = findViewById(R.id.creditsButton);

        resumenTxt = findViewById(R.id.resumenTxt);

        bottomNav = findViewById(R.id.bottomNav);

        addButton.setOnClickListener(v -> {

            mostrarDialogo();

        });

        creditsButton.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            CreditsActivity.class
                    );

            startActivity(intent);

        });

        bottomNav.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.home) {

                Toast.makeText(
                        this,
                        "Inicio",
                        Toast.LENGTH_SHORT
                ).show();

                return true;

            }

            if(item.getItemId() == R.id.subs) {

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                SubsActivity.class
                        );

                startActivity(intent);

                return true;

            }

            if(item.getItemId() == R.id.settings) {

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                SettingsActivity.class
                        );

                startActivity(intent);

                return true;

            }

            return false;

        });

    }

    private void mostrarDialogo() {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Nueva Subscripción");

        LinearLayout layout =
                new LinearLayout(this);

        layout.setOrientation(LinearLayout.VERTICAL);

        layout.setPadding(40,40,40,40);

        EditText nombre =
                new EditText(this);

        nombre.setHint("Nombre del servicio");

        layout.addView(nombre);

        EditText costo =
                new EditText(this);

        costo.setHint("Costo");

        costo.setInputType(InputType.TYPE_CLASS_NUMBER);

        layout.addView(costo);

        Spinner frecuencia =
                new Spinner(this);

        String[] opciones = {
                "Semanal",
                "Mensual",
                "Anual"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        opciones
                );

        frecuencia.setAdapter(adapter);

        layout.addView(frecuencia);

        Button fechaBtn =
                new Button(this);

        fechaBtn.setText("Seleccionar Fecha");

        layout.addView(fechaBtn);

        fechaBtn.setOnClickListener(v -> {

            DatePickerDialog picker =
                    new DatePickerDialog(this);

            picker.setOnDateSetListener((view, year, month, day) -> {

                fechaSeleccionada =
                        day + "/" + (month + 1) + "/" + year;

                fechaBtn.setText(fechaSeleccionada);

            });

            picker.show();

        });

        builder.setView(layout);

        builder.setPositiveButton("Agregar",
                (dialog, which) -> {

                    if(nombre.getText().toString().isEmpty()
                            || costo.getText().toString().isEmpty()) {

                        Toast.makeText(
                                this,
                                "Completa todos los campos",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    agregarCard(
                            nombre.getText().toString(),
                            costo.getText().toString(),
                            frecuencia.getSelectedItem().toString(),
                            fechaSeleccionada
                    );

                });

        builder.setNegativeButton("Cancelar", null);

        builder.show();

    }

    private void agregarCard(
            String nombre,
            String costo,
            String frecuencia,
            String fecha
    ) {

        CardView card =
                new CardView(this);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        params.setMargins(0,20,0,0);

        card.setLayoutParams(params);

        card.setRadius(25);

        card.setCardElevation(10);

        card.setCardBackgroundColor(0xFFFFFFFF);

        LinearLayout inside =
                new LinearLayout(this);

        inside.setOrientation(LinearLayout.VERTICAL);

        inside.setPadding(40,40,40,40);

        TextView titulo =
                new TextView(this);

        titulo.setText("🔥 " + nombre);

        titulo.setTextSize(22);

        titulo.setGravity(Gravity.START);

        titulo.setPadding(0,0,0,10);

        TextView precio =
                new TextView(this);

        precio.setText("$" + costo + " MXN");

        precio.setTextSize(18);

        TextView frecuenciaTxt =
                new TextView(this);

        frecuenciaTxt.setText("🔁 Cobro: " + frecuencia);

        frecuenciaTxt.setTextSize(16);

        TextView fechaTxt =
                new TextView(this);

        fechaTxt.setText("📅 Próximo cobro: " + fecha);

        fechaTxt.setTextSize(16);

        Button editar =
                new Button(this);

        editar.setText("Editar");

        editar.setBackgroundColor(0xFF2563EB);

        editar.setTextColor(0xFFFFFFFF);

        Button eliminar =
                new Button(this);

        eliminar.setText("Eliminar");

        eliminar.setBackgroundColor(0xFFDC2626);

        eliminar.setTextColor(0xFFFFFFFF);

        editar.setOnClickListener(v -> {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);

            builder.setTitle("Editar Subscripción");

            LinearLayout layout =
                    new LinearLayout(this);

            layout.setOrientation(LinearLayout.VERTICAL);

            layout.setPadding(40,40,40,40);

            EditText nuevoNombre =
                    new EditText(this);

            nuevoNombre.setText(nombre);

            layout.addView(nuevoNombre);

            EditText nuevoCosto =
                    new EditText(this);

            nuevoCosto.setText(costo);

            nuevoCosto.setInputType(InputType.TYPE_CLASS_NUMBER);

            layout.addView(nuevoCosto);

            builder.setView(layout);

            builder.setPositiveButton("Guardar",
                    (dialog, which) -> {

                        titulo.setText(
                                "🔥 " +
                                        nuevoNombre.getText().toString()
                        );

                        precio.setText(
                                "$" +
                                        nuevoCosto.getText().toString()
                                        + " MXN"
                        );

                        Toast.makeText(
                                this,
                                "Subscripción actualizada",
                                Toast.LENGTH_SHORT
                        ).show();

                    });

            builder.show();

        });

        eliminar.setOnClickListener(v -> {

            containerSubs.removeView(card);

            total -= Integer.parseInt(costo);

            resumenTxt.setText(
                    "Próximo cobro total: $" +
                            total +
                            " MXN"
            );

        });

        inside.addView(titulo);

        inside.addView(precio);

        inside.addView(frecuenciaTxt);

        inside.addView(fechaTxt);

        inside.addView(editar);

        inside.addView(eliminar);

        card.addView(inside);

        containerSubs.addView(card);

        total += Integer.parseInt(costo);

        resumenTxt.setText(
                "Próximo cobro total: $" +
                        total +
                        " MXN"
        );

    }

}

