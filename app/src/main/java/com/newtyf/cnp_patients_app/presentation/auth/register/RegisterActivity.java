package com.newtyf.cnp_patients_app.presentation.auth.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.presentation.main.MainActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText txtNombre, txtDni, txtEmailReg, txtTelefono, txtEspecialidad, txtPasswordReg;

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

        txtNombre = findViewById(R.id.txtNombre);
        txtDni = findViewById(R.id.txtDni);
        txtEmailReg = findViewById(R.id.txtEmailReg);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtEspecialidad = findViewById(R.id.txtEspecialidad);
        txtPasswordReg = findViewById(R.id.txtPasswordReg);
    }

    public void Register(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void GoBack(View view) {
        finish();
    }
}
