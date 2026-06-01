package com.newtyf.cnp_patients_app.views;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.newtyf.cnp_patients_app.R;

public class PatientCreateActivity extends AppCompatActivity {

    private ImageView btnBack;
    private MaterialButton btnRegistrar, btnCancelar;
    private TextInputEditText etNombre, etDni;

    // Variables para el Acordeón
    private LinearLayout headerContacto, contentContacto;
    private ImageView iconArrowContacto;

    private LinearLayout headerAdicional, contentAdicional;
    private ImageView iconArrowAdicional;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_create);

        // 1. Vincular vistas principales
        btnBack = findViewById(R.id.btn_back_create);
        btnRegistrar = findViewById(R.id.btn_registrar_paciente);
        btnCancelar = findViewById(R.id.btn_cancelar_paciente);
        etNombre = findViewById(R.id.et_pac_nombre);
        etDni = findViewById(R.id.et_pac_dni);

        // 2. Vincular vistas del Acordeón (Información de Contacto)
        headerContacto = findViewById(R.id.header_contacto);
        contentContacto = findViewById(R.id.content_contacto);
        iconArrowContacto = findViewById(R.id.icon_arrow_contacto);

        // 3. Vincular vistas del Acordeón (Información Adicional)
        headerAdicional = findViewById(R.id.header_adicional);
        contentAdicional = findViewById(R.id.content_adicional);
        iconArrowAdicional = findViewById(R.id.icon_arrow_adicional);

        // --- ESTADO INICIAL (UX) ---
        // Ocultamos las secciones secundarias al inicio para no abrumar al usuario
        contentContacto.setVisibility(View.GONE);
        contentAdicional.setVisibility(View.GONE);

        // --- EVENTOS CLIC ---

        // Flecha de atrás y botón cancelar
        btnBack.setOnClickListener(v -> finish());
        btnCancelar.setOnClickListener(v -> finish());

        // Eventos para abrir/cerrar acordeones
        headerContacto.setOnClickListener(v -> toggleAccordion(contentContacto, iconArrowContacto));
        headerAdicional.setOnClickListener(v -> toggleAccordion(contentAdicional, iconArrowAdicional));

        // Botón Registrar
        btnRegistrar.setOnClickListener(v -> {
            // UX: Validación rápida
            if (etNombre.getText() != null && etNombre.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "El nombre del paciente es obligatorio", Toast.LENGTH_SHORT).show();
                return;
            }

            mostrarModalExito();
        });
    }

    /**
     * Método reutilizable para animar cualquier sección tipo acordeón.
     */
    private void toggleAccordion(View content, ImageView arrow) {
        boolean isCurrentlyVisible = content.getVisibility() == View.VISIBLE;

        if (isCurrentlyVisible) {
            // Si está abierto, lo cerramos y la flecha vuelve a mirar hacia abajo
            content.setVisibility(View.GONE);
            arrow.animate().rotation(0f).setDuration(200).start();
        } else {
            // Si está cerrado, lo abrimos y la flecha gira hacia arriba (180 grados)
            content.setVisibility(View.VISIBLE);
            arrow.animate().rotation(180f).setDuration(200).start();
        }
    }

    private void mostrarModalExito() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_success_patient);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.setCancelable(false);
        dialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            dialog.dismiss();
            finish();
        }, 2000);
    }
}