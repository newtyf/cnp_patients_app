package com.newtyf.cnp_patients_app.presentation.consultations.register;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.model.Consultation;
import com.newtyf.cnp_patients_app.data.model.Patient;
import com.newtyf.cnp_patients_app.data.repository.ConsultationRepository;
import com.newtyf.cnp_patients_app.data.repository.PatientRepository;

public class ConsultationStep1Fragment extends Fragment {

    private static final String ARG_PATIENT_ID = "patient_id";

    private PatientRepository patientRepository;
    private ConsultationRepository consultationRepository;

    public static ConsultationStep1Fragment newInstance(String patientId) {
        ConsultationStep1Fragment fragment = new ConsultationStep1Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PATIENT_ID, patientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consultation_step1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        patientRepository = new PatientRepository(requireContext());
        consultationRepository = new ConsultationRepository(requireContext());

        String patientId = getArguments() != null ? getArguments().getString(ARG_PATIENT_ID) : null;

        cargarPaciente(view, patientId);
        setupSpinner(view);

        view.findViewById(R.id.btnCancelar).setOnClickListener(v -> requireActivity().finish());

        view.findViewById(R.id.btnSiguiente).setOnClickListener(v -> {
            AutoCompleteTextView spinnerTipo = view.findViewById(R.id.spinnerTipo);
            TextInputEditText etMotivo = view.findViewById(R.id.etMotivo);
            TextInputEditText etNotas  = view.findViewById(R.id.etNotas);
            TextInputLayout tilTipo    = view.findViewById(R.id.tilTipo);
            TextInputLayout tilMotivo  = view.findViewById(R.id.tilMotivo);
            TextInputLayout tilNotas   = view.findViewById(R.id.tilNotas);

            String tipo   = spinnerTipo.getText().toString().trim();
            String motivo = etMotivo.getText() != null ? etMotivo.getText().toString().trim() : "";
            String notas  = etNotas.getText() != null ? etNotas.getText().toString().trim() : "";

            boolean valido = true;

            if (tipo.isEmpty())   { tilTipo.setError(getString(R.string.error_campo_requerido));   valido = false; } else { tilTipo.setError(null); }
            if (motivo.isEmpty()) { tilMotivo.setError(getString(R.string.error_campo_requerido)); valido = false; } else { tilMotivo.setError(null); }
            if (notas.isEmpty())  { tilNotas.setError(getString(R.string.error_campo_requerido));  valido = false; } else { tilNotas.setError(null); }

            if (!valido) return;

            Consultation consulta = new Consultation();
            consulta.setPatientId(patientId);
            consulta.setType(tipo);
            consulta.setReason(motivo);
            consulta.setNotes(notas);
            consulta.setDate(java.time.LocalDate.now().toString());

            consultationRepository.insert(consulta);

            ((ConsultationRegisterActivity) requireActivity()).goToStep2(patientId, consulta.getId());
        });
    }

    private void cargarPaciente(View view, String patientId) {
        if (patientId == null) return;
        Patient patient = patientRepository.getById(patientId);
        if (patient == null) return;

        ((TextView) view.findViewById(R.id.tvNombrePaciente)).setText(patient.getFullName());

        String info = "ID: " + (patient.getDni() != null ? patient.getDni() : "—");
        try { info += " • " + patient.getAge() + " años"; } catch (Exception ignored) {}
        if (patient.getGenderLabel() != null) info += " • " + patient.getGenderLabel();
        ((TextView) view.findViewById(R.id.tvInfoPaciente)).setText(info);
    }

    private void setupSpinner(View view) {
        AutoCompleteTextView spinner = view.findViewById(R.id.spinnerTipo);
        String[] tipos = getResources().getStringArray(R.array.tipos_consulta);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, tipos);
        spinner.setAdapter(adapter);
    }
}
