package com.newtyf.cnp_patients_app.presentacion.pacientes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.BaseDatosCnp;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadPaciente;

public class ActivityDetallePaciente extends AppCompatActivity {

    private String patientId;
    private BaseDatosCnp dbHelper;
    private TextView tvNombre, tvDatos, tvEmail, tvTelefono;
    private MaterialButton btnEditar, btnEliminar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_paciente);

        dbHelper = BaseDatosCnp.obtenerBaseDatos(this);
        patientId = getIntent().getStringExtra("patient_id");

        initViews();
        setupToolbar();

        if (patientId == null) {
            Toast.makeText(this, "Error al cargar el paciente", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnEditar.setOnClickListener(v -> abrirEdicion());
        btnEliminar.setOnClickListener(v -> mostrarDialogoEliminar());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosPaciente();
    }

    private void initViews() {
        tvNombre = findViewById(R.id.tv_detail_nombre);
        tvDatos = findViewById(R.id.tv_detail_datos);
        tvEmail = findViewById(R.id.tv_detail_email);
        tvTelefono = findViewById(R.id.tv_detail_telefono);
        btnEditar = findViewById(R.id.btn_detail_editar);
        btnEliminar = findViewById(R.id.btn_detail_eliminar);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar_detail);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void cargarDatosPaciente() {
        BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
            EntidadPaciente p = dbHelper.daoPaciente().obtenerPacientePorId(patientId);
            if (p != null) {
                runOnUiThread(() -> {
                    tvNombre.setText(p.getNombreCompleto());
                    String datosStr = "DNI: " + p.idNumber + "  |  Género: " + p.gender;
                    tvDatos.setText(datosStr);
                    tvEmail.setText(p.email);
                    tvTelefono.setText(p.phone);
                });
            }
        });
    }

    private void abrirEdicion() {
        Intent intent = new Intent(this, ActivityCrearPaciente.class);
        intent.putExtra("patient_id", patientId);
        startActivity(intent);
    }

    private void mostrarDialogoEliminar() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Eliminar Paciente")
                .setMessage("¿Estás seguro de que deseas eliminar este paciente? Esta acción no se puede deshacer.")
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
                        EntidadPaciente p = dbHelper.daoPaciente().obtenerPacientePorId(patientId);
                        if (p != null) {
                            dbHelper.daoPaciente().eliminarPaciente(p);
                            runOnUiThread(() -> {
                                Toast.makeText(ActivityDetallePaciente.this, "Paciente eliminado", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }
                    });
                })
                .show();
    }
}