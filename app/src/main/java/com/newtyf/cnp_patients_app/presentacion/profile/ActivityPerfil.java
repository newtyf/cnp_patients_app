package com.newtyf.cnp_patients_app.presentacion.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.BaseDatosCnp;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadUsuario;
import com.newtyf.cnp_patients_app.presentacion.auth.ActivityBienvenida;

public class ActivityPerfil extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvProfileName;
    private MaterialButton btnEditPersonal, btnEditSecurity, btnEditAddress, btnLogout;
    private BaseDatosCnp dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil);

        // Inicializar Room
        dbHelper = BaseDatosCnp.obtenerBaseDatos(this);

        // Vincular Vistas
        btnBack = findViewById(R.id.btn_back_profile);
        tvProfileName = findViewById(R.id.tv_profile_name);
        btnEditPersonal = findViewById(R.id.btn_edit_personal);
        btnEditSecurity = findViewById(R.id.btn_edit_security);
        btnEditAddress = findViewById(R.id.btn_edit_address);
        btnLogout = findViewById(R.id.btn_logout);

        // 1. Acciones de navegación
        btnBack.setOnClickListener(v -> finish());

        // 🚀 MÓDULOS DE PERFIL
        btnEditPersonal.setOnClickListener(v -> {
            startActivity(new Intent(ActivityPerfil.this, ActivityEditarPersonal.class));
        });

        btnEditSecurity.setOnClickListener(v -> {
            startActivity(new Intent(ActivityPerfil.this, ActivityEditarSeguridad.class));
        });

        btnEditAddress.setOnClickListener(v -> {
            startActivity(new Intent(ActivityPerfil.this, ActivityEditarDireccion.class));
        });
        // Lógica de Cerrar Sesión
        btnLogout.setOnClickListener(v -> mostrarDialogoCerrarSesion());

        // Cargar datos iniciales
        cargarDatosNutricionista();
    }

    // 🔥 Este método es CLAVE. Se ejecuta cada vez que regresas a esta pantalla
    @Override
    protected void onResume() {
        super.onResume();
        cargarDatosNutricionista(); // Recargamos el nombre por si lo cambiaron en ActivityEditarPersonal
    }

    private void cargarDatosNutricionista() {
        // Consultamos en segundo plano
        BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
            try {
                // Buscamos a nuestro usuario actual
                EntidadUsuario usuarioActivo = dbHelper.daoUsuario().obtenerUsuarioPorEmail("admin@cnp.pe");

                if (usuarioActivo != null) {
                    // Volvemos a la UI para pintar el nombre actualizado
                    runOnUiThread(() -> {
                        if (tvProfileName != null) {
                            tvProfileName.setText(usuarioActivo.name);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void mostrarDialogoCerrarSesion() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que deseas salir de la aplicación?")
                .setNegativeButton(getString(R.string.accion_cancelar), (dialog, which) -> dialog.dismiss())
                .setPositiveButton(getString(R.string.accion_aceptar), (dialog, which) -> cerrarSesion())
                .show();
    }

    private void cerrarSesion() {
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();

        // Limpiamos el historial de navegación
        Intent intent = new Intent(ActivityPerfil.this, ActivityBienvenida.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}