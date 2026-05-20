package com.newtyf.cnp_patients_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.newtyf.cnp_patients_app.MainActivity;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.models.Patient;

public class PatientDetailActivity extends AppCompatActivity {

    TextView tvDetNombre, tvDetDni, tvDetEmail, tvDetTelefono, tvDetFechaNac, tvDetObjetivo;

    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvDetNombre = findViewById(R.id.tvDetNombre);
        tvDetDni = findViewById(R.id.tvDetDni);
        tvDetEmail = findViewById(R.id.tvDetEmail);
        tvDetTelefono = findViewById(R.id.tvDetTelefono);
        tvDetFechaNac = findViewById(R.id.tvDetFechaNac);
        tvDetObjetivo = findViewById(R.id.tvDetObjetivo);

        loadPatient();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPatient();
    }

    private void loadPatient() {
        patient = Patient.getByDni(MainActivity.pacientes, getIntent().getStringExtra("dni"));
        if (patient != null) {
            tvDetNombre.setText("Nombre: " + patient.getName());
            tvDetDni.setText("DNI: " + patient.getDni());
            tvDetEmail.setText("Email: " + patient.getEmail());
            tvDetTelefono.setText("Teléfono: " + patient.getPhone());
            tvDetFechaNac.setText("Fecha de nacimiento: " + patient.getBirthDate());
            tvDetObjetivo.setText("Objetivo: " + patient.getObjective());
        }
    }

    public void GoBack(View view) {
        finish();
    }

    public void Edit(View view) {
        Intent intent = new Intent(this, PatientCreateActivity.class);
        intent.putExtra("dni", patient.getDni());
        startActivity(intent);
    }

    public void Delete(View view) {
        Log.i("LOG", patient.getName());
        MainActivity.pacientes.remove(patient);
        Toast.makeText(this, "Paciente eliminado", Toast.LENGTH_SHORT).show();
        finish();
    }
}
