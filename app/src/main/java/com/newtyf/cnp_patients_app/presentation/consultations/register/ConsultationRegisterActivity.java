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
            Insets ime = insets.getInsets(WindowInsetsCompat.Type.ime());
            int bottom = Math.max(systemBars.bottom, ime.bottom);
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottom);
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

    public void goToStep2(String patientId, String consultationId) {
        updateProgress(2);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.stepContainer, ConsultationStep2Fragment.newInstance(patientId, consultationId))
                .commit();
    }

    public void goToStep3(String patientId, String consultationId) {
        updateProgress(3);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.stepContainer, ConsultationStep3Fragment.newInstance(patientId, consultationId))
                .commit();
    }

    private void updateProgress(int step) {
        ((TextView) findViewById(R.id.tvPaso)).setText("PASO " + step + " / 3");
        ((com.google.android.material.progressindicator.LinearProgressIndicator)
                findViewById(R.id.progressPasos)).setProgress(step);
    }
}
