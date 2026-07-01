package com.newtyf.cnp_patients_app.presentation.consultations.register;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.model.AnthropometricRecord;
import com.newtyf.cnp_patients_app.data.model.Patient;
import com.newtyf.cnp_patients_app.data.repository.AnthropometricRepository;
import com.newtyf.cnp_patients_app.data.repository.PatientRepository;

public class ConsultationStep2Fragment extends Fragment {

    private static final String ARG_PATIENT_ID      = "patient_id";
    private static final String ARG_CONSULTATION_ID = "consultation_id";


    private TextInputEditText etPeso, etEstatura, etGrasa, etMusculo;
    private AutoCompleteTextView spinnerFormula, spinnerNaf;
    private com.google.android.material.textfield.TextInputLayout tilPeso, tilEstatura, tilGrasa, tilMusculo, tilFormula, tilNaf;
    private TextView tvImc, tvImcCategoria, tvTmb, tvTmbFormula, tvGet;

    private PatientRepository patientRepository;
    private AnthropometricRepository anthropometricRepository;

    public static ConsultationStep2Fragment newInstance(String patientId, String consultationId) {
        ConsultationStep2Fragment fragment = new ConsultationStep2Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PATIENT_ID, patientId);
        args.putString(ARG_CONSULTATION_ID, consultationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_consultation_step2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        patientRepository         = new PatientRepository(requireContext());
        anthropometricRepository  = new AnthropometricRepository(requireContext());

        String patientId      = getArguments() != null ? getArguments().getString(ARG_PATIENT_ID) : null;
        String consultationId = getArguments() != null ? getArguments().getString(ARG_CONSULTATION_ID) : null;

        etPeso      = view.findViewById(R.id.etPeso);
        etEstatura  = view.findViewById(R.id.etEstatura);
        etGrasa     = view.findViewById(R.id.etGrasa);
        etMusculo   = view.findViewById(R.id.etMusculo);
        spinnerFormula = view.findViewById(R.id.spinnerFormula);
        spinnerNaf     = view.findViewById(R.id.spinnerNaf);
        tilPeso        = view.findViewById(R.id.tilPeso);
        tilEstatura    = view.findViewById(R.id.tilEstatura);
        tilGrasa       = view.findViewById(R.id.tilGrasa);
        tilMusculo     = view.findViewById(R.id.tilMusculo);
        tilFormula     = view.findViewById(R.id.tilFormula);
        tilNaf         = view.findViewById(R.id.tilNaf);
        tvImc          = view.findViewById(R.id.tvImc);
        tvImcCategoria = view.findViewById(R.id.tvImcCategoria);
        tvTmb          = view.findViewById(R.id.tvTmb);
        tvTmbFormula   = view.findViewById(R.id.tvTmbFormula);
        tvGet          = view.findViewById(R.id.tvGet);

        cargarPaciente(view, patientId);
        setupSpinners();
        setupWatchers();

        view.findViewById(R.id.btnDiagnostico).setOnClickListener(v -> {
            boolean result = save(consultationId);
            if (result) ((ConsultationRegisterActivity) requireActivity()).goToStep3(patientId, consultationId);
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

    private void setupSpinners() {
        String[] formulas = getResources().getStringArray(R.array.bmr_formulas);
        spinnerFormula.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, formulas));
        spinnerFormula.setOnItemClickListener((p, v, pos, id) -> { tilFormula.setError(null); recalcular(); });

        String[] niveles = getResources().getStringArray(R.array.niveles_actividad);
        spinnerNaf.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, niveles));
        spinnerNaf.setOnItemClickListener((p, v, pos, id) -> { tilNaf.setError(null); recalcular(); });
    }

    private void setupWatchers() {
        etPeso.addTextChangedListener(clearAndRecalculate(tilPeso));
        etEstatura.addTextChangedListener(clearAndRecalculate(tilEstatura));
        etGrasa.addTextChangedListener(clearAndRecalculate(tilGrasa));
        etMusculo.addTextChangedListener(clearAndRecalculate(tilMusculo));
    }

    private void recalcular() {
        Double peso     = parseDouble(etPeso);
        Double estatura = parseDouble(etEstatura);
        Double grasa    = parseDouble(etGrasa);

        if (peso != null && estatura != null && estatura > 0) {
            double imc = AnthropometricRecord.calcularBmi(peso, estatura);
            tvImc.setText(String.format("%.1f", imc));
            tvImcCategoria.setText(AnthropometricRecord.categoriaImc(imc));
        } else {
            tvImc.setText("—");
            tvImcCategoria.setText("—");
        }

        String formula = spinnerFormula.getText().toString();
        Double tmb = (peso != null && estatura != null)
                ? AnthropometricRecord.calcularBmr(formula, peso, estatura, grasa)
                : null;

        if (tmb != null) {
            tvTmb.setText(String.format("%.0f", tmb));
            tvTmbFormula.setText(formula);
            int nafPos = nafPosition();
            if (nafPos != -1) {
                double get = AnthropometricRecord.calcularTdee(tmb, AnthropometricRecord.NAF_FACTORES[nafPos]);
                tvGet.setText(String.format("%.0f", get));
            } else {
                tvGet.setText("—");
            }
        } else {
            tvTmb.setText("—");
            tvGet.setText("—");
        }
    }

    private int nafPosition() {
        String[] niveles = getResources().getStringArray(R.array.niveles_actividad);
        String selected = spinnerNaf.getText().toString();
        for (int i = 0; i < niveles.length; i++) {
            if (niveles[i].equals(selected)) return i;
        }
        return -1;
    }

    private boolean save(String consultationId) {
        Double peso     = parseDouble(etPeso);
        Double estatura = parseDouble(etEstatura);
        Double grasa    = parseDouble(etGrasa);
        String formula  = spinnerFormula.getText().toString().trim();
        int nafPos      = nafPosition();
        boolean katch   = formula.equals("Katch-McArdle");

        boolean valido = true;

        if (peso == null)           { tilPeso.setError(getString(R.string.error_campo_requerido));     valido = false; } else { tilPeso.setError(null); }
        if (estatura == null)       { tilEstatura.setError(getString(R.string.error_campo_requerido)); valido = false; } else { tilEstatura.setError(null); }
        if (formula.isEmpty())      { tilFormula.setError(getString(R.string.error_campo_requerido));  valido = false; } else { tilFormula.setError(null); }
        if (katch && grasa == null) { tilGrasa.setError(getString(R.string.error_campo_requerido));    valido = false; } else { tilGrasa.setError(null); }
        if (nafPos == -1)           { tilNaf.setError(getString(R.string.error_campo_requerido));      valido = false; } else { tilNaf.setError(null); }

        if (!valido) return valido;

        AnthropometricRecord record = new AnthropometricRecord();
        record.setConsultationId(consultationId);
        record.setWeightKg(peso);
        record.setHeightCm(estatura);
        record.setBodyFatPct(grasa);
        record.setMuscleMassPct(parseDouble(etMusculo));
        record.setBmi(AnthropometricRecord.calcularBmi(peso, estatura));
        record.setBmrFormula(formula);

        Double tmb = AnthropometricRecord.calcularBmr(formula, peso, estatura, grasa);
        record.setBmrKcal(tmb);

        double nafFactor = AnthropometricRecord.NAF_FACTORES[nafPos];
        record.setActivityFactor(nafFactor);
        record.setTdeeKcal(AnthropometricRecord.calcularTdee(tmb, nafFactor));

        anthropometricRepository.insert(record);
        return valido;
    }

    private TextWatcher clearAndRecalculate(com.google.android.material.textfield.TextInputLayout til) {
        return new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { til.setError(null); recalcular(); }
        };
    }

    private Double parseDouble(TextInputEditText et) {
        try {
            String s = et.getText() != null ? et.getText().toString().trim() : "";
            return s.isEmpty() ? null : Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
