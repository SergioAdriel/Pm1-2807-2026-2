package com.example.subsmanager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.subsmanager.database.AppDatabase;
import com.example.subsmanager.model.Subscription;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    LinearLayout containerSubs;
    Button addButton;
    BottomNavigationView bottomNav;
    TextView resumenTxt;
    PieChart pieChart;

    double total = 0;
    String fechaSeleccionada = "";
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getInstance(this);

        containerSubs = findViewById(R.id.containerSubs);
        addButton = findViewById(R.id.addButton);
        resumenTxt = findViewById(R.id.resumenTxt);
        bottomNav = findViewById(R.id.bottomNav);
        pieChart = findViewById(R.id.pieChart);

        requestNotificationPermission();
        loadSubscriptions();

        addButton.setOnClickListener(v -> mostrarDialogo(null));

        setupNavigation();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void loadSubscriptions() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Subscription> subs = db.subscriptionDao().getAll();
            runOnUiThread(() -> {
                containerSubs.removeAllViews();
                total = 0;
                ArrayList<PieEntry> entries = new ArrayList<>();
                for (Subscription s : subs) {
                    agregarCard(s);
                    entries.add(new PieEntry((float) s.cost, s.name));
                }
                actualizarResumen();
                updateChart(entries);
            });
        });
    }

    private void updateChart(ArrayList<PieEntry> entries) {
        if (entries.isEmpty()) {
            pieChart.setVisibility(android.view.View.GONE);
            return;
        }
        pieChart.setVisibility(android.view.View.VISIBLE);
        PieDataSet dataSet = new PieDataSet(entries, "Suscripciones");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(12f);
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Gastos");
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void setupNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                loadSubscriptions();
                return true;
            } else if (id == R.id.subs) {
                startActivity(new Intent(MainActivity.this, SubsActivity.class));
                return true;
            } else if (id == R.id.settings) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }

    private void mostrarDialogo(Subscription existingSub) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(existingSub == null ? "Nueva Subscripción" : "Editar Subscripción");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 40);

        EditText nombre = new EditText(this);
        nombre.setHint("Nombre (Netflix, Spotify...)");
        if (existingSub != null) nombre.setText(existingSub.name);
        layout.addView(nombre);

        EditText costo = new EditText(this);
        costo.setHint("Costo");
        costo.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (existingSub != null) costo.setText(String.valueOf(existingSub.cost));
        layout.addView(costo);

        Spinner frecuencia = new Spinner(this);
        String[] opciones = {"Mensual", "Anual", "Semanal"};
        frecuencia.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, opciones));
        if (existingSub != null) {
            for (int i = 0; i < opciones.length; i++) {
                if (opciones[i].equals(existingSub.frequency)) frecuencia.setSelection(i);
            }
        }
        layout.addView(frecuencia);

        Button fechaBtn = new Button(this);
        fechaBtn.setText(existingSub == null ? "Fecha de Pago" : existingSub.nextPaymentDate);
        fechaSeleccionada = existingSub == null ? "" : existingSub.nextPaymentDate;
        layout.addView(fechaBtn);

        fechaBtn.setOnClickListener(v -> {
            DatePickerDialog picker = new DatePickerDialog(this);
            picker.setOnDateSetListener((view, year, month, day) -> {
                fechaSeleccionada = day + "/" + (month + 1) + "/" + year;
                fechaBtn.setText(fechaSeleccionada);
            });
            picker.show();
        });

        builder.setView(layout);
        builder.setPositiveButton(existingSub == null ? "Agregar" : "Guardar", null); // Set null here to override later
        AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(view -> {
            String name = nombre.getText().toString().trim();
            String priceStr = costo.getText().toString().trim();

            if (name.isEmpty()) {
                nombre.setError("El nombre es obligatorio");
                return;
            }
            if (priceStr.isEmpty()) {
                costo.setError("El costo es obligatorio");
                return;
            }
            if (fechaSeleccionada.isEmpty()) {
                Toast.makeText(this, "Selecciona una fecha de inicio", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            String freq = frecuencia.getSelectedItem().toString();
            String calculada = calcularSiguienteFecha(fechaSeleccionada, freq);

            if (existingSub == null) {
                saveSubscription(new Subscription(name, price, freq, calculada));
            } else {
                existingSub.name = name;
                existingSub.cost = price;
                existingSub.frequency = freq;
                existingSub.nextPaymentDate = calculada;
                updateSubscription(existingSub);
            }
            dialog.dismiss();
        });
    }

    private String calcularSiguienteFecha(String fechaInicio, String frecuencia) {
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
        try {
            Date date = sdf.parse(fechaInicio);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            switch (frecuencia) {
                case "Semanal":
                    cal.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                case "Mensual":
                    cal.add(Calendar.MONTH, 1);
                    break;
                case "Anual":
                    cal.add(Calendar.YEAR, 1);
                    break;
            }
            return sdf.format(cal.getTime());
        } catch (ParseException e) {
            return fechaInicio;
        }
    }

    private void saveSubscription(Subscription sub) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.subscriptionDao().insert(sub);
            loadSubscriptions();
        });
    }

    private void updateSubscription(Subscription sub) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.subscriptionDao().update(sub);
            loadSubscriptions();
        });
    }

    private void deleteSubscription(Subscription sub) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.subscriptionDao().delete(sub);
            loadSubscriptions();
        });
    }

    private void agregarCard(Subscription sub) {
        MaterialCardView card = new MaterialCardView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 32);
        card.setLayoutParams(params);
        card.setRadius(48f);
        card.setCardElevation(0f);
        card.setStrokeWidth(2);
        card.setStrokeColor(ContextCompat.getColor(this, R.color.divider));
        card.setCardBackgroundColor(ContextCompat.getColor(this, R.color.surface));

        LinearLayout inside = new LinearLayout(this);
        inside.setOrientation(LinearLayout.VERTICAL);
        inside.setPadding(48, 48, 48, 48);

        TextView titulo = new TextView(this);
        titulo.setText(sub.name);
        titulo.setTextSize(20);
        titulo.setTypeface(null, android.graphics.Typeface.BOLD);
        titulo.setTextColor(ContextCompat.getColor(this, R.color.on_surface));

        TextView info = new TextView(this);
        info.setText("$" + sub.cost + " • " + sub.frequency);
        info.setTextSize(16);
        info.setTextColor(ContextCompat.getColor(this, R.color.primary));
        info.setPadding(0, 8, 0, 8);

        TextView fecha = new TextView(this);
        fecha.setText("Próximo pago: " + sub.nextPaymentDate);
        fecha.setTextColor(ContextCompat.getColor(this, R.color.text_secondary));
        fecha.setTextSize(14);

        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        buttons.setGravity(Gravity.END);
        buttons.setPadding(0, 16, 0, 0);

        Button edit = new Button(this, null, com.google.android.material.R.attr.materialButtonStyle);
        edit.setText("Editar");
        edit.setAllCaps(false);
        edit.setOnClickListener(v -> mostrarDialogo(sub));

        Button delete = new Button(this, null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
        delete.setText("Borrar");
        delete.setAllCaps(false);
        delete.setTextColor(ContextCompat.getColor(this, R.color.error));
        delete.setOnClickListener(v -> deleteSubscription(sub));

        buttons.addView(edit);
        buttons.addView(delete);

        inside.addView(titulo);
        inside.addView(info);
        inside.addView(fecha);
        inside.addView(buttons);
        card.addView(inside);
        containerSubs.addView(card);

        total += sub.cost;
    }

    private void actualizarResumen() {
        resumenTxt.setText(String.format("Gasto Mensual: $%.2f MXN", total));
    }
}