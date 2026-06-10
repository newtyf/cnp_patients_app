package com.newtyf.cnp_patients_app.presentation.patients.detail;

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
import com.newtyf.cnp_patients_app.presentation.patients.edit.PatientEditFragment;

public class PatientDetailFragment extends Fragment {

    private static final String ARG_ID = "patient_id";

    private TextView tvNombreCompleto, tvDni, tvEdad, tvSexo, tvTelefono, tvCorreo;
    private PatientRepository repository;

    public static PatientDetailFragment newInstance(String patientId) {
        PatientDetailFragment fragment = new PatientDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, patientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new PatientRepository(requireContext());

        tvNombreCompleto = view.findViewById(R.id.tvNombreCompleto);
        tvDni            = view.findViewById(R.id.tvDni);
        tvEdad           = view.findViewById(R.id.tvEdad);
        tvSexo           = view.findViewById(R.id.tvSexo);
        tvTelefono       = view.findViewById(R.id.tvTelefono);
        tvCorreo         = view.findViewById(R.id.tvCorreo);

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        String patientId = getArguments() != null ? getArguments().getString(ARG_ID) : null;

        view.findViewById(R.id.btnEditar).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, PatientEditFragment.newInstance(patientId))
                        .addToBackStack(null)
                        .commit());

        cargarPaciente();
    }

    private void cargarPaciente() {
        String patientId = getArguments() != null ? getArguments().getString(ARG_ID) : null;
        if (patientId == null) return;

        Patient patient = repository.getById(patientId);
        if (patient == null) return;

        tvNombreCompleto.setText(patient.getFullName());
        tvDni.setText(patient.getDni() != null ? patient.getDni() : getString(R.string.detail_patient_value_placeholder));
        try {
            tvEdad.setText(patient.getAge() + " años");
        } catch (Exception e) {
            tvEdad.setText(getString(R.string.detail_patient_value_placeholder));
        }
        tvSexo.setText(formatGenero(patient.getGender()));
        tvTelefono.setText(orPlaceholder(patient.getPhone()));
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
