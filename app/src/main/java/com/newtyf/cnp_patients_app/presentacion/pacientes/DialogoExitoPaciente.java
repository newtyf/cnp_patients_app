package com.newtyf.cnp_patients_app.presentacion.pacientes;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.newtyf.cnp_patients_app.R;

public class DialogoExitoPaciente extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new MaterialAlertDialogBuilder(requireContext())
                .setView(R.layout.dialogo_exito_paciente)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                })
                .create();
    }
}