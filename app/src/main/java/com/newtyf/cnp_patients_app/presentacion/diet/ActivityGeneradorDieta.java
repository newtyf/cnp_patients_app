package com.newtyf.cnp_patients_app.presentacion.diet;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.presentacion.consultations.DialogoSeleccionarPaciente;

public class ActivityGeneradorDieta extends AppCompatActivity {

    // Vistas del encabezado (Paciente)
    private MaterialCardView cardPaciente;
    private TextView tvNombrePaciente;

    // Vistas del Resumen de Macros
    private TextView tvTotalKcal, tvTotalProt, tvTotalCarb, tvTotalFat;

    // Lista escalable de comidas
    private RecyclerView rvComidas;

    // Controles
    private MaterialButton btnGuardar;
    private String idPacienteSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Renderizamos el nuevo layout Material 3
        setContentView(R.layout.activity_generador_dieta);

        inicializarVistas();
        configurarToolbar();
        configurarEventos();
        configurarRecyclerView();
    }

    private void inicializarVistas() {
        cardPaciente = findViewById(R.id.card_paciente_dieta);
        tvNombrePaciente = findViewById(R.id.tv_nombre_paciente_dieta);

        tvTotalKcal = findViewById(R.id.tv_total_kcal);
        tvTotalProt = findViewById(R.id.tv_total_prot);
        tvTotalCarb = findViewById(R.id.tv_total_carb);
        tvTotalFat = findViewById(R.id.tv_total_fat);

        rvComidas = findViewById(R.id.rv_comidas_dieta);
        btnGuardar = findViewById(R.id.btn_guardar_plan);
    }

    private void configurarToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar_dieta);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    private void configurarEventos() {
        // Reutilizamos la lógica de UX: la tarjeta entera es un botón
        cardPaciente.setOnClickListener(v -> abrirBuscadorPacientes());

        btnGuardar.setOnClickListener(v -> {
            if (idPacienteSeleccionado == null) {
                Toast.makeText(this, getString(R.string.error_seleccionar_paciente), Toast.LENGTH_SHORT).show();
                return;
            }
            // TODO: Integración con Room para guardar EntidadDieta
            Toast.makeText(this, getString(R.string.msg_plan_guardado), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void configurarRecyclerView() {
        // Preparamos el lienzo para inyectar las comidas dinámicamente
        if (rvComidas != null) {
            rvComidas.setLayoutManager(new LinearLayoutManager(this));
            rvComidas.setHasFixedSize(true);
            // El adaptador (DietDayAdapter) será inyectado en la siguiente fase
        }
    }

    private void abrirBuscadorPacientes() {
        // Reutilizamos el componente exacto del módulo de Consultas (Principio DRY)
        DialogoSeleccionarPaciente dialogo = new DialogoSeleccionarPaciente();
        dialogo.setListener(paciente -> {
            idPacienteSeleccionado = paciente.id;
            tvNombrePaciente.setText(paciente.getNombreCompleto());
            dialogo.dismiss();
        });
        dialogo.show(getSupportFragmentManager(), "DialogoBusquedaPacientesDieta");
    }

    /**
     * Este método será invocado por el Adaptador del RecyclerView
     * cada vez que se presione el botón "+ Añadir Alimento" en una comida específica.
     */
    public void mostrarBuscadorAlimentos(String tipoComida) {
        // Validar que haya un paciente seleccionado antes de añadir comida
        if (idPacienteSeleccionado == null) {
            Toast.makeText(this, getString(R.string.error_seleccionar_paciente), Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: En la próxima iteración, aquí abriremos el DialogoBuscarAlimento
        String mensaje = getString(R.string.buscando_alimento_para, tipoComida);
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}