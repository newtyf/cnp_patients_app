package com.newtyf.cnp_patients_app.presentacion.consultations;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.newtyf.cnp_patients_app.R;

public class ActivityPasosConsulta extends AppCompatActivity {

    private TextView tvIndicadorPaso;
    // Variables para almacenar los cálculos y pasarlos al Paso 3
    private double imcCalculado = 0.0;
    private double tmbCalculado = 0.0;
    private double pesoIdealCalculado = 0.0;

    public void setDatosCalculados(double imc, double tmb, double pesoIdeal) {
        this.imcCalculado = imc;
        this.tmbCalculado = tmb;
        this.pesoIdealCalculado = pesoIdeal;
    }

    public double getImcCalculado() { return imcCalculado; }
    public double getTmbCalculado() { return tmbCalculado; }
    public double getPesoIdealCalculado() { return pesoIdealCalculado; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pasos_consulta);

        setupToolbar();

        if (savedInstanceState == null) {
            // Utilizamos el string dinámico "PASO %1$d / 3" de nuestro strings.xml
            cargarFragmento(new FragmentoPaso1Antecedentes(), getString(R.string.paso_indicador, 1));
        }
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar_pasos_consulta);
        tvIndicadorPaso = findViewById(R.id.tv_indicador_paso);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    public void cargarFragmento(Fragment fragmento, String tituloPaso) {
        if (tvIndicadorPaso != null) {
            tvIndicadorPaso.setText(tituloPaso);
        }
        getSupportFragmentManager()
                .beginTransaction()
                // Animaciones estándar para un flujo más suave
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.contenedor_fragmentos_consulta, fragmento)
                .commit();
    }
    // Añade esta variable en la parte superior (debajo de tvIndicadorPaso)
    private String idPacienteSeleccionado = null;

    // ... (resto de tus métodos como onCreate, setupToolbar, cargarFragmento)

    // Añade este método al final de la clase:
    public void setPacienteSeleccionado(String idPaciente) {
        this.idPacienteSeleccionado = idPaciente;
    }

    public String getIdPacienteSeleccionado() {
        return idPacienteSeleccionado;
    }
}