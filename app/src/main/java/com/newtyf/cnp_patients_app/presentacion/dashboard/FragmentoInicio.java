package com.newtyf.cnp_patients_app.presentacion.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.BaseDatosCnp;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadPaciente;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadUsuario;
import com.newtyf.cnp_patients_app.domain.models.ConsultaDTO;
import com.newtyf.cnp_patients_app.presentacion.consultations.ActivityListaConsultas;
import com.newtyf.cnp_patients_app.presentacion.consultations.ActivityPasosConsulta; // 🔥 Importación actualizada al flujo de 3 pasos
import com.newtyf.cnp_patients_app.presentacion.pacientes.ActivityCrearPaciente;
import com.newtyf.cnp_patients_app.presentacion.pacientes.ActivityDetallePaciente;
import com.newtyf.cnp_patients_app.presentacion.pacientes.ActivityListaPacientes;
import com.newtyf.cnp_patients_app.presentacion.profile.ActivityPerfil;
import com.newtyf.cnp_patients_app.presentacion.diet.ActivityGeneradorDieta;

import java.util.List;

public class FragmentoInicio extends Fragment {

    // Solo declaramos globalmente las vistas que necesitan ser actualizadas dinámicamente
    private RecyclerView rvCitasHoy;
    private RecyclerView rvPacientesRecientes;
    private TextView tvCountPacientes;
    private TextView tvCountConsultas;
    private TextView tvSaludo;

    private BaseDatosCnp dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_inicio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = BaseDatosCnp.obtenerBaseDatos(requireContext());

        inicializarVistas(view);
        configurarRecyclerViews();
        configurarEventosClic(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Se elimina la llamada redundante en onViewCreated. onResume es el único punto de entrada de carga.
        cargarDatosDelDashboard();
    }

    private void inicializarVistas(View view) {
        rvCitasHoy = view.findViewById(R.id.rv_citas_hoy);
        rvPacientesRecientes = view.findViewById(R.id.rv_pacientes_recientes);
        tvCountPacientes = view.findViewById(R.id.tvCountPacientes);
        tvCountConsultas = view.findViewById(R.id.tvCountConsultas);
        tvSaludo = view.findViewById(R.id.tvSaludo);
    }

    private void configurarRecyclerViews() {
        rvCitasHoy.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        rvPacientesRecientes.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        // Optimización de rendimiento de UI para M3
        rvCitasHoy.setHasFixedSize(true);
        rvPacientesRecientes.setHasFixedSize(true);
    }

    private void configurarEventosClic(View view) {
        // Reducción drástica de código (DRY) mediante la eliminación de variables globales innecesarias
        view.findViewById(R.id.btnAccionNuevoPac).setOnClickListener(v -> navegarA(ActivityCrearPaciente.class));

        // Cambia la línea de ActivityDetalleConsulta por ActivityPasosConsulta
        view.findViewById(R.id.btnAccionNuevaConsulta).setOnClickListener(v -> navegarA(com.newtyf.cnp_patients_app.presentacion.consultations.ActivityPasosConsulta.class));

        view.findViewById(R.id.btnAccionDietas).setOnClickListener(v -> navegarA(ActivityGeneradorDieta.class));
        view.findViewById(R.id.card_pacientes_activos).setOnClickListener(v -> navegarA(ActivityListaPacientes.class));
        view.findViewById(R.id.card_consultas_mes).setOnClickListener(v -> navegarA(ActivityListaConsultas.class));

        View btnPerfil = view.findViewById(R.id.card_profile_image);
        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(v -> navegarA(ActivityPerfil.class));
        }
    }

    private void navegarA(Class<?> activityDestino) {
        startActivity(new Intent(requireContext(), activityDestino));
    }

    private void cargarDatosDelDashboard() {
        BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
            try {
                // Extracción de BD
                List<ConsultaDTO> listaCitas = dbHelper.daoConsulta().obtenerConsultasDeHoy();
                List<EntidadPaciente> listaPacientes = dbHelper.daoPaciente().obtenerPacientesRecientes();

                // Procesamiento de Usuario (Sin Hardcodeo directo en la lógica)
                String emailNutricionista = getString(R.string.default_admin_email);
                EntidadUsuario usuarioActivo = dbHelper.daoUsuario().obtenerUsuarioPorEmail(emailNutricionista);

                String nombreRecuperado = (usuarioActivo != null && usuarioActivo.name != null)
                        ? usuarioActivo.name
                        : getString(R.string.default_nutritionist_name);

                // Prevención absoluta de Memory Leaks y NullPointerExceptions
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> actualizarInterfaz(nombreRecuperado, listaCitas, listaPacientes));
                }
            } catch (Exception e) {
                // En un entorno de producción, aquí se integra Crashlytics o Sentry.
                e.printStackTrace();
            }
        });
    }

    private void actualizarInterfaz(String nombre, List<ConsultaDTO> citas, List<EntidadPaciente> pacientes) {
        if (tvSaludo != null) {
            tvSaludo.setText(getString(R.string.saludo_usuario, nombre));
        }

        // Renovación de adaptadores
        rvCitasHoy.setAdapter(new AdaptadorConsultas(citas));
        rvPacientesRecientes.setAdapter(new AdaptadorPacientesRecientes(pacientes, paciente -> {
            Intent intent = new Intent(requireContext(), ActivityDetallePaciente.class);
            intent.putExtra(getString(R.string.extra_patient_id), paciente.id);
            startActivity(intent);
        }));

        // Actualización de Métricas
        tvCountPacientes.setText(String.valueOf(pacientes.size()));
        tvCountConsultas.setText(String.valueOf(citas.size()));
    }
}