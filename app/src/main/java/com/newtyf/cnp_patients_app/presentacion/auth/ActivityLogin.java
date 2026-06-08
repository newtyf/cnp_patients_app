package com.newtyf.cnp_patients_app.presentacion.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.BaseDatosCnp;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadUsuario;
import com.newtyf.cnp_patients_app.presentacion.dashboard.ActivityPanelPrincipal;

public class ActivityLogin extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLoginSubmit;
    private TextView tvTopRegister;
    private BaseDatosCnp dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Room
        dbHelper = BaseDatosCnp.obtenerBaseDatos(this);

        // Vincular Vistas
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLoginSubmit = findViewById(R.id.btn_login_submit);
        tvTopRegister = findViewById(R.id.tv_top_register);

        // Ir a la pantalla de Registro
        tvTopRegister.setOnClickListener(v -> {
            startActivity(new Intent(ActivityLogin.this, ActivityRegistro.class));
        });

        // Acción del botón Login
        btnLoginSubmit.setOnClickListener(v -> realizarLogin());
    }

    private void realizarLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa tu correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Desactivamos el botón para evitar que el usuario haga muchos clics
        btnLoginSubmit.setEnabled(false);

        // Ejecutamos la consulta a la BD en segundo plano
        BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
            try {
                // 🔥 AQUÍ ESTÁ LA MAGIA: Ahora recibimos un objeto 'EntidadUsuario' y no un 'int'
                EntidadUsuario usuarioLogueado = dbHelper.daoUsuario().verificarCredenciales(email, password);

                // Volvemos al hilo principal para actualizar la pantalla
                runOnUiThread(() -> {
                    btnLoginSubmit.setEnabled(true);

                    if (usuarioLogueado != null) {
                        // ¡Login Exitoso! (El objeto no es nulo)
                        Toast.makeText(this, "Bienvenido, " + usuarioLogueado.name, Toast.LENGTH_SHORT).show();

                        // Ir al Dashboard y limpiar el historial para no volver al Login
                        Intent intent = new Intent(ActivityLogin.this, ActivityPanelPrincipal.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        // ¡Login Fallido! (El objeto vino nulo)
                        Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    btnLoginSubmit.setEnabled(true);
                    Toast.makeText(this, "Error de conexión con la base de datos", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}