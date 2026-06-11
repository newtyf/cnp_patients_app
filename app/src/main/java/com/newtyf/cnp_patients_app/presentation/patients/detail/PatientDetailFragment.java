package com.newtyf.cnp_patients_app.presentation.patients.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.common.ui.TabPlaceholderFragment;
import com.newtyf.cnp_patients_app.data.model.Patient;
import com.newtyf.cnp_patients_app.data.repository.PatientRepository;
import com.newtyf.cnp_patients_app.presentation.consultations.list.ConsultationListFragment;
import com.newtyf.cnp_patients_app.presentation.patients.detail.tabs.PatientInfoFragment;
import com.newtyf.cnp_patients_app.presentation.patients.edit.PatientEditFragment;

public class PatientDetailFragment extends Fragment {

    private static final String ARG_ID = "patient_id";

    private PatientRepository repository;
    private String patientId;

    public static PatientDetailFragment newInstance(String patientId) {
        PatientDetailFragment fragment = new PatientDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, patientId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new PatientRepository(requireContext());
        patientId = getArguments() != null ? getArguments().getString(ARG_ID) : null;

        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        view.findViewById(R.id.btnEditar).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, PatientEditFragment.newInstance(patientId))
                        .addToBackStack(null)
                        .commit());

        cargarCabecera(view);
        setupTabs(view);
    }

    private void cargarCabecera(View view) {
        if (patientId == null) return;
        Patient patient = repository.getById(patientId);
        if (patient == null) return;

        ((TextView) view.findViewById(R.id.tvNombreCompleto)).setText(patient.getFullName());
        ((TextView) view.findViewById(R.id.tvDni)).setText(patient.getDni() != null
                ? patient.getDni()
                : getString(R.string.detail_patient_value_placeholder));
    }

    private void setupTabs(View view) {
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);

        showTab(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showTab(tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void showTab(int position) {
        Fragment fragment;
        switch (position) {
            case 0:  fragment = PatientInfoFragment.newInstance(patientId); break;
            case 1:  fragment = ConsultationListFragment.newInstance(patientId); break;
            default: fragment = TabPlaceholderFragment.newInstance(); break;
        }

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.tabContainer, fragment)
                .commit();
    }
}
