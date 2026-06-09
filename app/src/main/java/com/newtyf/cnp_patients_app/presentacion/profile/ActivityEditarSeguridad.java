package com.newtyf.cnp_patients_app.presentacion.profile;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.BaseDatosCnp;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadUsuario;

public class ActivityEditarSeguridad extends AppCompatActivity {

    private TextInputEditText etEmail, etCelular, etPassword;
    private MaterialButton btnGuardar;
    private MaterialToolbar toolbar;
    private BaseDatosCnp dbHelper;
    private EntidadUsuario usuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_seguridad);

        dbHelper = BaseDatosCnp.obtenerBaseDatos(this);
        toolbar = findViewById(R.id.toolbar_edit_security);
        etEmail = findViewById(R.id.et_edit_email);
        etCelular = findViewById(R.id.et_edit_celular);
        etPassword = findViewById(R.id.et_edit_password);
        btnGuardar = findViewById(R.id.btn_guardar_seguridad);

        toolbar.setNavigationOnClickListener(v -> finish());
        btnGuardar.setOnClickListener(v -> guardarDatosSeguridad());

        cargarDatosSeguridad();
    }

    private void cargarDatosSeguridad() {
        BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
            try {
                usuarioActual = dbHelper.daoUsuario().obtenerUsuarioPorEmail("admin@cnp.pe");
                if (usuarioActual != null) {
                    runOnUiThread(() -> {
                        etEmail.setText(usuarioActual.email);
                        etPassword.setText(usuarioActual.password);
                        etCelular.setText("987654321"); // Mock: No existe en la BD actual
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void guardarDatosSeguridad() {
        String nuevoEmail = etEmail.getText().toString().trim();
        String nuevaClave = etPassword.getText().toString().trim();

        if (nuevoEmail.isEmpty() || nuevaClave.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_campo_requerido), Toast.LENGTH_SHORT).show();
            return;
        }

        if (usuarioActual != null) {
            btnGuardar.setEnabled(false);
            BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
                try {
                    usuarioActual.email = nuevoEmail;
                    usuarioActual.password = nuevaClave;
                    dbHelper.daoUsuario().actualizarUsuario(usuarioActual);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Datos de seguridad actualizados", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show();
                        btnGuardar.setEnabled(true);
                    });
                }
            });
        }
    }
}