package com.newtyf.cnp_patients_app.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.newtyf.cnp_patients_app.MainActivity;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.models.Patient;

import java.util.List;

public class PatientListActivity extends AppCompatActivity {

    TableLayout tblPacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patient_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tblPacientes = findViewById(R.id.tblPacientes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTable();
    }

    private void loadTable() {
        // Limpiar filas previas (mantener header en index 0)
        int rowCount = tblPacientes.getChildCount();
        if (rowCount > 1) {
            tblPacientes.removeViews(1, rowCount - 1);
        }

        List<Patient> lista = MainActivity.pacientes;
        for (int i = 0; i < lista.size(); i++) {
            final int index = i;
            Patient p = lista.get(i);

            TableRow row = new TableRow(this);

            TextView tvNombre = new TextView(this);
            tvNombre.setText(p.getName());
            tvNombre.setPadding(8, 8, 8, 8);
            tvNombre.setClickable(true);
            tvNombre.setFocusable(true);
            tvNombre.setOnClickListener(v -> {
                Intent intent = new Intent(this, PatientDetailActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);
            });

            Button btnEditar = new Button(this);
            btnEditar.setText("Editar");
            btnEditar.setOnClickListener(v -> {
                Intent intent = new Intent(this, PatientCreateActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);
            });

            Button btnEliminar = new Button(this);
            btnEliminar.setText("Eliminar");
            btnEliminar.setOnClickListener(v -> {
                MainActivity.pacientes.remove(index);
                loadTable();
                Toast.makeText(this, "Paciente eliminado", Toast.LENGTH_SHORT).show();
            });

            row.addView(tvNombre);
            row.addView(btnEditar);
            row.addView(btnEliminar);
            tblPacientes.addView(row);
        }
    }

    public void GoToCreatePatient(View view) {
        Intent intent = new Intent(this, PatientCreateActivity.class);
        startActivity(intent);
    }

    public void Logout(View view) {
        finish();
    }
}
