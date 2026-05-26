package com.newtyf.cnp_patients_app.presentation.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.presentation.patients.create.PatientCreateFragment;

public class DashboardFragment extends Fragment {

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btnAccionNuevoPac).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, PatientCreateFragment.newInstance())
                        .addToBackStack(null)
                        .commit());

        view.findViewById(R.id.btnAccionNuevaConsulta).setOnClickListener(v -> {
            // TODO: navegar a lista de pacientes para seleccionar consulta
        });

        view.findViewById(R.id.btnAccionCalculadora).setOnClickListener(v -> {
            // TODO: calculadora de energía
        });

        view.findViewById(R.id.btnAccionExportar).setOnClickListener(v -> {
            // TODO: exportar dieta
        });
    }
}
