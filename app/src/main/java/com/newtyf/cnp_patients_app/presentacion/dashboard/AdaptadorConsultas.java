package com.newtyf.cnp_patients_app.presentacion.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.domain.models.ConsultaDTO;
import java.util.List;

public class AdaptadorConsultas extends RecyclerView.Adapter<AdaptadorConsultas.ViewHolder> {

    private List<ConsultaDTO> citasList;

    public AdaptadorConsultas(List<ConsultaDTO> citasList) {
        this.citasList = citasList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cita, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ConsultaDTO cita = citasList.get(position);
        holder.tvNombre.setText(cita.getPacienteNombre());
        holder.tvDetalle.setText(cita.getDetalle());
    }

    @Override
    public int getItemCount() {
        return citasList != null ? citasList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDetalle;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_cita_nombre);
            tvDetalle = itemView.findViewById(R.id.tv_cita_detalle);
        }
    }
}