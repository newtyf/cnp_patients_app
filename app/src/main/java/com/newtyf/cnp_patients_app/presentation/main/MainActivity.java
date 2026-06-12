package com.newtyf.cnp_patients_app.presentation.main;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.model.Patient;
import com.newtyf.cnp_patients_app.presentation.dashboard.DashboardFragment;
import com.newtyf.cnp_patients_app.presentation.patients.list.PacienteListFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    NavigationBarView navigation;

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
        navigation = findViewById(R.id.bottom_navigation);

        loadFragment(new DashboardFragment());

        navigation.setOnItemSelectedListener((item) -> {
            int id = item.getItemId();
            if (id == R.id.item_1) {
                Log.i("NAVIGATION", "Inicio");
                loadFragment(new DashboardFragment());
                return true;
            } else if (id == R.id.item_2) {
                Log.i("NAVIGATION", "Pacientes");
                loadFragment(new PacienteListFragment());
                return true;
            } else if (id == R.id.item_3) {
                Log.i("NAVIGATION", "Dietas");
                loadFragment(new DashboardFragment());
                return true;
            } else if (id == R.id.item_4) {
                Log.i("NAVIGATION", "Mapa");
                loadFragment(new DashboardFragment());
                return true;
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
