package com.newtyf.cnp_patients_app.presentacion.diet;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.newtyf.cnp_patients_app.R;

public class ActivityCrearReceta extends AppCompatActivity {

    private ImageView btnBack;
    private AutoCompleteTextView dropdownCategoria;
    private MaterialButton btnAddIngrediente, btnGuardarReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_receta);

        // 1. Vincular Vistas
        btnBack = findViewById(R.id.btn_back_recipe);
        dropdownCategoria = findViewById(R.id.dropdown_categoria_receta);
        btnAddIngrediente = findViewById(R.id.btn_add_ingrediente);
        btnGuardarReceta = findViewById(R.id.btn_guardar_receta);

        // 2. Configurar el Dropdown de Categorías
        String[] categorias = new String[]{"Desayuno", "Almuerzo", "Snack", "Cena", "Bebida/Batido"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                categorias
        );
        dropdownCategoria.setAdapter(adapter);

        // 3. Acciones
        btnBack.setOnClickListener(v -> finish());

        // Este botón debería abrir el Modal del Buscador de Alimentos que creamos antes
        btnAddIngrediente.setOnClickListener(v -> {
            Toast.makeText(this, "Abriendo buscador de alimentos...", Toast.LENGTH_SHORT).show();
            // Aquí llamarías a un BottomSheetDialog similar al que hicimos en DietGeneratorActivity
        });

        // Guardar Receta
        btnGuardarReceta.setOnClickListener(v -> mostrarModalExito());
    }

    private void mostrarModalExito() {
        Dialog dialog = new Dialog(this);
        // Reutilizamos nuestro genial modal verde
        dialog.setContentView(R.layout.dialogo_exito_paciente);

        TextView tvModalText = dialog.findViewById(R.id.tv_modal_success_title);
        if(tvModalText != null) {
            tvModalText.setText("Plato Guardado");
        }

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            dialog.dismiss();
            finish();
        }, 1500);
    }
}