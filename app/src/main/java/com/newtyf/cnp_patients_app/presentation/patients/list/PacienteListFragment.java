package com.newtyf.cnp_patients_app.presentation.patients.list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newtyf.cnp_patients_app.NutritionistApp;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.model.Patient;
import com.newtyf.cnp_patients_app.data.repository.PatientRepository;
import com.newtyf.cnp_patients_app.data.session.SessionManager;
import com.newtyf.cnp_patients_app.presentation.patients.create.PatientCreateFragment;
import com.newtyf.cnp_patients_app.presentation.patients.detail.PatientDetailFragment;

import java.util.List;

public class PacienteListFragment extends Fragment {

    private LinearLayout containerCards;
    private PatientRepository repository;

    public static PacienteListFragment newInstance() {
        return new PacienteListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paciente_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new PatientRepository(requireContext());

        containerCards = view.findViewById(R.id.containerCards);

        ImageButton btnPerfil = view.findViewById(R.id.btnPerfil);
        // TODO: navegar a perfil de nutricionista

        // TODO: filtrar lista al escribir
        // view.findViewById(R.id.etBuscar)

        FloatingActionButton fab = view.findViewById(R.id.fabCreatePatient);
        fab.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, PatientCreateFragment.newInstance())
                .addToBackStack(null)
                .commit());
    }

    @Override
    public void onResume() {
        super.onResume();
        containerCards.removeAllViews();
        loadCards();
    }

    private void loadCards() {
        List<Patient> lista = repository.getAll(SessionManager.getCurrentId());

        for (Patient p : lista) {
            View card = LayoutInflater.from(getContext()).inflate(R.layout.item_patient, containerCards, false);

            TextView tvName = card.findViewById(R.id.tvPatientName);
            Chip chipDni = card.findViewById(R.id.chipDni);
            Chip chipAge = card.findViewById(R.id.chipAge);
            TextView tvLastConsult = card.findViewById(R.id.tvLastConsult);
            ImageButton btnMenuPatient = card.findViewById(R.id.btnMenuPatient);

            tvName.setText(p.getFirstName() + " " + p.getLastName());
            chipAge.setText(p.getAge() + " años");
            chipDni.setText("DNI: " + p.getDni());
            tvLastConsult.setText(p.getBirthDate());

            btnMenuPatient.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, PatientDetailFragment.newInstance(p.getId()))
                    .addToBackStack(null)
                    .commit());

            containerCards.addView(card);
        }
    }
}