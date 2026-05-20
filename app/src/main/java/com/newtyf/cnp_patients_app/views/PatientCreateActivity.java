package com.newtyf.cnp_patients_app.views;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.newtyf.cnp_patients_app.MainActivity;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.models.Patient;

public class PatientCreateActivity extends AppCompatActivity {

    EditText txtNombrePac, txtDniPac, txtEmailPac, txtTelefonoPac, txtFechaNacPac, txtObjetivoPac;
    Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_create);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtNombrePac = findViewById(R.id.txtNombrePac);
        txtDniPac = findViewById(R.id.txtDniPac);
        txtEmailPac = findViewById(R.id.txtEmailPac);
        txtTelefonoPac = findViewById(R.id.txtTelefonoPac);
        txtFechaNacPac = findViewById(R.id.txtFechaNacPac);
        txtObjetivoPac = findViewById(R.id.txtObjetivoPac);

        patient = Patient.getByDni(MainActivity.pacientes, getIntent().getStringExtra("dni"));
        if (patient != null) {
            txtNombrePac.setText(patient.getName());
            txtDniPac.setText(patient.getDni());
            txtEmailPac.setText(patient.getEmail());
            txtTelefonoPac.setText(patient.getPhone());
            txtFechaNacPac.setText(patient.getBirthDate());
            txtObjetivoPac.setText(patient.getObjective());
        }
    }

    public void SavePatient(View view) {
        String nombre = txtNombrePac.getText().toString().trim();
        String dni = txtDniPac.getText().toString().trim();
        String email = txtEmailPac.getText().toString().trim();
        String telefono = txtTelefonoPac.getText().toString().trim();
        String fechaNac = txtFechaNacPac.getText().toString().trim();
        String objetivo = txtObjetivoPac.getText().toString().trim();

        if (nombre.isEmpty() || dni.isEmpty()) {
            Toast.makeText(this, "Nombre y DNI son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Patient p = new Patient(nombre, dni, email, telefono, fechaNac, objetivo);

        if (patient != null) {
            MainActivity.pacientes.set(MainActivity.pacientes.indexOf(patient), p);
            Toast.makeText(this, "Paciente actualizado", Toast.LENGTH_SHORT).show();
        } else {
            MainActivity.pacientes.add(p);
            Toast.makeText(this, "Paciente creado", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    public void GoBack(View view) {
        finish();
    }
}
