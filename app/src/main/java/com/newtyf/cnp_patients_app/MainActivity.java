package com.newtyf.cnp_patients_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.newtyf.cnp_patients_app.domain.models.Patient;
import com.newtyf.cnp_patients_app.presentacion.auth.ActivityLogin;

import java.util.ArrayList;
import java.util.List;

// Autor: pregunta1
public class MainActivity extends AppCompatActivity {

    public static List<Patient> pacientes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Pacientes de prueba
// Dentro de tu MainActivity.java, al final de onCreate():

        // Cargar pacientes de prueba
        pacientes.add(new Patient(
                "1",              // id
                "nutri_001",      // nutritionistId
                "Ana",            // firstName
                "Torres",         // lastName
                "12345678",       // idNumber (DNI)
                "ana@email.com",  // email
                "987654321",      // phone
                "01/01/1990",     // birthDate
                "F",              // gender
                "",               // photoPath (vacío por ahora)
                1                 // active (1 = activo)
        ));

        // 1. Ir a la vista de LOGUEO
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
        finish(); // Cierra MainActivity para que no quede en el historial
    }
}