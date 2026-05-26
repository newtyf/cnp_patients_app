package com.newtyf.cnp_patients_app.presentation.patients.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.newtyf.cnp_patients_app.NutritionistApp;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.model.Patient;
import com.newtyf.cnp_patients_app.presentation.patients.create.PatientCreateFragment;
import com.newtyf.cnp_patients_app.presentation.patients.edit.PatientEditFragment;

public class PatientDetailFragment extends Fragment {

    private static final String ARG_DNI = "dni";

    private String dni;
    private Patient patient;

    private TextView tvDetNombre, tvDetDni, tvDetEmail, tvDetPhone, tvDetBirthDate, tvDetObjective;

    public static PatientDetailFragment newInstance(String dni) {
        PatientDetailFragment fragment = new PatientDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DNI, dni);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            dni = getArguments().getString(ARG_DNI);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvDetNombre = view.findViewById(R.id.tvDetNombre);
        tvDetDni = view.findViewById(R.id.tvDetDni);
        tvDetEmail = view.findViewById(R.id.tvDetEmail);
        tvDetPhone = view.findViewById(R.id.tvDetTelefono);
        tvDetBirthDate = view.findViewById(R.id.tvDetFechaNac);
        tvDetObjective = view.findViewById(R.id.tvDetObjetivo);

        view.findViewById(R.id.btnEdit).setOnClickListener(v -> navigateToEdit());
        view.findViewById(R.id.btnDelete).setOnClickListener(v -> deletePatient());
        view.findViewById(R.id.btnBack).setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());

        loadPatient();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPatient();
    }

    private void loadPatient() {
        patient = Patient.getByDni(NutritionistApp.pacientes, dni);
        if (patient != null) {
            tvDetNombre.setText("Name: " + patient.getName());
            tvDetDni.setText("DNI: " + patient.getDni());
            tvDetEmail.setText("Email: " + patient.getEmail());
            tvDetPhone.setText("Phone: " + patient.getPhone());
            tvDetBirthDate.setText("Birth date: " + patient.getBirthDate());
            tvDetObjective.setText("Objective: " + patient.getObjective());
        }
    }

    private void navigateToEdit() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, PatientEditFragment.newInstance(patient.getDni()))
                .addToBackStack(null)
                .commit();
    }

    private void deletePatient() {
        NutritionistApp.pacientes.remove(patient);
        Toast.makeText(getContext(), "Patient deleted", Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
