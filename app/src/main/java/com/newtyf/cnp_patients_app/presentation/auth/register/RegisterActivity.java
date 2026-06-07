package com.newtyf.cnp_patients_app.presentation.auth.register;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.newtyf.cnp_patients_app.R;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tfNombre, tfEmail, tfLicencia, tfEspecialidad, tfPassword, tfConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupEspecialidadDropdown();
    }

    private void initViews() {
        tfNombre = findViewById(R.id.tfNombre);
        tfEmail = findViewById(R.id.tfEmail);
        tfLicencia = findViewById(R.id.tfLicencia);
        tfEspecialidad = findViewById(R.id.tfEspecialidad);
        tfPassword = findViewById(R.id.tfPassword);
        tfConfirmPassword = findViewById(R.id.tfConfirmPassword);
    }

    private void setupEspecialidadDropdown() {
        AutoCompleteTextView actv = findViewById(R.id.actvEspecialidad);
        String[] especialidades = getResources().getStringArray(R.array.especialidades);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                especialidades
        );
        actv.setAdapter(adapter);
    }

    public void Register(View view) {
        // TODO: implementar lógica de registro
    }

    public void GoBack(View view) {
        finish();
    }
}
