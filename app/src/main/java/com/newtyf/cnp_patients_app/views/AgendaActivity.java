package com.newtyf.cnp_patients_app.views;

import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newtyf.cnp_patients_app.R;

public class AgendaActivity extends AppCompatActivity {

    private ImageView btnBack;
    private CalendarView calendarView;
    private TextView tvFechaSeleccionada;
    private FloatingActionButton fabAddAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agenda);

        // Vincular vistas
        btnBack = findViewById(R.id.btn_back_agenda);
        calendarView = findViewById(R.id.calendarView);
        tvFechaSeleccionada = findViewById(R.id.tv_fecha_seleccionada);
        fabAddAppointment = findViewById(R.id.fab_add_appointment);

        // Volver atrás
        btnBack.setOnClickListener(v -> finish());

        // Lógica del Calendario (Corregida usando sintaxis Lambda para evitar el error)
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Formateamos la fecha amigablemente (El mes empieza en 0, por eso sumamos 1)
            String fechaGesto = "Citas para el " + dayOfMonth + "/" + (month + 1) + "/" + year;
            tvFechaSeleccionada.setText(fechaGesto);

            // Aquí en el futuro filtraremos la lista de citas (RecyclerView) con la BD
        });

        // Lógica del FAB (Nueva Cita)
        fabAddAppointment.setOnClickListener(v -> {
            Toast.makeText(this, "Abriendo modal para agendar cita...", Toast.LENGTH_SHORT).show();
        });
    }
}