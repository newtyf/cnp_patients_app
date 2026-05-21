package com.newtyf.cnp_patients_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.database.DatabaseHelper;

// Autor: pregunta1
public class DashboardActivity extends AppCompatActivity {

    private TextView tvCountPacientes;
    private TextView tvCountConsultas;

    // Variables actualizadas para las Acciones Rápidas
    private LinearLayout btnAccionNuevoPac;
    private LinearLayout btnAccionCalculadora;
    private LinearLayout btnAccionNuevaConsulta;
    private LinearLayout btnAccionExportar;

    private BottomNavigationView bottomNavigation;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        inicializarVistas();
    }

    private void inicializarVistas() {
        // Enlazar los textos de métricas
        tvCountPacientes = findViewById(R.id.tvCountPacientes);
        tvCountConsultas = findViewById(R.id.tvCountConsultas);

        // Enlazar los nuevos botones de Acciones Rápidas
        btnAccionNuevoPac = findViewById(R.id.btnAccionNuevoPac);
        btnAccionCalculadora = findViewById(R.id.btnAccionCalculadora);
        btnAccionNuevaConsulta = findViewById(R.id.btnAccionNuevaConsulta);
        btnAccionExportar = findViewById(R.id.btnAccionExportar);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Flujos de navegación y acciones de click
        btnAccionNuevoPac.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, PatientCreateActivity.class);
            startActivity(intent);
        });

        btnAccionCalculadora.setOnClickListener(v -> {
            // TODO: Agregar el Intent hacia la Calculadora de Energía al implementarla
        });

        btnAccionNuevaConsulta.setOnClickListener(v -> {
            // Redirige al listado de pacientes para seleccionar a quién hacerle la consulta
            Intent intent = new Intent(DashboardActivity.this, PatientListActivity.class);
            startActivity(intent);
        });

        btnAccionExportar.setOnClickListener(v -> {
            // TODO: Agregar la lógica para Exportar Dieta a PDF
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Carga automática y reactiva de los indicadores cuantitativos
        cargarDatosResumenSQLite();
    }

    private void cargarDatosResumenSQLite() {
        if (tvCountPacientes != null) {
            int totalPacientes = dbHelper.getCantidadPacientesActivos();
            tvCountPacientes.setText(String.valueOf(totalPacientes));
        }

        if (tvCountConsultas != null) {
            int consultasMes = dbHelper.getCantidadConsultasDelMes();
            tvCountConsultas.setText(String.valueOf(consultasMes));
        }
    }
}