package com.newtyf.cnp_patients_app.presentation.auth.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.model.Nutritionist;
import com.newtyf.cnp_patients_app.data.repository.NutritionistRepository;
import com.newtyf.cnp_patients_app.presentation.auth.pin.PinActivity;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tfNombre, tfEmail, tfLicencia, tfEspecialidad, tfPassword, tfConfirmPassword;
    private NutritionistRepository repository;

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

        repository = new NutritionistRepository(this);

        initViews();
        setupEspecialidadDropdown();
        setupBotones();
    }

    private void initViews() {
        tfNombre          = findViewById(R.id.tfNombre);
        tfEmail           = findViewById(R.id.tfEmail);
        tfLicencia        = findViewById(R.id.tfLicencia);
        tfEspecialidad    = findViewById(R.id.tfEspecialidad);
        tfPassword        = findViewById(R.id.tfPassword);
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

    private void setupBotones() {
        findViewById(R.id.btnRegistrar).setOnClickListener(v -> registrar());
        findViewById(R.id.btnGoToLogin).setOnClickListener(v -> finish());
    }

    private void registrar() {
        String nombre    = getText(tfNombre);
        String email     = getText(tfEmail);
        String licencia  = getText(tfLicencia);
        String password  = getText(tfPassword);
        String confirm   = getText(tfConfirmPassword);

        boolean valido = true;

        if (nombre.isEmpty()) {
            tfNombre.setError(getString(R.string.error_campo_requerido));
            valido = false;
        } else tfNombre.setError(null);

        if (email.isEmpty()) {
            tfEmail.setError(getString(R.string.error_campo_requerido));
            valido = false;
        } else tfEmail.setError(null);

        if (licencia.isEmpty()) {
            tfLicencia.setError(getString(R.string.error_campo_requerido));
            valido = false;
        } else tfLicencia.setError(null);

        if (password.isEmpty()) {
            tfPassword.setError(getString(R.string.error_campo_requerido));
            valido = false;
        } else if (password.length() < 8) {
            tfPassword.setError(getString(R.string.error_password_corta));
            valido = false;
        } else tfPassword.setError(null);

        if (!password.equals(confirm)) {
            tfConfirmPassword.setError(getString(R.string.error_passwords_no_coinciden));
            valido = false;
        } else tfConfirmPassword.setError(null);

        if (!valido) return;

        AutoCompleteTextView actv = findViewById(R.id.actvEspecialidad);
        String especialidad = actv.getText().toString().trim();

        Nutritionist nutritionist = new Nutritionist();
        nutritionist.setFullName(nombre);
        nutritionist.setEmail(email);
        nutritionist.setLicenseNumber(licencia);
        nutritionist.setSpecialty(especialidad.isEmpty() ? null : especialidad);

        repository.insert(nutritionist);

        Intent intent = new Intent(this, PinActivity.class);
        intent.putExtra(PinActivity.EXTRA_MODE, PinActivity.MODE_SETUP);
        intent.putExtra(PinActivity.EXTRA_NUTRITIONIST_ID, nutritionist.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private String getText(TextInputLayout tf) {
        if (tf.getEditText() == null) return "";
        return tf.getEditText().getText().toString().trim();
    }
}
