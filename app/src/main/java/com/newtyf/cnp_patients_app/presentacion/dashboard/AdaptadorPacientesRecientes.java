package com.newtyf.cnp_patients_app.presentacion.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadPaciente;
import java.util.List;

public class AdaptadorPacientesRecientes extends RecyclerView.Adapter<AdaptadorPacientesRecientes.ViewHolder> {

    private List<EntidadPaciente> listaPacientes;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(EntidadPaciente paciente);
    }

    public AdaptadorPacientesRecientes(List<EntidadPaciente> listaPacientes, OnItemClickListener listener) {
        this.listaPacientes = listaPacientes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el diseño compacto del Dashboard
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paciente_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EntidadPaciente paciente = listaPacientes.get(position);

        holder.tvNombre.setText(paciente.getNombreCompleto());

        // Aquí puedes poner lógica futura para saber la última consulta real
        holder.tvSubtitulo.setText("Última vez: Reciente");

        // Aquí puedes poner lógica futura para la etiqueta de la dieta
        holder.chipTag.setText("Regular");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(paciente);
        });
    }

    @Override
    public int getItemCount() {
        return listaPacientes != null ? listaPacientes.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvSubtitulo;
        Chip chipTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre_dash);
            tvSubtitulo = itemView.findViewById(R.id.tv_subtitulo_dash);
            chipTag = itemView.findViewById(R.id.chip_tag_dash);
        }
    }
}