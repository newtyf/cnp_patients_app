package com.newtyf.cnp_patients_app.presentation.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.model.Consultation;
import com.newtyf.cnp_patients_app.data.model.Patient;
import com.newtyf.cnp_patients_app.data.repository.ConsultationRepository;
import com.newtyf.cnp_patients_app.data.repository.PatientRepository;
import com.newtyf.cnp_patients_app.data.session.SessionManager;
import com.newtyf.cnp_patients_app.presentation.patients.create.PatientCreateFragment;

import java.util.List;

public class DashboardFragment extends Fragment {

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String nutritionistId = SessionManager.getCurrentId();
        String nombre = SessionManager.getCurrentNutritionist() != null
                ? SessionManager.getCurrentNutritionist().getFullName()
                : "Licenciado(a)";

        int hora = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY);
        String saludo = hora >= 6 && hora < 20 ? "Buenos días, " : "Buenas noches, ";
        String icono  = hora >= 6 && hora < 20 ? "☀️" : "🌙";

        ((TextView) view.findViewById(R.id.tvSaludo)).setText(saludo + nombre);
        ((TextView) view.findViewById(R.id.tvSaludoIcono)).setText(icono);

        PatientRepository patientRepo           = new PatientRepository(requireContext());
        ConsultationRepository consultationRepo = new ConsultationRepository(requireContext());

        ((TextView) view.findViewById(R.id.tvCountPacientes)).setText(String.valueOf(patientRepo.count(nutritionistId)));
        ((TextView) view.findViewById(R.id.tvCountConsultas)).setText(String.valueOf(consultationRepo.countThisMonth(nutritionistId)));

        LinearLayout llConsultas = view.findViewById(R.id.llUltimasConsultas);
        List<Consultation> recientes = consultationRepo.getRecent(nutritionistId, 10);
        for (Consultation c : recientes) {
            View card = LayoutInflater.from(requireContext()).inflate(R.layout.item_dashboard_consulta, llConsultas, false);
            Patient patient = patientRepo.getById(c.getPatientId());
            ((TextView) card.findViewById(R.id.tvNombrePaciente)).setText(patient != null ? patient.getFullName() : "—");
            ((TextView) card.findViewById(R.id.tvFecha)).setText(c.getDate() != null ? c.getDate() : "—");
            ((TextView) card.findViewById(R.id.tvMotivo)).setText(c.getReason() != null ? c.getReason() : "—");
            llConsultas.addView(card);
        }

        view.findViewById(R.id.btnAccionNuevoPac).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, PatientCreateFragment.newInstance())
                        .addToBackStack(null)
                        .commit());

        view.findViewById(R.id.btnAccionNuevaConsulta).setOnClickListener(v -> {
            // TODO: navegar a lista de pacientes para seleccionar consulta
        });

        view.findViewById(R.id.btnAccionCalculadora).setOnClickListener(v -> {
            // TODO: calculadora de energía
        });

        view.findViewById(R.id.btnAccionExportar).setOnClickListener(v -> {
            // TODO: exportar dieta
        });
    }
}
