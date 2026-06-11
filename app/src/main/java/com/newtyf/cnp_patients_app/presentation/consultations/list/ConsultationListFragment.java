package com.newtyf.cnp_patients_app.presentation.consultations.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.newtyf.cnp_patients_app.R;

public class ConsultationListFragment extends Fragment {

    private static final String ARG_PATIENT_ID = "patient_id";

    public static ConsultationListFragment newInstance(String patientId) {
        ConsultationListFragment fragment = new ConsultationListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PATIENT_ID, patientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consultation_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout container = view.findViewById(R.id.containerConsultas);
        cargarMock(container);
    }

    private void cargarMock(LinearLayout container) {
        String[][] consultas = {
            { "Evaluación Inicial",  "15 Octubre, 2023",   "100kg", "\"Paciente ingresa con IMC de 29.5. Refiere cansancio crónico. Se establecen metas iniciales.\"" },
            { "Control Mensual",     "12 Noviembre, 2023",  "95kg",  "\"Reducción de 1.2kg. Mejora en los niveles de glucosa en ayunas. Paciente reporta mejor energía.\"" },
            { "Ajuste de Dieta",     "20 Diciembre, 2023",  "90kg",  "\"Ajuste calórico para fiestas de fin de año. Se reemplazan colaciones por opciones más saludables.\"" },
            { "Control Mensual",     "18 Enero, 2024",      "88kg",  "\"Paciente alcanzó meta de -12kg. Se ajusta plan para fase de mantenimiento.\"" },
        };

        for (String[] c : consultas) {
            View item = LayoutInflater.from(getContext()).inflate(R.layout.item_consulta, container, false);
            ((TextView) item.findViewById(R.id.tvTipoNombre)).setText(c[0]);
            ((TextView) item.findViewById(R.id.tvFecha)).setText(c[1]);
            ((Chip)     item.findViewById(R.id.chipPeso)).setText(c[2]);
            ((TextView) item.findViewById(R.id.tvNota)).setText(c[3]);
            container.addView(item);
        }
    }
}
