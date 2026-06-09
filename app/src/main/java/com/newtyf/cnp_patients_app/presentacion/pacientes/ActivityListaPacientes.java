package com.newtyf.cnp_patients_app.presentacion.pacientes; // Ajusta a tu paquete correcto

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.BaseDatosCnp;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadPaciente;
import com.newtyf.cnp_patients_app.presentacion.pacientes.PacienteActivoAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityListaPacientes extends AppCompatActivity {

    private RecyclerView rvPacientes;
    private TextInputEditText etBuscador;
    private FloatingActionButton fabNuevoPaciente;
    private MaterialToolbar toolbar;

    private PacienteActivoAdapter adaptador;
    private BaseDatosCnp dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pacientes);

        dbHelper = BaseDatosCnp.obtenerBaseDatos(this);

        inicializarVistas();
        configurarToolbar();
        configurarRecyclerView();
        configurarBuscador();
        configurarFab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cargar datos en onResume garantiza que si agregas un paciente nuevo y vuelves, la lista se actualice
        cargarPacientesDeBD();
    }

    private void inicializarVistas() {
        toolbar = findViewById(R.id.toolbar_pacientes);
        rvPacientes = findViewById(R.id.rv_lista_pacientes);
        etBuscador = findViewById(R.id.et_buscar_paciente);
        fabNuevoPaciente = findViewById(R.id.fab_nuevo_paciente);
    }

    private void configurarToolbar() {
        toolbar.setNavigationOnClickListener(v -> finish()); // Regresa al Dashboard
    }

    private void configurarRecyclerView() {
        // Inicializamos el adaptador con una lista vacía para evitar nulls
        adaptador = new PacienteActivoAdapter(this, new ArrayList<>());
        rvPacientes.setLayoutManager(new LinearLayoutManager(this));
        rvPacientes.setAdapter(adaptador);

        // Ocultar teclado si el usuario hace scroll en la lista (Mejora UX)
        rvPacientes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    ocultarTeclado();
                }
            }
        });
    }

    private void configurarBuscador() {
        etBuscador.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (adaptador != null && etBuscador.getText() != null) {
                    String busqueda = etBuscador.getText().toString();
                    adaptador.getFilter().filter(busqueda);
                    ocultarTeclado();
                }
                return true;
            }
            return false;
        });
    }

    private void configurarFab() {
        fabNuevoPaciente.setOnClickListener(v -> {
            // TODO: Crear y lanzar tu Activity/Fragmento de Nuevo Paciente
            // startActivity(new Intent(this, ActivityNuevoPaciente.class));
        });
    }

    private void cargarPacientesDeBD() {
        BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
            List<EntidadPaciente> listaPacientes = dbHelper.daoPaciente().obtenerTodos();

            runOnUiThread(() -> {
                if (listaPacientes != null && adaptador != null) {
                    adaptador.actualizarDatos(listaPacientes);
                    // Si hay una búsqueda activa, la re-aplicamos sobre los nuevos datos
                    if (etBuscador.getText() != null && !etBuscador.getText().toString().isEmpty()) {
                        adaptador.getFilter().filter(etBuscador.getText().toString());
                    }
                }
            });
        });
    }

    private void ocultarTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}