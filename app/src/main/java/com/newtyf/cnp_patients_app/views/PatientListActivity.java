package com.newtyf.cnp_patients_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newtyf.cnp_patients_app.R;

public class PatientListActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fabAddPatient;
    private com.google.android.material.button.MaterialButton btnNuevaConsultaPac1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Configurar FAB para ir al Formulario de Nuevo Paciente
        fabAddPatient = findViewById(R.id.fab_add_patient);
        fabAddPatient.setOnClickListener(v -> {
            Intent intent = new Intent(PatientListActivity.this, PatientCreateActivity.class);
            startActivity(intent);
        });

        btnNuevaConsultaPac1 = findViewById(R.id.btn_nueva_consulta_pac1);
        btnNuevaConsultaPac1.setOnClickListener(v -> {
            Intent intent = new Intent(PatientListActivity.this, ConsultationActivity.class);
            startActivity(intent);
        });

        // 2. Configurar el Menú Inferior (Bottom Navigation)
        configurarNavegacionInferior();
    }

    private void configurarNavegacionInferior() {
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Marcamos la pestaña "Consultas" (nav_pacientes) como activa
        bottomNavigation.setSelectedItemId(R.id.nav_pacientes);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_pacientes) {
                return true; // Ya estamos en esta pantalla
            } else if (itemId == R.id.nav_home) {
                // Volver al Dashboard
                startActivity(new Intent(PatientListActivity.this, DashboardActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_recetas) {
                Toast.makeText(this, "Módulo de recetas en desarrollo", Toast.LENGTH_SHORT).show();
                return false;
            } else if (itemId == R.id.nav_mapa) {
                Toast.makeText(this, "Mapa en desarrollo", Toast.LENGTH_SHORT).show();
                return false;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Aseguramos que la pestaña inferior siga marcada si volvemos a esta vista
        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_pacientes);
        }
    }
}