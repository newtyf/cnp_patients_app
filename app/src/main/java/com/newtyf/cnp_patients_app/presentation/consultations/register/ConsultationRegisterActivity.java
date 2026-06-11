package com.newtyf.cnp_patients_app.presentation.consultations.register;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.newtyf.cnp_patients_app.R;

public class ConsultationRegisterActivity extends AppCompatActivity {

    public static final String EXTRA_PATIENT_ID = "patient_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultation_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(v -> finish());

        if (savedInstanceState == null) {
            String patientId = getIntent().getStringExtra(EXTRA_PATIENT_ID);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.stepContainer, ConsultationStep1Fragment.newInstance(patientId))
                    .commit();
        }
    }

    public void updateProgress(int step, int total) {
        ((TextView) findViewById(R.id.tvPaso)).setText("PASO " + step + " / " + total);
        ((com.google.android.material.progressindicator.LinearProgressIndicator)
                findViewById(R.id.progressPasos)).setProgress(step);
    }
}
