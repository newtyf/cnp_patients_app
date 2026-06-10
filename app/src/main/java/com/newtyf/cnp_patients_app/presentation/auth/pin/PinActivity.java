package com.newtyf.cnp_patients_app.presentation.auth.pin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.repository.NutritionistRepository;
import com.newtyf.cnp_patients_app.presentation.main.MainActivity;

public class PinActivity extends AppCompatActivity {

    public static final String EXTRA_MODE           = "pin_mode";
    public static final String EXTRA_NUTRITIONIST_ID = "nutritionist_id";
    public static final int    MODE_SETUP  = 0;
    public static final int    MODE_LOGIN  = 1;

    private static final int PIN_LENGTH = 4;

    private View dot1, dot2, dot3, dot4;
    private Button btnConfirmarPin;
    private TextView tvPinHeader, tvPinSubtitle;

    private final StringBuilder pinIngresado = new StringBuilder();
    private NutritionistRepository repository;
    private String nutritionistId;
    private int mode;

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

        repository    = new NutritionistRepository(this);
        mode          = getIntent().getIntExtra(EXTRA_MODE, MODE_LOGIN);
        nutritionistId = getIntent().getStringExtra(EXTRA_NUTRITIONIST_ID);

        if (mode == MODE_LOGIN && nutritionistId == null) {
            nutritionistId = repository.getFirst() != null ? repository.getFirst().getId() : null;
        }

        initViews();
        adaptarTextos();
        setupTeclado();
        setupConfirmar();
    }

    private void initViews() {
        dot1           = findViewById(R.id.dot1);
        dot2           = findViewById(R.id.dot2);
        dot3           = findViewById(R.id.dot3);
        dot4           = findViewById(R.id.dot4);
        btnConfirmarPin = findViewById(R.id.btnConfirmarPin);
        tvPinHeader    = findViewById(R.id.tvPinHeader);
        tvPinSubtitle  = findViewById(R.id.tvPinSubtitle);
    }

    private void adaptarTextos() {
        if (mode == MODE_LOGIN) {
            tvPinHeader.setText(R.string.pin_login_header);
            tvPinSubtitle.setText(R.string.pin_login_subtitle);
            btnConfirmarPin.setText(R.string.pin_login_button);
        }
        // MODE_SETUP ya tiene los textos correctos en el layout
    }

    private void setupConfirmar() {
        btnConfirmarPin.setOnClickListener(v -> {
            if (mode == MODE_SETUP) {
                repository.updatePin(nutritionistId, pinIngresado.toString());
                Toast.makeText(this, R.string.pin_setup_success, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                boolean correcto = repository.checkPin(nutritionistId, pinIngresado.toString());
                if (correcto) {
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.pin_error_incorrecto, Toast.LENGTH_SHORT).show();
                    pinIngresado.setLength(0);
                    actualizarIndicadores();
                }
            }
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
