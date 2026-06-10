package com.newtyf.cnp_patients_app.presentation.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.newtyf.cnp_patients_app.R;
import com.newtyf.cnp_patients_app.data.repository.NutritionistRepository;
import com.newtyf.cnp_patients_app.presentation.auth.login.LoginActivity;
import com.newtyf.cnp_patients_app.presentation.auth.pin.PinActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        NutritionistRepository repository = new NutritionistRepository(this);

        findViewById(R.id.main).postDelayed(() -> {
            Intent intent;
            if (repository.hasRegistered()) {
                intent = new Intent(this, PinActivity.class);
                intent.putExtra(PinActivity.EXTRA_MODE, PinActivity.MODE_LOGIN);
            } else {
                intent = new Intent(this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, 2000);
    }
}
