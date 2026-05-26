package com.newtyf.cnp_patients_app.presentation.patients.create;

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

public class PatientCreateFragment extends Fragment {

    private EditText txtName, txtDni, txtEmail, txtPhone, txtBirthDate, txtObjective;

    public static PatientCreateFragment newInstance() {
        return new PatientCreateFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_create, container, false);
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

        view.findViewById(R.id.btnSave).setOnClickListener(v -> savePatient());
        view.findViewById(R.id.btnBack).setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());
    }

    private void savePatient() {
        String name = txtName.getText().toString().trim();
        String dni = txtDni.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String phone = txtPhone.getText().toString().trim();
        String birthDate = txtBirthDate.getText().toString().trim();
        String objective = txtObjective.getText().toString().trim();

        if (name.isEmpty() || dni.isEmpty()) {
            Toast.makeText(getContext(), "Name and DNI are required", Toast.LENGTH_SHORT).show();
            return;
        }

        NutritionistApp.pacientes.add(new Patient(name, dni, email, phone, birthDate, objective));
        Toast.makeText(getContext(), "Patient created", Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
