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

public class ActivityEditarPersonal extends AppCompatActivity {

    private TextInputEditText etNombres, etCnp, etEspecialidad;
    private MaterialButton btnGuardar;
    private MaterialToolbar toolbar;

    private BaseDatosCnp dbHelper;
    private EntidadUsuario usuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_personal);

        // Inicializar Base de Datos
        dbHelper = BaseDatosCnp.obtenerBaseDatos(this);

        // Vincular Vistas
        toolbar = findViewById(R.id.toolbar_edit_personal);
        etNombres = findViewById(R.id.et_edit_nombres);
        etCnp = findViewById(R.id.et_edit_cnp);
        etEspecialidad = findViewById(R.id.et_edit_especialidad);
        btnGuardar = findViewById(R.id.btn_guardar_personal);

        // Funcionalidad del botón Atrás
        toolbar.setNavigationOnClickListener(v -> finish());

        // Acción del botón Guardar
        btnGuardar.setOnClickListener(v -> guardarDatosPersonales());

        // 🚀 Cargar los datos reales apenas se abre la pantalla
        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        // Ejecutamos la consulta en segundo plano para no bloquear la interfaz
        BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {

            // Obtenemos el usuario de prueba que inyectamos (admin@cnp.pe)
            // En un futuro, esto vendrá de tu Gestor de Sesión (SharedPreferences)
            try {
                usuarioActual = dbHelper.daoUsuario().obtenerUsuarioPorEmail("admin@cnp.pe");

                if (usuarioActual != null) {
                    // Volvemos al hilo principal para pintar los datos en la pantalla
                    runOnUiThread(() -> {
                        etNombres.setText(usuarioActual.name);

                        // NOTA: Como la EntidadUsuario original solo tenía email, nombre y password,
                        // dejamos estos textos por defecto por ahora.
                        etCnp.setText("12345");
                        etEspecialidad.setText("Nutrición Clínica");
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void guardarDatosPersonales() {
        String nuevosNombres = etNombres.getText().toString().trim();
        String nuevoCnp = etCnp.getText().toString().trim();
        String nuevaEspecialidad = etEspecialidad.getText().toString().trim();

        // Validación básica
        if (nuevosNombres.isEmpty()) {
            etNombres.setError(getString(R.string.error_campo_requerido));
            return;
        }

        if (usuarioActual != null) {
            // Desactivamos el botón para evitar dobles clics
            btnGuardar.setEnabled(false);

            // Ejecutamos la actualización en segundo plano
            BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
                try {
                    // Actualizamos el objeto con los nuevos datos ingresados
                    usuarioActual.name = nuevosNombres;

                    // Si en el futuro agregas cnp y especialidad a EntidadUsuario, los actualizarías así:
                    // usuarioActual.cnp = nuevoCnp;
                    // usuarioActual.especialidad = nuevaEspecialidad;

                    // Mandamos el objeto actualizado a Room
                    dbHelper.daoUsuario().actualizarUsuario(usuarioActual);

                    // Volvemos al hilo principal para avisar del éxito y cerrar
                    runOnUiThread(() -> {
                        Toast.makeText(this, getString(R.string.edit_profile_save) + " con éxito", Toast.LENGTH_SHORT).show();
                        finish(); // Cierra la pantalla y regresa al Perfil
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
                        btnGuardar.setEnabled(true);
                    });
                }
            });
        }
    }
}