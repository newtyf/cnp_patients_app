package com.newtyf.cnp_patients_app.presentation.patients.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.newtyf.cnp_patients_app.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PatientCreateFragment extends Fragment {

    private TextInputLayout tfFechaNac;

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

        tfFechaNac = view.findViewById(R.id.tfFechaNac);

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        view.findViewById(R.id.btnCancelar).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        setupDatePicker();

        // TODO: btnGuardar — implementar cuando tengamos BD
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
}
