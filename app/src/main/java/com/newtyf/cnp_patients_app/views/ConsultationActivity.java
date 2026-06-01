package com.newtyf.cnp_patients_app.views;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.newtyf.cnp_patients_app.R;

public class ConsultationActivity extends AppCompatActivity {

    // Contenedores y UI General
    private LinearLayout step1Container, step2Container, step3Container;
    private TextView tvStepIndicator;
    private LinearProgressIndicator progressConsulta;
    private ImageView btnClose;

    // Botones de Navegación
    private MaterialButton btnSiguiente1, btnAtras2, btnSiguiente2, btnAtras3, btnFinalizar;

    // --- VARIABLES PASO 2 (Medidas) ---
    private TextInputEditText etPeso, etEstatura;
    private TextView tvImcResult, tvImcLabel, tvTmbResult, tvGetResult;
    private AutoCompleteTextView dropdownNaf;

    // --- VARIABLES PASO 3 (Diagnóstico) ---
    private TextView tvResumenImc, tvResumenImcLabel, tvResumenPesoIdeal, tvResumenTmb;

    // Variables globales para guardar los cálculos y pasarlos al Paso 3
    private double imcCalculado = 0.0;
    private double tmbCalculado = 0.0;
    private double pesoIdealCalculado = 0.0;
    private String imcEtiqueta = "Pendiente";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_consultation);

        vincularVistas();
        configurarNavegacion();
        configurarCalculadoraInteractiva();
    }

    private void vincularVistas() {
        // UI Global
        step1Container = findViewById(R.id.step1_container);
        step2Container = findViewById(R.id.step2_container);
        step3Container = findViewById(R.id.step3_container);
        tvStepIndicator = findViewById(R.id.tv_step_indicator);
        progressConsulta = findViewById(R.id.progress_consulta);
        btnClose = findViewById(R.id.btn_close_consultation);

        // Botones
        btnSiguiente1 = findViewById(R.id.btn_siguiente_1);
        btnAtras2 = findViewById(R.id.btn_atras_2);
        btnSiguiente2 = findViewById(R.id.btn_siguiente_2);
        btnAtras3 = findViewById(R.id.btn_atras_3);
        btnFinalizar = findViewById(R.id.btn_finalizar_consulta);

        // Inputs Paso 2
        etPeso = findViewById(R.id.et_peso);
        etEstatura = findViewById(R.id.et_estatura);
        tvImcResult = findViewById(R.id.tv_imc_result);
        tvImcLabel = findViewById(R.id.tv_imc_label);
        tvTmbResult = findViewById(R.id.tv_tmb_result);
        tvGetResult = findViewById(R.id.tv_get_result);

        // Configurar Dropdown del NAF (Nivel de Actividad Física)
        dropdownNaf = findViewById(R.id.dropdown_naf);
        String[] opcionesNaf = new String[]{
                "Sedentario (Poco o ningún ejercicio)",
                "Ligero (1-3 días/sem)",
                "Moderado (3-5 días/sem)",
                "Activo (6-7 días/sem)"
        };
        ArrayAdapter<String> adapterNaf = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opcionesNaf);
        dropdownNaf.setAdapter(adapterNaf);

        // Outputs Paso 3
        tvResumenImc = findViewById(R.id.tv_resumen_imc);
        tvResumenImcLabel = findViewById(R.id.tv_resumen_imc_label);
        tvResumenPesoIdeal = findViewById(R.id.tv_resumen_peso_ideal);
        tvResumenTmb = findViewById(R.id.tv_resumen_tmb);
    }

    private void configurarNavegacion() {
        btnClose.setOnClickListener(v -> finish());

        // Transiciones del Wizard
        btnSiguiente1.setOnClickListener(v -> switchStep(2));
        btnAtras2.setOnClickListener(v -> switchStep(1));

        btnSiguiente2.setOnClickListener(v -> {
            if (imcCalculado == 0.0) {
                Toast.makeText(this, "Por favor ingrese Peso y Estatura para continuar", Toast.LENGTH_SHORT).show();
                return;
            }
            switchStep(3);
        });

        btnAtras3.setOnClickListener(v -> switchStep(2));

        btnFinalizar.setOnClickListener(v -> mostrarModalExito());
    }

    private void configurarCalculadoraInteractiva() {
        // Un "Escuchador" que se activa cada vez que se teclea un número
        TextWatcher calculadorEnTiempoReal = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                realizarCalculosMedicos();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        etPeso.addTextChangedListener(calculadorEnTiempoReal);
        etEstatura.addTextChangedListener(calculadorEnTiempoReal);
    }

    private void realizarCalculosMedicos() {
        String pesoStr = etPeso.getText().toString();
        String estaturaStr = etEstatura.getText().toString();

        if (!pesoStr.isEmpty() && !estaturaStr.isEmpty()) {
            try {
                double pesoKg = Double.parseDouble(pesoStr);
                double estaturaCm = Double.parseDouble(estaturaStr);
                double estaturaMts = estaturaCm / 100.0;

                if (estaturaMts > 0) {
                    // 1. CÁLCULO DE IMC
                    imcCalculado = pesoKg / (estaturaMts * estaturaMts);

                    // Asignar etiqueta clínica y colores
                    if (imcCalculado < 18.5) {
                        imcEtiqueta = "Bajo Peso";
                        tvImcLabel.setTextColor(Color.parseColor("#1565C0")); // Azul
                        tvImcLabel.setBackgroundColor(Color.parseColor("#BBDEFB"));
                    } else if (imcCalculado < 25) {
                        imcEtiqueta = "Normal";
                        tvImcLabel.setTextColor(Color.parseColor("#2E7D32")); // Verde
                        tvImcLabel.setBackgroundColor(Color.parseColor("#C8E6C9"));
                    } else if (imcCalculado < 30) {
                        imcEtiqueta = "Sobrepeso";
                        tvImcLabel.setTextColor(Color.parseColor("#E65100")); // Naranja
                        tvImcLabel.setBackgroundColor(Color.parseColor("#FFE0B2"));
                    } else {
                        imcEtiqueta = "Obesidad";
                        tvImcLabel.setTextColor(Color.parseColor("#C62828")); // Rojo
                        tvImcLabel.setBackgroundColor(Color.parseColor("#FFCDD2"));
                    }

                    // 2. CÁLCULO DE TMB (Mifflin-St Jeor para Hombre - Asumido del Muckup 34 años)
                    // Fórmula: (10 × peso) + (6.25 × altura cm) - (5 × edad) + 5
                    tmbCalculado = (10 * pesoKg) + (6.25 * estaturaCm) - (5 * 34) + 5;

                    // 3. CÁLCULO DE PESO IDEAL (Basado en IMC saludable promedio de 22.5)
                    pesoIdealCalculado = 22.5 * (estaturaMts * estaturaMts);

                    // --- ACTUALIZAR LA PANTALLA EN TIEMPO REAL (Paso 2) ---
                    tvImcResult.setText(String.format("%.1f kg/m²", imcCalculado));
                    tvImcLabel.setText(imcEtiqueta);
                    tvTmbResult.setText(String.format("%,d kcal", (int) tmbCalculado));
                }
            } catch (NumberFormatException e) {
                // Si borran un número y queda en blanco
                limpiarResultados();
            }
        } else {
            limpiarResultados();
        }
    }

    private void limpiarResultados() {
        imcCalculado = 0.0;
        tvImcResult.setText("0.0 kg/m²");
        tvImcLabel.setText("Esperando datos...");
        tvImcLabel.setTextColor(Color.parseColor("#757575"));
        tvImcLabel.setBackgroundColor(Color.parseColor("#E0E0E0"));
        tvTmbResult.setText("0 kcal");
    }

    private void switchStep(int step) {
        step1Container.setVisibility(View.GONE);
        step2Container.setVisibility(View.GONE);
        step3Container.setVisibility(View.GONE);

        switch (step) {
            case 1:
                step1Container.setVisibility(View.VISIBLE);
                tvStepIndicator.setText("PASO 1");
                progressConsulta.setProgress(33);
                break;
            case 2:
                step2Container.setVisibility(View.VISIBLE);
                tvStepIndicator.setText("PASO 2");
                progressConsulta.setProgress(66);
                break;
            case 3:
                step3Container.setVisibility(View.VISIBLE);
                tvStepIndicator.setText("PASO 3");
                progressConsulta.setProgress(100);

                // ¡MAGIA DE UX! Al entrar al Paso 3, inyectamos los datos calculados en el Paso 2
                tvResumenImc.setText(String.format("%.1f", imcCalculado));
                tvResumenImcLabel.setText(imcEtiqueta);
                tvResumenPesoIdeal.setText(String.format("%.1f kg", pesoIdealCalculado));
                tvResumenTmb.setText(String.format("%,d kcal / día", (int) tmbCalculado));
                break;
        }
    }

    private void mostrarModalExito() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_success_patient);

        // Cambiar el texto del modal genérico para la consulta
        TextView tvModalText = dialog.findViewById(R.id.tv_modal_success_title); // Si le pusiste ID, si no omitir
        if(tvModalText != null) {
            tvModalText.setText("Consulta Finalizada");
        }

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            dialog.dismiss();

            Intent intent = new Intent(ConsultationActivity.this, DietGeneratorActivity.class);
            intent.putExtra("PACIENTE_NOMBRE", "Carlos Mendoza"); // Pasamos el nombre
            startActivity(intent);
            finish();
        }, 2000);
    }
}