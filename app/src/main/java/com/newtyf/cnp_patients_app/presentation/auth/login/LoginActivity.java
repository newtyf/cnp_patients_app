package com.newtyf.cnp_patients_app.presentation.auth.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.presentation.auth.pin.PinActivity;
import com.newtyf.cnp_patients_app.presentation.auth.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout tfEmail, tfPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tfEmail = findViewById(R.id.tfEmail);
        tfPassword = findViewById(R.id.tfPassword);
    }

    public void GoToPatientList(View view) {
        Intent intent = new Intent(this, PinActivity.class);
        startActivity(intent);
    }

    public void GoToRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
