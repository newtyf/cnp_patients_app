package com.newtyf.cnp_patients_app.presentacion.consultations;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadPaciente;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdaptadorBuscadorPaciente extends RecyclerView.Adapter<AdaptadorBuscadorPaciente.ViewHolder> implements Filterable {

    private List<EntidadPaciente> listaOriginal;
    private List<EntidadPaciente> listaFiltrada;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(EntidadPaciente paciente);
    }

    public AdaptadorBuscadorPaciente(List<EntidadPaciente> lista, OnItemClickListener listener) {
        this.listaOriginal = lista;
        this.listaFiltrada = new ArrayList<>(lista);
        this.listener = listener;
    }

    public void actualizarDatos(List<EntidadPaciente> nuevaLista) {
        this.listaOriginal = nuevaLista;
        this.listaFiltrada = new ArrayList<>(nuevaLista);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_simple, parent, false);
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
                if (busqueda.isEmpty()) {
                    resultadosTemp.addAll(listaOriginal);
                } else {
                    for (EntidadPaciente p : listaOriginal) {
                        String nombreCompleto = p.getNombreCompleto().toLowerCase();
                        String dni = p.idNumber != null ? p.idNumber.toLowerCase() : "";
                        if (nombreCompleto.contains(busqueda) || dni.contains(busqueda)) {
                            resultadosTemp.add(p);
                        }
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre_simple);
            tvDni = itemView.findViewById(R.id.tv_dni_simple);
        }
    }
}