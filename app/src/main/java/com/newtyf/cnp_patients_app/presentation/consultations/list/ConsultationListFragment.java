package com.newtyf.cnp_patients_app.presentation.consultations.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.color.MaterialColors;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.model.Consultation;
import com.newtyf.cnp_patients_app.data.repository.ConsultationRepository;

import java.util.List;

public class ConsultationListFragment extends Fragment {

    private static final String ARG_PATIENT_ID = "patient_id";

    private ConsultationRepository repository;

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

        repository = new ConsultationRepository(requireContext());

        String patientId = getArguments() != null ? getArguments().getString(ARG_PATIENT_ID) : null;
        if (patientId == null) return;

        List<Consultation> lista = repository.getByPatient(patientId);

        View emptyState = view.findViewById(R.id.emptyState);
        ScrollView scrollView = view.findViewById(R.id.scrollConsultas);

        if (lista.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            LinearLayout container = view.findViewById(R.id.containerConsultas);
            for (Consultation c : lista) {
                renderItem(container, c);
            }
        }
    }

    private void renderItem(LinearLayout container, Consultation c) {
        View item = LayoutInflater.from(getContext()).inflate(R.layout.item_consulta, container, false);

        TextView tvRazon = item.findViewById(R.id.tvTipoNombre);
        TextView tvFecha = item.findViewById(R.id.tvFecha);
        TextView tvNota  = item.findViewById(R.id.tvNota);
        Chip chipEstado  = item.findViewById(R.id.chipEstado);

        tvRazon.setText(c.getReason() != null ? c.getReason() : getString(R.string.detail_patient_value_placeholder));
        tvFecha.setText(c.getCreatedAt() != null ? c.getCreatedAt() : "—");
        tvNota.setText(c.getNotes() != null ? c.getNotes() : "");
        tvNota.setVisibility(c.getNotes() != null && !c.getNotes().isEmpty() ? View.VISIBLE : View.GONE);

        boolean completado = c.getAnthropometricRecord() != null;
        if (completado) {
            chipEstado.setText(getString(R.string.consulta_estado_completado));
            chipEstado.setChipBackgroundColor(android.content.res.ColorStateList.valueOf(
                    MaterialColors.getColor(chipEstado, com.google.android.material.R.attr.colorSecondaryContainer)));
            chipEstado.setTextColor(
                    MaterialColors.getColor(chipEstado, com.google.android.material.R.attr.colorOnSecondaryContainer));
        }

        container.addView(item);
    }
}
