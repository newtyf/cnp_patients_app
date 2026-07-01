package com.newtyf.cnp_patients_app.presentation.consultations.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.model.AnthropometricRecord;
import com.newtyf.cnp_patients_app.data.model.Patient;
import com.newtyf.cnp_patients_app.data.repository.AnthropometricRepository;
import com.newtyf.cnp_patients_app.data.repository.PatientRepository;


public class ConsultationStep3Fragment extends Fragment {

    private static final String ARG_PATIENT_ID      = "patient_id";
    private static final String ARG_CONSULTATION_ID = "consultation_id";

    private TextView tvImc, tvTmb, tvIdealWeight;

    private PatientRepository patientRepository;
    private AnthropometricRepository anthropometricRepository;

    public static ConsultationStep3Fragment newInstance(String param1, String param2) {
        ConsultationStep3Fragment fragment = new ConsultationStep3Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PATIENT_ID, param1);
        args.putString(ARG_CONSULTATION_ID, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consultation_step3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        patientRepository         = new PatientRepository(requireContext());
        anthropometricRepository  = new AnthropometricRepository(requireContext());

        String patientId      = getArguments() != null ? getArguments().getString(ARG_PATIENT_ID) : null;
        String consultationId = getArguments() != null ? getArguments().getString(ARG_CONSULTATION_ID) : null;

        tvImc     = view.findViewById(R.id.tvCurrentImc);
        tvTmb     = view.findViewById(R.id.tvTMB);
        tvIdealWeight     = view.findViewById(R.id.tvIdealWeight);


        loadPatient(view, patientId);
        loadAnthropometric(view, consultationId);

        view.findViewById(R.id.btnFinish).setOnClickListener(v -> {
            requireActivity().finish();
        });
    }

    private void loadPatient(View view, String patientId) {
        if (patientId == null) return;
        Patient patient = patientRepository.getById(patientId);
        if (patient == null) return;

        ((TextView) view.findViewById(R.id.tvNombrePaciente)).setText(patient.getFullName());

        String info = "ID: " + (patient.getDni() != null ? patient.getDni() : "—");
        try { info += " • " + patient.getAge() + " años"; } catch (Exception ignored) {}
        if (patient.getGenderLabel() != null) info += " • " + patient.getGenderLabel();
        ((TextView) view.findViewById(R.id.tvInfoPaciente)).setText(info);
    }

    private void loadAnthropometric(View view, String consultationId) {
        if (consultationId == null) return;
        AnthropometricRecord anthropometricRecord = anthropometricRepository.getByConsultation(consultationId);
        if (anthropometricRecord == null) return;

        tvImc.setText(String.format("%.0f", anthropometricRecord.getBmi()));
        tvIdealWeight.setText(String.format("%.0f", anthropometricRecord.calcularIdealWeight()));
        tvTmb.setText(String.format("%.0f", anthropometricRecord.getBmrKcal()));


    }


}