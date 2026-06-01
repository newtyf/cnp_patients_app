package com.newtyf.cnp_patients_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.database.DatabaseHelper;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvSaludo, tvCountPacientes, tvCountConsultas, tvVerTodoCitas;
    private LinearLayout btnAccionNuevoPac, btnAccionDietas, btnAccionNuevaConsulta;
    private BottomNavigationView bottomNavigation;
    private DatabaseHelper dbHelper;

    // Tarjetas
    private MaterialCardView cardProfileImage, cardCita1, cardCita2, cardMetricPacientes, cardMetricConsultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        // Configuración de márgenes para sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);
        inicializarVistas();
        configurarNavegacionInferior();
    }

    private void inicializarVistas() {
        // Textos
        tvSaludo = findViewById(R.id.tvSaludo);
        tvCountPacientes = findViewById(R.id.tvCountPacientes);
        tvCountConsultas = findViewById(R.id.tvCountConsultas);
        tvVerTodoCitas = findViewById(R.id.tvVerTodoCitas);

        // Botones de Acción Rápida
        btnAccionNuevoPac = findViewById(R.id.btnAccionNuevoPac);
        btnAccionDietas = findViewById(R.id.btnAccionDietas);
        btnAccionNuevaConsulta = findViewById(R.id.btnAccionNuevaConsulta);

        // Tarjetas Métricas
        cardMetricPacientes = findViewById(R.id.card_metric_pacientes);
        cardMetricConsultas = findViewById(R.id.card_metric_consultas);

        // Tarjetas Perfil y Citas
        cardProfileImage = findViewById(R.id.card_profile_image);
        cardCita1 = findViewById(R.id.card_cita_1);
        cardCita2 = findViewById(R.id.card_cita_2);

        // --- EVENTOS CLIC ---

        // Métricas (Drill-down)
        cardMetricPacientes.setOnClickListener(v -> startActivity(new Intent(this, PatientListActivity.class)));
        cardMetricConsultas.setOnClickListener(v -> startActivity(new Intent(this, AgendaActivity.class)));

        // Abrir Perfil
        cardProfileImage.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));

        // Acciones Rápidas
        btnAccionNuevoPac.setOnClickListener(v -> startActivity(new Intent(this, PatientCreateActivity.class)));

        // Botón Gestión de Recetas (ANTES DIETAS)
        btnAccionDietas.setOnClickListener(v -> startActivity(new Intent(this, RecipeCreateActivity.class)));

        btnAccionNuevaConsulta.setOnClickListener(v -> startActivity(new Intent(this, PatientListActivity.class)));

        // Citas de hoy
        cardCita1.setOnClickListener(v -> startActivity(new Intent(this, ConsultationActivity.class)));
        cardCita2.setOnClickListener(v -> startActivity(new Intent(this, ConsultationActivity.class)));

        tvVerTodoCitas.setOnClickListener(v -> Toast.makeText(this, "Redirigiendo a Agenda...", Toast.LENGTH_SHORT).show());
    }

    private void configurarNavegacionInferior() {
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_home);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_pacientes) {
                startActivity(new Intent(this, PatientListActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_recetas) { // ID actualizado a RECETAS
                startActivity(new Intent(this, RecipeCreateActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_mapa) {
                Toast.makeText(this, "Mapa en desarrollo", Toast.LENGTH_SHORT).show();
                return false;
            }
            return false;
        });
    }
}