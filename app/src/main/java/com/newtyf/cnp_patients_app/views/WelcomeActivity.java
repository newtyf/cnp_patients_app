package com.newtyf.cnp_patients_app.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.newtyf.cnp_patients_app.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        MaterialButton btnLogin = findViewById(R.id.btn_welcome_login);
        MaterialButton btnRegister = findViewById(R.id.btn_welcome_register);

        // 1. Enlace hacia la pantalla de Inicio de Sesión
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // 2. NUEVO: Enlace hacia tu nueva y moderna pantalla de Registro
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}