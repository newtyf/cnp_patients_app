package com.newtyf.cnp_patients_app.views;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

        int index = getIntent().getIntExtra("index", -1);
        if (index >= 0 && index < MainActivity.pacientes.size()) {
            Patient p = MainActivity.pacientes.get(index);
            tvDetNombre.setText("Nombre: " + p.getName());
            tvDetDni.setText("DNI: " + p.getDni());
            tvDetEmail.setText("Email: " + p.getEmail());
            tvDetTelefono.setText("Teléfono: " + p.getPhone());
            tvDetFechaNac.setText("Fecha de nacimiento: " + p.getBirthDate());
            tvDetObjetivo.setText("Objetivo: " + p.getObjective());
        }
    }

    public void GoBack(View view) {
        finish();
    }
}
