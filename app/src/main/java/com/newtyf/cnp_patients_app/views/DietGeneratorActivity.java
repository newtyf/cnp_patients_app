package com.newtyf.cnp_patients_app.views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.newtyf.cnp_patients_app.R;

public class DietGeneratorActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvPacienteDieta;
    private LinearLayout llSeleccionarPaciente; // NUEVA VARIABLE
    private BottomNavigationView bottomNavigation;

    // Variables para la selección de días
    private LinearLayout dayLu, dayMa, dayMi;
    private TextView tvLuText, tvLuNum, tvMaText, tvMaNum, tvMiText, tvMiNum;

    // Botones de Añadir Comidas
    private TextView btnAddDesayuno, btnAddAlmuerzo, btnAddSnack, btnAddCena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diet_generator);

        // 1. Vincular Vistas Globales
        btnBack = findViewById(R.id.btn_back_dietas);
        tvPacienteDieta = findViewById(R.id.tv_paciente_dieta);
        llSeleccionarPaciente = findViewById(R.id.ll_seleccionar_paciente); // VINCULACIÓN

        // 2. Vincular Botones de Comida
        btnAddDesayuno = findViewById(R.id.btn_add_desayuno);
        btnAddAlmuerzo = findViewById(R.id.btn_add_almuerzo);
        btnAddSnack = findViewById(R.id.btn_add_snack);
        btnAddCena = findViewById(R.id.btn_add_cena);

        // Recibir datos del paciente
        String nombrePaciente = getIntent().getStringExtra("PACIENTE_NOMBRE");
        if (nombrePaciente != null && !nombrePaciente.isEmpty()) {
            tvPacienteDieta.setText(nombrePaciente);
            tvPacienteDieta.setTextColor(Color.parseColor("#1B5E20")); // Verde oscuro
        } else {
            tvPacienteDieta.setText("Seleccione un paciente...");
            tvPacienteDieta.setTextColor(Color.parseColor("#E65100")); // Naranja alerta
        }

        // Acciones
        btnBack.setOnClickListener(v -> finish());

        // --- UX: ABRIR SELECTOR DE PACIENTES ---
        llSeleccionarPaciente.setOnClickListener(v -> abrirSelectorDePacientes());

        // --- UX: ABRIR BUSCADOR CONTEXTUAL ---
        if(btnAddDesayuno != null) btnAddDesayuno.setOnClickListener(v -> abrirBuscadorAlimentos("Añadir a Desayuno"));
        if(btnAddAlmuerzo != null) btnAddAlmuerzo.setOnClickListener(v -> abrirBuscadorAlimentos("Añadir a Almuerzo"));
        if(btnAddSnack != null) btnAddSnack.setOnClickListener(v -> abrirBuscadorAlimentos("Añadir a Snack"));
        if(btnAddCena != null) btnAddCena.setOnClickListener(v -> abrirBuscadorAlimentos("Añadir a Cena"));

        configurarSelectorDias();
        configurarNavegacionInferior();
    }

    // ==========================================
    // MÉTODO PARA EL BOTTOM SHEET DE PACIENTES
    // ==========================================
    private void abrirSelectorDePacientes() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.modal_seleccionar_paciente, null);
        bottomSheetDialog.setContentView(view);

        // Vincular los pacientes de prueba en el modal
        LinearLayout itemPaciente1 = view.findViewById(R.id.item_paciente_1);
        LinearLayout itemPaciente2 = view.findViewById(R.id.item_paciente_2);

        // Si seleccionan a Carlos Mendoza
        itemPaciente1.setOnClickListener(v -> {
            tvPacienteDieta.setText("Carlos Mendoza");
            tvPacienteDieta.setTextColor(Color.parseColor("#1B5E20"));
            bottomSheetDialog.dismiss();
            Toast.makeText(this, "Paciente asignado", Toast.LENGTH_SHORT).show();
        });

        // Si seleccionan a Ana Lucia
        itemPaciente2.setOnClickListener(v -> {
            tvPacienteDieta.setText("Ana Lucia Ramirez");
            tvPacienteDieta.setTextColor(Color.parseColor("#1B5E20"));
            bottomSheetDialog.dismiss();
            Toast.makeText(this, "Paciente asignado", Toast.LENGTH_SHORT).show();
        });

        bottomSheetDialog.show();
    }

    // ==========================================
    // MÉTODO PARA EL BOTTOM SHEET (MODAL DESLIZANTE)
    // ==========================================
    private void abrirBuscadorAlimentos(String comidaContexto) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.modal_buscar_alimento, null);
        bottomSheetDialog.setContentView(view);

        // Personalizar el título dinámicamente
        TextView tvModalTitle = view.findViewById(R.id.tv_modal_meal_title);
        tvModalTitle.setText(comidaContexto);

        // Mostrar el modal
        bottomSheetDialog.show();
    }
    private void configurarSelectorDias() {
        dayLu = findViewById(R.id.day_lu);
        dayMa = findViewById(R.id.day_ma);
        dayMi = findViewById(R.id.day_mi);

        tvLuText = findViewById(R.id.tv_lu_text); tvLuNum = findViewById(R.id.tv_lu_num);
        tvMaText = findViewById(R.id.tv_ma_text); tvMaNum = findViewById(R.id.tv_ma_num);
        tvMiText = findViewById(R.id.tv_mi_text); tvMiNum = findViewById(R.id.tv_mi_num);

        // Simular clic en el Martes
        dayMa.setOnClickListener(v -> seleccionarDia(dayMa, tvMaText, tvMaNum));

        // Simular clic en el Lunes
        dayLu.setOnClickListener(v -> seleccionarDia(dayLu, tvLuText, tvLuNum));

        // Simular clic en el Miércoles
        dayMi.setOnClickListener(v -> seleccionarDia(dayMi, tvMiText, tvMiNum));
    }

    // Método para cambiar el color del día seleccionado (UX)
    private void seleccionarDia(LinearLayout selectedDay, TextView text, TextView num) {
        // 1. Resetear todos a gris (Inactivos)
        resetearDia(dayLu, tvLuText, tvLuNum);
        resetearDia(dayMa, tvMaText, tvMaNum);
        resetearDia(dayMi, tvMiText, tvMiNum);

        // 2. Pintar el seleccionado de verde (Activo)
        selectedDay.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#1B5E20"))); // Tu color primario
        text.setTextColor(Color.WHITE);
        num.setTextColor(Color.WHITE);

        Toast.makeText(this, "Cargando dieta del " + text.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    private void resetearDia(LinearLayout day, TextView text, TextView num) {
        day.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#F5F5F5")));
        text.setTextColor(Color.parseColor("#757575"));
        num.setTextColor(Color.parseColor("#333333"));
    }

    private void configurarNavegacionInferior() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_recetas);
        // ... (Tu misma lógica del Bottom Navigation)
    }
}