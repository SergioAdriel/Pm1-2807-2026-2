package com.example.subsmanager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.subsmanager.model.Plan;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SubsActivity extends AppCompatActivity {

    private RecyclerView rv;
    private List<Plan> allPlans;
    private LinearLayout brandContainer;
    private String selectedBrand = "Netflix";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subs);

        rv = findViewById(R.id.rvPlans);
        brandContainer = findViewById(R.id.brandContainer);
        rv.setLayoutManager(new LinearLayoutManager(this));

        initData();
        setupBrandFilters();
        filterPlans(selectedBrand);
    }

    private void initData() {
        allPlans = new ArrayList<>();
        allPlans.add(new Plan("Netflix", "Básico con anuncios", "$99 MXN", "720p, 1 dispositivo", 3.5f));
        allPlans.add(new Plan("Netflix", "Estándar", "$219 MXN", "1080p, 2 dispositivos", 4.5f));
        allPlans.add(new Plan("Netflix", "Premium", "$299 MXN", "4K+HDR, 4 dispositivos", 5.0f));
        
        allPlans.add(new Plan("Spotify", "Individual", "$129 MXN", "Sin anuncios, modo offline", 5.0f));
        allPlans.add(new Plan("Spotify", "Duo", "$169 MXN", "2 cuentas premium", 4.5f));
        allPlans.add(new Plan("Spotify", "Familiar", "$199 MXN", "6 cuentas, bloqueo contenido explícito", 4.8f));
        
        allPlans.add(new Plan("Disney+", "Estándar", "$179 MXN", "Hasta 1080p, 2 dispositivos", 4.0f));
        allPlans.add(new Plan("Disney+", "Premium", "$219 MXN", "4K UHD/HDR, 4 dispositivos", 4.7f));
        
        allPlans.add(new Plan("HBO Max", "Mensual", "$179 MXN", "4K, 3 dispositivos", 4.0f));
        
        allPlans.add(new Plan("YouTube", "Premium Individual", "$119 MXN", "Sin anuncios, Music incluido", 4.8f));
        allPlans.add(new Plan("YouTube", "Premium Familiar", "$239 MXN", "Hasta 5 miembros", 4.5f));
    }

    private void setupBrandFilters() {
        List<String> brands = new ArrayList<>();
        brands.add("Netflix");
        brands.add("Spotify");
        brands.add("Disney+");
        brands.add("HBO Max");
        brands.add("YouTube");

        for (String brand : brands) {
            Button btn = new Button(this, null, android.R.attr.buttonStyleSmall);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 16, 0);
            btn.setLayoutParams(params);
            btn.setText(brand);
            btn.setAllCaps(false);
            
            updateButtonStyle(btn, brand.equals(selectedBrand));

            btn.setOnClickListener(v -> {
                selectedBrand = brand;
                filterPlans(brand);
                // Actualizar estilos de todos los botones
                for (int i = 0; i < brandContainer.getChildCount(); i++) {
                    Button b = (Button) brandContainer.getChildAt(i);
                    updateButtonStyle(b, b.getText().toString().equals(selectedBrand));
                }
            });
            brandContainer.addView(btn);
        }
    }

    private void updateButtonStyle(Button btn, boolean isSelected) {
        if (isSelected) {
            btn.setBackgroundColor(Color.parseColor("#2563EB"));
            btn.setTextColor(Color.WHITE);
        } else {
            btn.setBackgroundColor(Color.parseColor("#E5E7EB"));
            btn.setTextColor(Color.BLACK);
        }
    }

    private void filterPlans(String brand) {
        List<Plan> filtered = allPlans.stream()
                .filter(p -> p.service.equals(brand))
                .collect(Collectors.toList());
        rv.setAdapter(new PlanAdapter(filtered));
    }

    private static class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.ViewHolder> {
        private final List<Plan> plans;

        PlanAdapter(List<Plan> plans) {
            this.plans = plans;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Plan plan = plans.get(position);
            holder.tvService.setText(plan.service);
            holder.tvPlanName.setText(plan.planName);
            holder.tvPrice.setText(plan.price);
            holder.tvFeatures.setText(plan.features);
            holder.ratingBar.setRating(plan.rating);
        }

        @Override
        public int getItemCount() {
            return plans.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvService, tvPlanName, tvPrice, tvFeatures;
            RatingBar ratingBar;

            ViewHolder(View itemView) {
                super(itemView);
                tvService = itemView.findViewById(R.id.tvService);
                tvPlanName = itemView.findViewById(R.id.tvPlanName);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvFeatures = itemView.findViewById(R.id.tvFeatures);
                ratingBar = itemView.findViewById(R.id.ratingBar);
            }
        }
    }
}