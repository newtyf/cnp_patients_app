package com.newtyf.cnp_patients_app.presentacion.auth;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.BaseDatosCnp;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadUsuario; // ¡Nueva importación de la Entidad!

public class ActivityRegistro extends AppCompatActivity {

    private TextInputEditText etNombre, etDni, etEmail, etTelefono, etEspecialidad, etPassword;
    private CheckBox cbTerminos;
    private TextView tvLeerTerminos;
    private MaterialButton btnRegisterSubmit;
    private ImageView btnBack;
    private BaseDatosCnp dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializamos la base de datos usando nuestro Singleton
        dbHelper = BaseDatosCnp.obtenerBaseDatos(this);

        // 1. Vincular las vistas con los IDs del XML
        etNombre = findViewById(R.id.et_reg_nombre);
        etDni = findViewById(R.id.et_reg_dni);
        etEmail = findViewById(R.id.et_reg_email);
        etTelefono = findViewById(R.id.et_reg_telefono);
        etEspecialidad = findViewById(R.id.et_reg_especialidad);
        etPassword = findViewById(R.id.et_reg_password);
        cbTerminos = findViewById(R.id.cb_terminos);
        tvLeerTerminos = findViewById(R.id.tv_leer_terminos);
        btnRegisterSubmit = findViewById(R.id.btn_register_submit);
        btnBack = findViewById(R.id.btn_back);

        // 2. Acción para retroceder limpiamente
        btnBack.setOnClickListener(v -> finish());

        // 3. UX: Mostrar el Modal de Términos y Condiciones al hacer clic en el texto
        tvLeerTerminos.setOnClickListener(v -> mostrarDialogoTerminos());

        // 4. Lógica de registro
        btnRegisterSubmit.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // UX: Validación de campos principales obligatorios
            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa los campos principales (Nombre, Correo, Contraseña)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!cbTerminos.isChecked()) {
                Toast.makeText(this, "Debes aceptar los Términos y Condiciones para continuar", Toast.LENGTH_LONG).show();
                return;
            }

            // 🚀 NUEVA LÓGICA ARQUITECTÓNICA CON ROOM
            // Ejecutamos en segundo plano para no bloquear la pantalla
            BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {

                // Creamos el objeto (Entidad) que Room necesita
                EntidadUsuario nuevoUsuario = new EntidadUsuario();
                nuevoUsuario.name = nombre;
                nuevoUsuario.email = email;
                nuevoUsuario.password = password;

                // Room inserta y nos devuelve el ID. Si hay conflicto (correo duplicado), devuelve -1
                long resultId = dbHelper.daoUsuario().insertarUsuario(nuevoUsuario);

                // Volvemos al hilo principal de UI para mostrar mensajes y cambiar de pantalla
                runOnUiThread(() -> {
                    if (resultId != -1) {
                        Toast.makeText(this, "Registro exitoso. Por favor, inicia sesión.", Toast.LENGTH_LONG).show();
                        // Lo enviamos de regreso al Login
                        finish();
                    } else {
                        Toast.makeText(this, "Error: El correo electrónico ya está registrado", Toast.LENGTH_LONG).show();
                    }
                });
            });
        });
    }

    private void mostrarDialogoTerminos() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Términos y Condiciones")
                .setMessage(getString(R.string.terminos_condiciones_texto))
                .setPositiveButton("Aceptar y continuar", (dialog, which) -> {
                    // Si el usuario acepta desde el diálogo, marcamos el checkbox automáticamente
                    cbTerminos.setChecked(true);
                })
                .setNegativeButton("Cerrar", (dialog, which) -> dialog.dismiss())
                .show();
    }
}