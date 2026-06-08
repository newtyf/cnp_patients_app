package com.newtyf.cnp_patients_app.presentacion.consultations;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.BaseDatosCnp;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadPaciente;
import java.util.Locale;

public class FragmentoPaso2Medidas extends Fragment {

    private TextInputEditText etPeso, etEstatura;
    private TextView tvValorImc, tvValorTmb, tvValorGet;
    private Chip chipEstadoImc;
    private MaterialAutoCompleteTextView actvNaf;

    // Variables del paciente obtenidas de la BD
    private String sexoPaciente = "Masculino";
    private int edadPaciente = 30; // Valores por defecto por si falta el dato

    public FragmentoPaso2Medidas() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_paso2_medidas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarVistas(view);
        cargarDatosPaciente();
        configurarMenuNaf();
        configurarCalculoEnVivo();

        MaterialButton btnSiguiente = view.findViewById(R.id.btn_siguiente_2);
        btnSiguiente.setOnClickListener(v -> guardarYContinuar());
    }

    private void inicializarVistas(View view) {
        etPeso = view.findViewById(R.id.et_peso);
        etEstatura = view.findViewById(R.id.et_estatura);
        tvValorImc = view.findViewById(R.id.tv_valor_imc);
        tvValorTmb = view.findViewById(R.id.tv_valor_tmb);
        tvValorGet = view.findViewById(R.id.tv_valor_get);
        chipEstadoImc = view.findViewById(R.id.chip_estado_imc);
        actvNaf = view.findViewById(R.id.actv_naf);
    }

    private void cargarDatosPaciente() {
        if (getActivity() instanceof ActivityPasosConsulta) {
            String idPaciente = ((ActivityPasosConsulta) getActivity()).getIdPacienteSeleccionado();
            if (idPaciente != null) {
                BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
                    EntidadPaciente paciente = BaseDatosCnp.obtenerBaseDatos(requireContext()).daoPaciente().obtenerPacientePorId(idPaciente);
                    if (paciente != null) {
                        // Si tienes la fecha de nacimiento en tu entidad, aquí la conviertes a edad.
                        // Por ahora simularemos con el sexo para la fórmula:
                        if (paciente.gender != null) {
                            sexoPaciente = paciente.gender;
                        }
                    }
                });
            }
        }
    }

    private void configurarMenuNaf() {
        String[] opcionesNaf = {
                "Sedentario (1.2)",
                "Ligero (1.375)",
                "Moderado (1.55)",
                "Activo (1.725)",
                "Muy Activo (1.9)"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, opcionesNaf);
        actvNaf.setAdapter(adapter);

        // Cuando eligen un NAF, recalcular todo
        actvNaf.setOnItemClickListener((parent, view, position, id) -> calcularMetricas());
    }

    private void configurarCalculoEnVivo() {
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { calcularMetricas(); }
        };

        etPeso.addTextChangedListener(watcher);
        etEstatura.addTextChangedListener(watcher);
    }

    private void calcularMetricas() {
        String pesoStr = etPeso.getText().toString().trim();
        String estStr = etEstatura.getText().toString().trim();

        if (pesoStr.isEmpty() || estStr.isEmpty()) return;

        try {
            double peso = Double.parseDouble(pesoStr);
            double estaturaCm = Double.parseDouble(estStr);
            double estaturaM = estaturaCm / 100.0;

            // 1. Cálculo de IMC
            double imc = peso / (estaturaM * estaturaM);
            tvValorImc.setText(String.format(Locale.getDefault(), "%.1f", imc));
            actualizarChipImc(imc);

            // 2. Cálculo de TMB (Mifflin-St Jeor)
            double tmb;
            if (sexoPaciente.equalsIgnoreCase("Femenino") || sexoPaciente.equalsIgnoreCase("Mujer")) {
                tmb = (10 * peso) + (6.25 * estaturaCm) - (5 * edadPaciente) - 161;
            } else {
                tmb = (10 * peso) + (6.25 * estaturaCm) - (5 * edadPaciente) + 5;
            }
            tvValorTmb.setText(String.format(Locale.getDefault(), "%,.0f", tmb));

            // 3. Cálculo de Gasto Energético (GET)
            String nafSeleccionado = actvNaf.getText().toString();
            double factorNaf = 1.2; // Sedentario por defecto
            if (nafSeleccionado.contains("1.375")) factorNaf = 1.375;
            else if (nafSeleccionado.contains("1.55")) factorNaf = 1.55;
            else if (nafSeleccionado.contains("1.725")) factorNaf = 1.725;
            else if (nafSeleccionado.contains("1.9")) factorNaf = 1.9;

            double get = tmb * factorNaf;
            tvValorGet.setText(String.format(Locale.getDefault(), "%,.0f", get));

        } catch (NumberFormatException e) {
            // Ignorar errores si el usuario tipea mal (ej. dos puntos "75..5")
        }
    }

    private void actualizarChipImc(double imc) {
        String estado;
        int colorFondo;
        int colorTexto;

        if (imc < 18.5) {
            estado = "Bajo Peso";
            colorFondo = Color.parseColor("#FFF3E0"); // Naranja claro
            colorTexto = Color.parseColor("#E65100"); // Naranja oscuro
        } else if (imc < 25.0) {
            estado = "Normal";
            colorFondo = Color.parseColor("#E8F5E9"); // Verde claro
            colorTexto = Color.parseColor("#2E7D32"); // Verde oscuro
        } else if (imc < 30.0) {
            estado = "Sobrepeso";
            colorFondo = Color.parseColor("#FFEBEE"); // Rojo claro
            colorTexto = Color.parseColor("#C62828"); // Rojo oscuro
        } else {
            estado = "Obesidad";
            colorFondo = Color.parseColor("#FFCDD2"); // Rojo más intenso
            colorTexto = Color.parseColor("#B71C1C"); // Rojo muy oscuro
        }

        chipEstadoImc.setText(estado);
        chipEstadoImc.setChipBackgroundColor(ColorStateList.valueOf(colorFondo));
        chipEstadoImc.setTextColor(colorTexto);
    }

    private void guardarYContinuar() {
        try {
            double imc = Double.parseDouble(tvValorImc.getText().toString().replace(",", "."));
            double tmb = Double.parseDouble(tvValorTmb.getText().toString().replace(",", ""));

            // Fórmula del Peso Ideal: 22.5 * (Estatura en m)^2
            double estM = Double.parseDouble(etEstatura.getText().toString()) / 100.0;
            double pesoIdeal = 22.5 * (estM * estM);

            if (getActivity() instanceof ActivityPasosConsulta) {
                ((ActivityPasosConsulta) getActivity()).setDatosCalculados(imc, tmb, pesoIdeal);
                ((ActivityPasosConsulta) getActivity()).cargarFragmento(
                        new FragmentoPaso3Diagnostico(),
                        getString(R.string.paso_indicador, 3)
                );
            }
        } catch (Exception e) {
            // Si hay error (campos vacíos), avanzamos pasando ceros, o puedes mostrar un Toast de advertencia
            if (getActivity() instanceof ActivityPasosConsulta) {
                ((ActivityPasosConsulta) getActivity()).cargarFragmento(
                        new FragmentoPaso3Diagnostico(),
                        getString(R.string.paso_indicador, 3)
                );
            }
        }
    }
}