package com.newtyf.cnp_patients_app.presentacion.consultations;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.BaseDatosCnp;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadConsulta;
import com.newtyf.cnp_patients_app.domain.models.Consultation;

import java.util.List;

public class ActivityListaConsultas extends AppCompatActivity {

    private RecyclerView rvConsultas;
    private BaseDatosCnp dbHelper;
    private ConsultationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_consultas);

        // Instancia Singleton de Room
        dbHelper = BaseDatosCnp.obtenerBaseDatos(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar_consultation_list);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        FloatingActionButton fab = findViewById(R.id.fab_add_consulta);
        if (fab != null) {
            // 🔥 Apuntamos al nuevo Wizard de 3 pasos
            fab.setOnClickListener(v -> startActivity(new Intent(this, ActivityPasosConsulta.class)));
        }

        rvConsultas = findViewById(R.id.rv_consultas_completas);
        if (rvConsultas != null) {
            rvConsultas.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarConsultas();
    }

    private void cargarConsultas() {
        // Lectura en segundo plano
        BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
            List<Consultation> lista = dbHelper.daoConsulta().obtenerTodasLasConsultasConDetalle();

            // Volvemos al hilo principal para actualizar el RecyclerView
            runOnUiThread(() -> {
                if (adapter == null) {
                    adapter = new ConsultationAdapter(lista);
                    if (rvConsultas != null) {
                        rvConsultas.setAdapter(adapter);
                    }
                } else {
                    adapter.actualizarDatos(lista);
                }
            });
        });
    }

    // ==============================================
    // ADAPTADOR INTERNO
    // ==============================================
    private class ConsultationAdapter extends RecyclerView.Adapter<ConsultationAdapter.ViewHolder> {
        private List<Consultation> consultas;

        ConsultationAdapter(List<Consultation> consultas) {
            this.consultas = consultas;
        }

        // Método para actualizar la lista de forma eficiente
        public void actualizarDatos(List<Consultation> nuevasConsultas) {
            this.consultas = nuevasConsultas;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consulta, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Consultation c = consultas.get(position);

            holder.tvFecha.setText(c.getDate());
            holder.chipTipo.setText(c.getType());
            holder.tvPaciente.setText(c.getPatientName());
            holder.tvMotivo.setText("Motivo: " + c.getReason());

            holder.btnEditar.setOnClickListener(v -> {
                // 🔥 Ahora abrirá la consulta existente en nuestro nuevo Wizard de 3 pasos
                Intent intent = new Intent(ActivityListaConsultas.this, ActivityPasosConsulta.class);
                intent.putExtra("consultation_id", c.getId());
                startActivity(intent);
            });

            holder.btnEliminar.setOnClickListener(v -> {
                new MaterialAlertDialogBuilder(ActivityListaConsultas.this)
                        .setTitle("Eliminar Consulta")
                        .setMessage("¿Estás seguro de que deseas eliminar esta cita médica?")
                        .setNegativeButton("Cancelar", null)
                        .setPositiveButton("Eliminar", (dialog, which) -> {
                            // Eliminación en segundo plano usando Room
                            BaseDatosCnp.ejecutorEscrituraBD.execute(() -> {
                                EntidadConsulta consultaEliminar = dbHelper.daoConsulta().obtenerConsultaPorId(c.getId());
                                if (consultaEliminar != null) {
                                    dbHelper.daoConsulta().eliminarConsulta(consultaEliminar);

                                    // Actualizamos la UI
                                    runOnUiThread(() -> {
                                        Toast.makeText(ActivityListaConsultas.this, "Consulta eliminada", Toast.LENGTH_SHORT).show();
                                        cargarConsultas(); // Refrescar lista
                                    });
                                }
                            });
                        }).show();
            });
        }

        @Override
        public int getItemCount() {
            return consultas != null ? consultas.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvFecha, tvPaciente, tvMotivo;
            Chip chipTipo;
            MaterialButton btnEditar, btnEliminar;

            ViewHolder(View itemView) {
                super(itemView);
                tvFecha = itemView.findViewById(R.id.tv_item_fecha_cita);
                chipTipo = itemView.findViewById(R.id.chip_item_tipo_cita);
                tvPaciente = itemView.findViewById(R.id.tv_item_nombre_paciente);
                tvMotivo = itemView.findViewById(R.id.tv_item_motivo_cita);
                btnEditar = itemView.findViewById(R.id.btn_item_editar_cita);
                btnEliminar = itemView.findViewById(R.id.btn_item_eliminar_cita);
            }
        }
    }
}