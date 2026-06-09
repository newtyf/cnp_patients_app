package com.newtyf.cnp_patients_app.presentacion.consultations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.newtyf.cnp_patients_app.R;

public class FragmentoPaso1Antecedentes extends Fragment {

    private TextView tvNombrePaciente, tvDatosPaciente;

    public FragmentoPaso1Antecedentes() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_paso1_antecedentes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referencias a las vistas
        tvNombrePaciente = view.findViewById(R.id.tv_nombre_paciente);
        tvDatosPaciente = view.findViewById(R.id.tv_datos_paciente);
        MaterialCardView cardPaciente = view.findViewById(R.id.card_seleccionar_paciente);
        MaterialButton btnSiguiente = view.findViewById(R.id.btn_siguiente_1);

        // Configuración inicial de la tarjeta
        tvNombrePaciente.setText("Seleccionar Paciente");
        tvDatosPaciente.setText("Toque aquí para buscar");

        // Configurar los menús desplegables
        configurarMenuSueno(view);

        // Evento para abrir el buscador de pacientes
        cardPaciente.setOnClickListener(v -> abrirBuscadorPacientes());

        // Navegación hacia el Paso 2
        btnSiguiente.setOnClickListener(v -> {
            if (getActivity() instanceof ActivityPasosConsulta) {
                ((ActivityPasosConsulta) getActivity()).cargarFragmento(
                        new FragmentoPaso2Medidas(),
                        getString(R.string.paso_indicador, 2)
                );
            }
        });
    }

    // 🔥 NUEVO MÉTODO: Alimenta el menú desplegable con opciones
    private void configurarMenuSueno(View view) {
        MaterialAutoCompleteTextView actvCalidadSueno = view.findViewById(R.id.actv_calidad_sueno);

        // Opciones que aparecerán en el menú
        String[] opcionesCalidad = new String[]{"Excelente", "Buena", "Regular", "Mala", "Muy Mala"};

        // Creamos el adaptador y lo unimos al menú
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                opcionesCalidad
        );

        actvCalidadSueno.setAdapter(adapter);
    }

    private void abrirBuscadorPacientes() {
        DialogoSeleccionarPaciente dialogo = new DialogoSeleccionarPaciente();
        dialogo.setListener(paciente -> {
            tvNombrePaciente.setText(paciente.getNombreCompleto());

            String identificacion = paciente.idNumber != null ? paciente.idNumber : "Sin DNI";
            String genero = paciente.gender != null ? paciente.gender : "No especificado";
            tvDatosPaciente.setText(String.format("ID: %s • %s", identificacion, genero));

            if (getActivity() instanceof ActivityPasosConsulta) {
                ((ActivityPasosConsulta) getActivity()).setPacienteSeleccionado(paciente.id);
            }

            dialogo.dismiss();
        });

        dialogo.show(getChildFragmentManager(), "DialogoBusquedaPacientes");
    }
}