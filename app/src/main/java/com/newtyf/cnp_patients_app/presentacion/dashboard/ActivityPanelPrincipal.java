package com.newtyf.cnp_patients_app.presentacion.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.newtyf.cnp_patients_app.R;

// Importaciones de las pantallas destino
import com.newtyf.cnp_patients_app.presentacion.consultations.ActivityListaConsultas;
import com.newtyf.cnp_patients_app.presentacion.diet.ActivityGeneradorDieta;

public class ActivityPanelPrincipal extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_principal);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        // 1. Cargar el Fragment de inicio por defecto
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new FragmentoInicio())
                    .commit();
        }

        // 2. Lógica para cambiar de pantalla
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Ya estamos en el inicio, no hacemos nada extra pero lo marcamos como seleccionado
                return true;
            } else if (itemId == R.id.nav_consultas) {
                // Abrir la pantalla de Consultas
                startActivity(new Intent(this, ActivityListaConsultas.class));
                return false; // Retornamos false para que el ícono de "Home" siga marcado debajo
            } else if (itemId == R.id.nav_dietas) {
                // Abrir la pantalla de Dietas
                startActivity(new Intent(this, ActivityGeneradorDieta.class));
                return false;
            } else if (itemId == R.id.nav_mapa) {
                Toast.makeText(this, "Pestaña Mapa en desarrollo", Toast.LENGTH_SHORT).show();
                return false;
            }

            return false;
        });
    }
}