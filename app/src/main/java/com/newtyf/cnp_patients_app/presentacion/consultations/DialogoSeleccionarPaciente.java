package com.newtyf.cnp_patients_app.presentacion.consultations;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.BaseDatosCnp;
import com.newtyf.cnp_patients_app.data.local.dao.DaoPaciente;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadPaciente;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DialogoSeleccionarPaciente extends BottomSheetDialogFragment {

    private RecyclerView rvBusqueda;
    private TextInputEditText etBuscador;
    private Button btnEjecutarBusqueda;

    // 🚀 1. AHORA USAMOS EL ADAPTADOR SIMPLE, NO EL REUTILIZADO
    private AdaptadorBuscadorPaciente adaptador;

    public interface OnPacienteSeleccionadoListener {
        void onPacienteSeleccionado(EntidadPaciente paciente);
    }

    private OnPacienteSeleccionadoListener listener;

    public void setListener(OnPacienteSeleccionadoListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_seleccionar_paciente, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarVistas(view);
        configurarRecyclerView();
        configurarBuscador();
        cargarDatosDeBaseDeDatos();
    }

    private void inicializarVistas(View view) {
        rvBusqueda = view.findViewById(R.id.rv_busqueda_pacientes);
        etBuscador = view.findViewById(R.id.et_buscar_paciente);
        btnEjecutarBusqueda = view.findViewById(R.id.btn_ejecutar_busqueda);
    }

    private void configurarRecyclerView() {
        // 🚀 2. INICIALIZAMOS EL ADAPTADOR SIMPLE (Solo pide Lista y Listener)
        adaptador = new AdaptadorBuscadorPaciente(new ArrayList<>(), paciente -> {
            if (listener != null) {
                listener.onPacienteSeleccionado(paciente);
            }
        });

        rvBusqueda.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBusqueda.setAdapter(adaptador);
    }

    private void configurarBuscador() {
        btnEjecutarBusqueda.setOnClickListener(v -> ejecutarFiltro());
        etBuscador.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                ejecutarFiltro();
                return true;
            }
            return false;
        });
    }

    private void ejecutarFiltro() {
        if (adaptador != null && etBuscador.getText() != null) {
            String busqueda = etBuscador.getText().toString();
            adaptador.getFilter().filter(busqueda);
            ocultarTeclado();
        }
    }

    private void ocultarTeclado() {
        View view = this.getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void cargarDatosDeBaseDeDatos() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            BaseDatosCnp db = BaseDatosCnp.obtenerBaseDatos(requireContext());
            DaoPaciente daoPaciente = db.daoPaciente();
            List<EntidadPaciente> listaPacientesDB = daoPaciente.obtenerTodos();

            requireActivity().runOnUiThread(() -> {
                if (listaPacientesDB != null && adaptador != null) {
                    adaptador.actualizarDatos(listaPacientesDB);
                }
            });
        });
    }
}