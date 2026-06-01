package com.newtyf.cnp_patients_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.newtyf.cnp_patients_app.R;

public class ProfileActivity extends AppCompatActivity {

    private ImageView btnBack;
    private MaterialButton btnEditPersonal, btnEditSecurity, btnEditAddress, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btn_back_profile), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top + v.getPaddingTop(), v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        // Vincular Vistas
        btnBack = findViewById(R.id.btn_back_profile);
        btnEditPersonal = findViewById(R.id.btn_edit_personal);
        btnEditSecurity = findViewById(R.id.btn_edit_security);
        btnEditAddress = findViewById(R.id.btn_edit_address);
        btnLogout = findViewById(R.id.btn_logout);

        // Volver al Dashboard
        btnBack.setOnClickListener(v -> finish());

        // Acciones Mockeadas (Para construir los formularios después)
        btnEditPersonal.setOnClickListener(v ->
                Toast.makeText(this, "Abriendo formulario de Información Personal...", Toast.LENGTH_SHORT).show()
        );

        btnEditSecurity.setOnClickListener(v ->
                Toast.makeText(this, "Abriendo configuración de Seguridad...", Toast.LENGTH_SHORT).show()
        );

        btnEditAddress.setOnClickListener(v ->
                Toast.makeText(this, "Abriendo mapa y dirección...", Toast.LENGTH_SHORT).show()
        );

        // Lógica segura de Cerrar Sesión
        btnLogout.setOnClickListener(v -> cerrarSesion());
    }

    private void cerrarSesion() {
        // Aquí en el futuro limpiaremos SharedPreferences o el Token de sesión.

        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();

        // El Intent con banderas asegura que el usuario no pueda volver usando el botón "Atrás"
        Intent intent = new Intent(ProfileActivity.this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}