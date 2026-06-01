package com.newtyf.cnp_patients_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegister;
    private TextView tvForgotPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializamos la base de datos
        dbHelper = new DatabaseHelper(this);

        // 1. Vincular vistas con los IDs del XML
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login_submit);
        tvRegister = findViewById(R.id.tv_top_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);

        // 2. Acciones de clics
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // UX: Validación de campos vacíos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // UX: Validación con la base de datos local
            boolean isValid = dbHelper.checkUser(email, password);

            if (isValid) {
                Toast.makeText(this, "¡Bienvenido a CNP Gestión!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish(); // Cerramos el login para que no pueda volver con el botón "Atrás"
            } else {
                Toast.makeText(this, "Credenciales incorrectas. Intenta de nuevo.", Toast.LENGTH_LONG).show();
            }
        });

        // Navegación hacia Registro
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Feedback de contraseña olvidada
        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Recuperación de contraseña en construcción", Toast.LENGTH_SHORT).show();
        });
    }
}