package com.newtyf.cnp_patients_app.presentation.patients.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.newtyf.cnp_patients_app.NutritionistApp;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.model.Patient;

public class PatientEditFragment extends Fragment {

    private static final String ARG_DNI = "dni";

    private String dni;
    private Patient patient;

    private EditText txtName, txtDni, txtEmail, txtPhone, txtBirthDate, txtObjective;

    public static PatientEditFragment newInstance(String dni) {
        PatientEditFragment fragment = new PatientEditFragment();
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
        return inflater.inflate(R.layout.fragment_patient_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtName = view.findViewById(R.id.txtNombrePac);
        txtDni = view.findViewById(R.id.txtDniPac);
        txtEmail = view.findViewById(R.id.txtEmailPac);
        txtPhone = view.findViewById(R.id.txtTelefonoPac);
        txtBirthDate = view.findViewById(R.id.txtFechaNacPac);
        txtObjective = view.findViewById(R.id.txtObjetivoPac);

        patient = Patient.getByDni(NutritionistApp.pacientes, dni);
        if (patient != null) {
            txtName.setText(patient.getName());
            txtDni.setText(patient.getDni());
            txtEmail.setText(patient.getEmail());
            txtPhone.setText(patient.getPhone());
            txtBirthDate.setText(patient.getBirthDate());
            txtObjective.setText(patient.getObjective());
        }

        view.findViewById(R.id.btnSave).setOnClickListener(v -> savePatient());
        view.findViewById(R.id.btnBack).setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void savePatient() {
        String name = txtName.getText().toString().trim();
        String dniVal = txtDni.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String birthDate = txtBirthDate.getText().toString().trim();
        String objective = txtObjective.getText().toString().trim();

        if (name.isEmpty() || dniVal.isEmpty()) {
            Toast.makeText(getContext(), "Name and DNI are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Patient updated = new Patient(name, dniVal, email, phone, birthDate, objective);
        NutritionistApp.pacientes.set(NutritionistApp.pacientes.indexOf(patient), updated);
        Toast.makeText(getContext(), "Patient updated", Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
