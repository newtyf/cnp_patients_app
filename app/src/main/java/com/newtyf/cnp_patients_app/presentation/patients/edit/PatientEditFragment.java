package com.newtyf.cnp_patients_app.presentation.patients.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.model.Patient;
import com.newtyf.cnp_patients_app.data.repository.PatientRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PatientEditFragment extends Fragment {

    private static final String ARG_ID = "patient_id";

    private TextInputLayout tfNombre, tfApellido, tfIdNumber, tfFechaNac, tfTelefono, tfEmail, tfNotas;
    private RadioGroup rgGenero;
    private PatientRepository repository;
    private Patient patient;

    public static PatientEditFragment newInstance(String patientId) {
        PatientEditFragment fragment = new PatientEditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, patientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new PatientRepository(requireContext());

        tfNombre   = view.findViewById(R.id.tfNombre);
        tfApellido = view.findViewById(R.id.tfApellido);
        tfIdNumber = view.findViewById(R.id.tfIdNumber);
        tfFechaNac = view.findViewById(R.id.tfFechaNac);
        tfTelefono = view.findViewById(R.id.tfTelefono);
        tfEmail    = view.findViewById(R.id.tfEmail);
        tfNotas    = view.findViewById(R.id.tfNotas);
        rgGenero   = view.findViewById(R.id.rgGenero);

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        view.findViewById(R.id.btnCancelar).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        view.findViewById(R.id.btnGuardar).setOnClickListener(v -> guardarCambios());

        setupDatePicker();
        cargarPaciente();
    }

    private void cargarPaciente() {
        String patientId = getArguments() != null ? getArguments().getString(ARG_ID) : null;
        if (patientId == null) return;

        patient = repository.getById(patientId);
        if (patient == null) return;

        setText(tfNombre, patient.getFirstName());
        setText(tfApellido, patient.getLastName());
        setText(tfIdNumber, patient.getDni());
        setText(tfFechaNac, patient.getBirthDate());
        setText(tfTelefono, patient.getPhone());
        setText(tfEmail, patient.getEmail());
        setText(tfNotas, patient.getNotes());

        if ("M".equals(patient.getGender()))      rgGenero.check(R.id.rbMasculino);
        else if ("F".equals(patient.getGender())) rgGenero.check(R.id.rbFemenino);
        else if ("O".equals(patient.getGender())) rgGenero.check(R.id.rbOtro);
    }

    private void guardarCambios() {
        if (patient == null) return;

        String nombre   = getText(tfNombre);
        String apellido = getText(tfApellido);
        String fecha    = getText(tfFechaNac);

        if (nombre.isEmpty()) {
            tfNombre.setError(getString(R.string.error_campo_requerido));
            return;
        }
        if (apellido.isEmpty()) {
            tfApellido.setError(getString(R.string.error_campo_requerido));
            return;
        }
        if (fecha.isEmpty()) {
            tfFechaNac.setError(getString(R.string.error_campo_requerido));
            return;
        }

        tfNombre.setError(null);
        tfApellido.setError(null);
        tfFechaNac.setError(null);

        patient.setFirstName(nombre);
        patient.setLastName(apellido);
        patient.setDni(getText(tfIdNumber).isEmpty() ? null : getText(tfIdNumber));
        patient.setBirthDate(fecha);
        patient.setGender(getGeneroSeleccionado());
        patient.setPhone(getText(tfTelefono));
        patient.setEmail(getText(tfEmail));
        patient.setNotes(getText(tfNotas));

        repository.update(patient);
        Toast.makeText(requireContext(), R.string.edit_patient_success, Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void setupDatePicker() {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.create_patient_label_fecha)
                .setCalendarConstraints(new CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointBackward.now())
                        .build())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(new Date(selection));
            if (tfFechaNac.getEditText() != null) {
                tfFechaNac.getEditText().setText(fecha);
            }
        });

        tfFechaNac.setEndIconOnClickListener(v ->
                datePicker.show(getParentFragmentManager(), "date_picker"));

        tfFechaNac.getEditText().setOnClickListener(v ->
                datePicker.show(getParentFragmentManager(), "date_picker"));
    }

    private String getGeneroSeleccionado() {
        int checkedId = rgGenero.getCheckedRadioButtonId();
        if (checkedId == R.id.rbMasculino) return "M";
        if (checkedId == R.id.rbFemenino)  return "F";
        if (checkedId == R.id.rbOtro)      return "O";
        return null;
    }

    private String getText(TextInputLayout tf) {
        if (tf.getEditText() == null) return "";
        return tf.getEditText().getText().toString().trim();
    }

    private void setText(TextInputLayout tf, String value) {
        if (tf.getEditText() != null && value != null) {
            tf.getEditText().setText(value);
        }
    }
}
