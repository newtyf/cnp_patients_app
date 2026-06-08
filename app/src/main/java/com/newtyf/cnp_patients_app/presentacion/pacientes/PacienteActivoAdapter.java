package com.newtyf.cnp_patients_app.presentacion.pacientes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadPaciente;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PacienteActivoAdapter extends RecyclerView.Adapter<PacienteActivoAdapter.ViewHolder> implements Filterable {

    private List<EntidadPaciente> listaOriginal;
    private List<EntidadPaciente> listaFiltrada;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(EntidadPaciente paciente);
    }

    public PacienteActivoAdapter(Context context, List<EntidadPaciente> lista, OnItemClickListener listener) {
        this.context = context;
        this.listaOriginal = lista;
        this.listaFiltrada = new ArrayList<>(lista);
        this.listener = listener;
    }

    public PacienteActivoAdapter(Context context, List<EntidadPaciente> lista) {
        this.context = context;
        this.listaOriginal = lista;
        this.listaFiltrada = new ArrayList<>(lista);
        this.listener = null;
    }

    public void actualizarDatos(List<EntidadPaciente> nuevaLista) {
        this.listaOriginal = nuevaLista;
        this.listaFiltrada = new ArrayList<>(nuevaLista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paciente_reciente, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EntidadPaciente paciente = listaFiltrada.get(position);
        holder.tvNombre.setText(paciente.getNombreCompleto());
        String dniTexto = paciente.idNumber != null ? "DNI: " + paciente.idNumber : "DNI: N/A";
        holder.tvDni.setText(dniTexto);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(paciente);
        });

// ... dentro de onBindViewHolder, reemplaza el bloque del botón por este:

        holder.btnNuevaConsulta.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(paciente);
            } else {
                // 🔥 CAMBIO: Ahora abre el nuevo Wizard de 3 pasos
                Intent intent = new Intent(context, com.newtyf.cnp_patients_app.presentacion.consultations.ActivityPasosConsulta.class);
                intent.putExtra("PRESELECTED_PATIENT_ID", paciente.id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return listaFiltrada.size(); }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String busqueda = constraint.toString().toLowerCase().trim();
                List<EntidadPaciente> resultadosTemp = new ArrayList<>();
                if (busqueda.isEmpty()) { resultadosTemp.addAll(listaOriginal); }
                else {
                    for (EntidadPaciente p : listaOriginal) {
                        String nombre = p.getNombreCompleto().toLowerCase();
                        String dni = p.idNumber != null ? p.idNumber.toLowerCase() : "";
                        if (nombre.contains(busqueda) || dni.contains(busqueda)) { resultadosTemp.add(p); }
                    }
                }
                Collections.sort(resultadosTemp, (p1, p2) -> p1.getNombreCompleto().compareToIgnoreCase(p2.getNombreCompleto()));
                FilterResults filterResults = new FilterResults();
                filterResults.values = resultadosTemp;
                return filterResults;
            }
            @Override
            @SuppressWarnings("unchecked")
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listaFiltrada = (List<EntidadPaciente>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDni;
        MaterialButton btnNuevaConsulta;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre_paciente);
            tvDni = itemView.findViewById(R.id.tv_dni_paciente);
            btnNuevaConsulta = itemView.findViewById(R.id.btn_nueva_consulta_card);
        }
    }
}