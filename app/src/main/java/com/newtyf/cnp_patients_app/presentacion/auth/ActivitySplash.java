package com.newtyf.cnp_patients_app.presentacion.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.newtyf.cnp_patients_app.R;

public class ActivitySplash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 1. Referenciar las vistas del XML usando la sintaxis de Java
        ImageView imgCnp = findViewById(R.id.img_cnp);
        ImageView imgHoja = findViewById(R.id.img_hoja);
        ImageView imgTenedor = findViewById(R.id.img_tenedor);
        ImageView imgCuchara = findViewById(R.id.img_cuchara);
        ImageView imgSubtitulo = findViewById(R.id.img_subtitulo);

        // 2. Orquesta de Animaciones (Línea de tiempo fluida)

        // Entrada del Texto Central (CNP)
        imgCnp.animate()
                .alpha(1f)
                .setDuration(500)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        // Entrada del Tenedor (Izquierda hacia el centro)
        imgTenedor.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(600)
                .setStartDelay(150) // Entra un instante después
                .setInterpolator(new DecelerateInterpolator())
                .start();

        // Entrada de la Cuchara (Derecha hacia el centro)
        imgCuchara.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(600)
                .setStartDelay(150)
                .setInterpolator(new DecelerateInterpolator())
                .start();

        // Entrada de la Hoja con efecto de rebote orgánico (Overshoot)
        imgHoja.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(700)
                .setStartDelay(350) // Aparece cuando el logo central ya está armado
                .setInterpolator(new OvershootInterpolator(1.5f)) // Efecto elástico premium
                .start();

        // Entrada sutil del subtítulo
        imgSubtitulo.animate()
                .alpha(1f)
                .setDuration(500)
                .setStartDelay(600)
                .start();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

           Intent intent = new Intent(ActivitySplash.this, ActivityBienvenida.class);
          startActivity(intent);
            finish();

        }, 2000);
    }
}