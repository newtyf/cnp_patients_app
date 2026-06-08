package com.newtyf.cnp_patients_app.presentacion.auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.newtyf.cnp_patients_app.R;

public class ActivityBienvenida extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);

        MaterialButton btnLogin = findViewById(R.id.btn_welcome_login);
        MaterialButton btnRegister = findViewById(R.id.btn_welcome_register);

        // 1. Enlace hacia la pantalla de Inicio de Sesión con animación
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityBienvenida.this, ActivityLogin.class);
            startActivity(intent);
            // Animación: La nueva pantalla entra desde la derecha, la actual sale por la izquierda
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        });

        // 2. Enlace hacia la pantalla de Registro con animación
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(ActivityBienvenida.this, ActivityRegistro.class);
            startActivity(intent);
            // Animación fluida de desvanecimiento
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}