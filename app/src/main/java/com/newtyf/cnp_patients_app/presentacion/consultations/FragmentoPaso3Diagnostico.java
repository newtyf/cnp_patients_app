package com.newtyf.cnp_patients_app.presentacion.consultations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.BaseDatosCnp;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadConsulta;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class FragmentoPaso3Diagnostico extends Fragment {

    private TextView tvResumenImc, tvResumenPesoIdeal, tvResumenTmb;
    private TextInputEditText etRecomendaciones;

    public FragmentoPaso3Diagnostico() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_paso3_diagnostico, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarVistas(view);
        cargarResumenDesdeHost();

        MaterialButton btnFinalizar = view.findViewById(R.id.btn_finalizar_consulta);
        // Evento para guardar en base de datos y cerrar
        btnFinalizar.setOnClickListener(v -> guardarConsultaYFinalizar());
    }

    private void inicializarVistas(View view) {
        tvResumenImc = view.findViewById(R.id.tv_resumen_imc);
        tvResumenPesoIdeal = view.findViewById(R.id.tv_resumen_peso_ideal);
        tvResumenTmb = view.findViewById(R.id.tv_resumen_tmb);
        etRecomendaciones = view.findViewById(R.id.et_recomendaciones);
    }

    private void cargarResumenDesdeHost() {
        if (getActivity() instanceof ActivityPasosConsulta) {
            ActivityPasosConsulta host = (ActivityPasosConsulta) getActivity();

            // Traemos los datos que calculamos en el Paso 2
            double imc = host.getImcCalculado();
            double pesoIdeal = host.getPesoIdealCalculado();
            double tmb = host.getTmbCalculado();

            // Mostramos los datos formateados en las tarjetas grises
            tvResumenImc.setText(String.format(Locale.getDefault(), "%.1f", imc));
            tvResumenPesoIdeal.setText(String.format(Locale.getDefault(), "%.1f kg", pesoIdeal));
            tvResumenTmb.setText(String.format(Locale.getDefault(), "%,.0f kcal", tmb));
        }
    }

    private void guardarConsultaYFinalizar() {
        if (getActivity() instanceof ActivityPasosConsulta) {
            ActivityPasosConsulta host = (ActivityPasosConsulta) getActivity();
            String idPaciente = host.getIdPacienteSeleccionado();

            // Validación de seguridad
            if (idPaciente == null) {
                Toast.makeText(getContext(), "Error: Debe seleccionar un paciente en el Paso 1", Toast.LENGTH_LONG).show();
                return;
            }

            String recomendaciones = etRecomendaciones.getText() != null ? etRecomendaciones.getText().toString() : "";

            // Generamos la fecha y hora de este instante exacto
            String fechaActual = new SimpleDateFormat("dd/MM/yyyy, hh:mm a", Locale.getDefault()).format(new Date());

            // 🚀 GUARDADO EN BASE DE DATOS (Room en segundo plano)
            BaseDatosCnp db = BaseDatosCnp.obtenerBaseDatos(requireContext());
            BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
                EntidadConsulta nuevaConsulta = new EntidadConsulta();
                nuevaConsulta.id = UUID.randomUUID().toString();
                nuevaConsulta.patientId = idPaciente;
                nuevaConsulta.nutritionistId = "admin@cnp.pe"; // Tu nutricionista por defecto
                nuevaConsulta.date = fechaActual;
                nuevaConsulta.type = "Control Nutricional"; // Por defecto
                nuevaConsulta.reason = "Evaluación de rutina";
                nuevaConsulta.notes = recomendaciones; // Guardamos las recomendaciones en las notas de la BD
                nuevaConsulta.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

                try {
                    db.daoConsulta().insertarConsulta(nuevaConsulta);

                    // Volvemos al hilo principal para mostrar el mensaje y cerrar la pantalla
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(requireContext(), "¡Consulta registrada con éxito!", Toast.LENGTH_SHORT).show();
                        requireActivity().finish();
                    });
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Error al guardar la consulta", Toast.LENGTH_SHORT).show()
                    );
                }
            });
        }
    }
}