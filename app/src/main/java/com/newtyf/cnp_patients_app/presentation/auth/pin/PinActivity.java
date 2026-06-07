package com.newtyf.cnp_patients_app.presentation.auth.pin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.presentation.main.MainActivity;

public class PinActivity extends AppCompatActivity {

    private static final int PIN_LENGTH = 4;

    private View dot1, dot2, dot3, dot4;
    private Button btnConfirmarPin;
    private MaterialSwitch switchBiometria;

    private final StringBuilder pinIngresado = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupTeclado();
        setupConfirmar();
    }

    private void initViews() {
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);
        dot4 = findViewById(R.id.dot4);
        btnConfirmarPin = findViewById(R.id.btnConfirmarPin);
        switchBiometria = findViewById(R.id.switchBiometria);
    }

    private void setupConfirmar() {
        btnConfirmarPin.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    private void setupTeclado() {
        int[] btnIds = {
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
            R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
            R.id.btn8, R.id.btn9
        };

        for (int id : btnIds) {
            Button btn = findViewById(id);
            btn.setOnClickListener(v -> {
                if (pinIngresado.length() < PIN_LENGTH) {
                    pinIngresado.append(((Button) v).getText());
                    actualizarIndicadores();
                }
            });
        }

        ImageButton btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            if (pinIngresado.length() > 0) {
                pinIngresado.deleteCharAt(pinIngresado.length() - 1);
                actualizarIndicadores();
            }
        });
    }

    private void actualizarIndicadores() {
        int len = pinIngresado.length();
        View[] dots = {dot1, dot2, dot3, dot4};
        for (int i = 0; i < dots.length; i++) {
            dots[i].setBackgroundResource(i < len
                    ? R.drawable.pin_dot_filled
                    : R.drawable.pin_dot_empty);
        }
        btnConfirmarPin.setEnabled(len == PIN_LENGTH);
    }
}
