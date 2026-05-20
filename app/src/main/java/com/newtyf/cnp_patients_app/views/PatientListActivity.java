package com.newtyf.cnp_patients_app.views;

import android.content.Intent;
import android.content.om.FabricatedOverlay;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.newtyf.cnp_patients_app.MainActivity;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.models.Patient;

import org.w3c.dom.Text;

import java.util.List;

public class PatientListActivity extends AppCompatActivity {

    TableLayout tblPacientes;
    FloatingActionButton fabCreatePatient;
    LinearLayout containerCards;

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

        fabCreatePatient = findViewById(R.id.fabCreatePatient);
        fabCreatePatient.setOnClickListener(this::GoToCreatePatient);

        containerCards = findViewById(R.id.containerCards);
    }

    @Override
    protected void onResume() {
        super.onResume();
        containerCards.removeAllViews();
        loadCards();
    }

    private void loadCards() {

        List<Patient> lista = MainActivity.pacientes;

        for (Patient p : lista) {
            View card = LayoutInflater.from(this).inflate(R.layout.item_patient, containerCards, false);

            TextView tvName = card.findViewById(R.id.tvPatientName);
            Chip chipDni = card.findViewById(R.id.chipDni);
            Chip chipAge = card.findViewById(R.id.chipAge);
            TextView tvLastConsult = card.findViewById(R.id.tvLastConsult);
            ImageButton btnMenuPatient = card.findViewById(R.id.btnMenuPatient);

            tvName.setText(p.getName());
            chipAge.setText(p.getAge() + " AÑOS");
            chipDni.setText("DNI: " + p.getDni());
            tvLastConsult.setText(p.getBirthDate());

            btnMenuPatient.setOnClickListener(v -> {
//                PopupMenu popup = new PopupMenu(this, v);
//                popup.inflate(R.menu.menu_patient_card);
//
//                popup.setOnMenuItemClickListener(item -> {
//                    if (item.getItemId() == R.id.actionDetail) {
//
//                    }
//                    return false;
//                });
//
//                popup.show();


                Intent intent = new Intent(this, PatientDetailActivity.class);
                intent.putExtra("dni", p.getDni());
                startActivity(intent);
            });

            containerCards.addView(card);

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
