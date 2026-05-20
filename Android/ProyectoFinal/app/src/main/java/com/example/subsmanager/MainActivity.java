package com.example.subsmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    LinearLayout containerSubs;

    Button addButton;
    Button creditsButton;

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

        nombre.setHint("Nombre");

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

        LinearLayout inside =
                new LinearLayout(this);

        inside.setOrientation(LinearLayout.VERTICAL);

        inside.setPadding(40,40,40,40);

        TextView titulo =
                new TextView(this);

        titulo.setText("🔥 " + nombre);

        titulo.setTextSize(22);

        TextView precio =
                new TextView(this);

        precio.setText("$" + costo + " MXN");

        precio.setTextSize(18);

        TextView frecuenciaTxt =
                new TextView(this);

        frecuenciaTxt.setText("🔁 " + frecuencia);

        frecuenciaTxt.setTextSize(16);

        TextView fechaTxt =
                new TextView(this);

        fechaTxt.setText("📅 Próximo cobro: " + fecha);

        fechaTxt.setTextSize(16);

        Button eliminar =
                new Button(this);

        eliminar.setText("Eliminar");

        eliminar.setOnClickListener(v -> {

            containerSubs.removeView(card);

        });

        inside.addView(titulo);

        inside.addView(precio);

        inside.addView(frecuenciaTxt);

        inside.addView(fechaTxt);

        inside.addView(eliminar);

        card.addView(inside);

        containerSubs.addView(card);

        if(!costo.isEmpty()) {

            total += Integer.parseInt(costo);

            resumenTxt.setText(
                    "Próximo cobro total: $" +
                            total +
                            " MXN"
            );

        }

    }

}