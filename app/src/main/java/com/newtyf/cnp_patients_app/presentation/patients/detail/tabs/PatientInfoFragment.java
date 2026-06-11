package com.newtyf.cnp_patients_app.presentation.patients.detail.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.model.Patient;
import com.newtyf.cnp_patients_app.data.repository.PatientRepository;

public class PatientInfoFragment extends Fragment {

    private static final String ARG_ID = "patient_id";

    public static PatientInfoFragment newInstance(String patientId) {
        PatientInfoFragment fragment = new PatientInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, patientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String patientId = getArguments() != null ? getArguments().getString(ARG_ID) : null;
        if (patientId == null) return;

        Patient patient = new PatientRepository(requireContext()).getById(patientId);
        if (patient == null) return;

        TextView tvEdad   = view.findViewById(R.id.tvEdad);
        TextView tvSexo   = view.findViewById(R.id.tvSexo);
        TextView tvTel    = view.findViewById(R.id.tvTelefono);
        TextView tvCorreo = view.findViewById(R.id.tvCorreo);

        try {
            tvEdad.setText(patient.getAge() + " años");
        } catch (Exception e) {
            tvEdad.setText(getString(R.string.detail_patient_value_placeholder));
        }
        tvSexo.setText(formatGenero(patient.getGender()));
        tvTel.setText(orPlaceholder(patient.getPhone()));
        tvCorreo.setText(orPlaceholder(patient.getEmail()));
    }

    private String formatGenero(String gender) {
        if (gender == null) return getString(R.string.detail_patient_value_placeholder);
        switch (gender) {
            case "M": return getString(R.string.create_patient_genero_m);
            case "F": return getString(R.string.create_patient_genero_f);
            case "O": return getString(R.string.create_patient_genero_o);
            default:  return getString(R.string.detail_patient_value_placeholder);
        }
    }

    private String orPlaceholder(String value) {
        return (value != null && !value.isEmpty()) ? value : getString(R.string.detail_patient_value_placeholder);
    }
}
