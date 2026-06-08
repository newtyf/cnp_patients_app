package com.newtyf.cnp_patients_app.presentacion.pacientes;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.BaseDatosCnp;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadPaciente;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class ActivityCrearPaciente extends AppCompatActivity {

    private TextInputEditText etNombres, etApellidos, etDni, etGenero, etEmail, etTelefono, etFecha;
    private TextInputLayout tilNombres, tilApellidos, tilDni;
    private MaterialButton btnGuardar;
    private MaterialToolbar toolbar;
    private BaseDatosCnp dbHelper;

    private String currentPatientId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_paciente);

        dbHelper = BaseDatosCnp.obtenerBaseDatos(this);
        initViews();
        setupToolbar();

        btnGuardar.setOnClickListener(v -> guardarPaciente());
        etFecha.setOnClickListener(v -> mostrarCalendario());

        if (getIntent().hasExtra("patient_id")) {
            currentPatientId = getIntent().getStringExtra("patient_id");
            cargarDatosParaEdicion(currentPatientId);
        }
    }

    private void initViews() {
        etNombres = findViewById(R.id.et_nombres);
        etApellidos = findViewById(R.id.et_apellidos);
        etDni = findViewById(R.id.et_dni);
        etGenero = findViewById(R.id.et_genero);
        etEmail = findViewById(R.id.et_email);
        etTelefono = findViewById(R.id.et_telefono);
        etFecha = findViewById(R.id.et_fecha_nacimiento);

        tilNombres = findViewById(R.id.til_nombres);
        tilApellidos = findViewById(R.id.til_apellidos);
        tilDni = findViewById(R.id.til_dni);

        btnGuardar = findViewById(R.id.btn_guardar_paciente);
        toolbar = findViewById(R.id.toolbar_create);
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void mostrarCalendario() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Seleccionar fecha")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            etFecha.setText(sdf.format(new Date(selection)));
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

    private void cargarDatosParaEdicion(String id) {
        BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
            EntidadPaciente p = dbHelper.daoPaciente().obtenerPacientePorId(id);
            if (p != null) {
                runOnUiThread(() -> {
                    toolbar.setTitle("Editar Paciente");
                    btnGuardar.setText("Actualizar Datos");
                    etNombres.setText(p.firstName);
                    etApellidos.setText(p.lastName);
                    etDni.setText(p.idNumber);
                    etGenero.setText(p.gender);
                    etEmail.setText(p.email);
                    etTelefono.setText(p.phone);
                    etFecha.setText(p.birthDate);
                });
            }
        });
    }

    private void guardarPaciente() {
        tilNombres.setError(null);
        tilApellidos.setError(null);
        tilDni.setError(null);

        String nombres = etNombres.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String dni = etDni.getText().toString().trim();

        boolean hasError = false;
        if (nombres.isEmpty()) { tilNombres.setError("Requerido"); hasError = true; }
        if (apellidos.isEmpty()) { tilApellidos.setError("Requerido"); hasError = true; }
        if (dni.isEmpty()) { tilDni.setError("Requerido"); hasError = true; }

        if (hasError) return;

        BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
            boolean isSuccess = true;
            try {
                if (currentPatientId == null) {
                    EntidadPaciente newPatient = new EntidadPaciente();
                    newPatient.id = UUID.randomUUID().toString();
                    newPatient.nutritionistId = "admin@cnp.pe";
                    newPatient.firstName = nombres;
                    newPatient.lastName = apellidos;
                    newPatient.idNumber = dni;
                    newPatient.gender = etGenero.getText().toString().trim();
                    newPatient.email = etEmail.getText().toString().trim();
                    newPatient.phone = etTelefono.getText().toString().trim();
                    newPatient.birthDate = etFecha.getText().toString().trim();
                    newPatient.active = true;

                    dbHelper.daoPaciente().insertarPaciente(newPatient);
                } else {
                    EntidadPaciente pacienteExistente = dbHelper.daoPaciente().obtenerPacientePorId(currentPatientId);
                    if (pacienteExistente != null) {
                        pacienteExistente.firstName = nombres;
                        pacienteExistente.lastName = apellidos;
                        pacienteExistente.idNumber = dni;
                        pacienteExistente.gender = etGenero.getText().toString().trim();
                        pacienteExistente.email = etEmail.getText().toString().trim();
                        pacienteExistente.phone = etTelefono.getText().toString().trim();
                        pacienteExistente.birthDate = etFecha.getText().toString().trim();

                        dbHelper.daoPaciente().actualizarPaciente(pacienteExistente);
                    } else {
                        isSuccess = false;
                    }
                }
            } catch (Exception e) {
                isSuccess = false;
            }

            final boolean finalSuccess = isSuccess;
            runOnUiThread(() -> {
                if (finalSuccess) {
                    Toast.makeText(this, "Paciente guardado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Error al procesar la solicitud", Toast.LENGTH_LONG).show();
                }
            });
        });
    }
}