package com.newtyf.cnp_patients_app.presentacion.profile;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.newtyf.cnp_patients_app.R;

public class ActivityEditarDireccion extends AppCompatActivity {

    private TextInputEditText etDireccion, etReferencia;
    private MaterialButton btnGuardar;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_direccion);

        toolbar = findViewById(R.id.toolbar_edit_address);
        etDireccion = findViewById(R.id.et_edit_direccion);
        etReferencia = findViewById(R.id.et_edit_referencia);
        btnGuardar = findViewById(R.id.btn_guardar_direccion);

        toolbar.setNavigationOnClickListener(v -> finish());

        // Mocks de dirección
        etDireccion.setText("Av. Javier Prado Este 1234, Lima");

        btnGuardar.setOnClickListener(v -> {
            Toast.makeText(this, "Dirección actualizada correctamente", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}