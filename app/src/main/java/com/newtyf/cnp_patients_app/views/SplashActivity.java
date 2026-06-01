package com.newtyf.cnp_patients_app.views; // Verifica que sea tu paquete correcto

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.newtyf.cnp_patients_app.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 1. Vincular las vistas
        ImageView imgCnp = findViewById(R.id.img_cnp);
        ImageView imgHoja = findViewById(R.id.img_hoja);
        ImageView imgTenedor = findViewById(R.id.img_tenedor);
        ImageView imgCuchara = findViewById(R.id.img_cuchara);
        ImageView imgSubtitulo = findViewById(R.id.img_subtitulo);

        // 2. Estado inicial (esconder elementos fuera de la pantalla)
        // Desplazamos el tenedor a la izquierda y la cuchara a la derecha
        imgTenedor.setTranslationX(-300f);
        imgCuchara.setTranslationX(300f);

        // 3. Iniciar la Coreografía (Animaciones)

        // Los cubiertos entran deslizándose (800ms)
        imgTenedor.animate().translationX(0f).setDuration(800).setInterpolator(new DecelerateInterpolator()).start();
        imgCuchara.animate().translationX(0f).setDuration(800).setInterpolator(new DecelerateInterpolator()).start();

        // Las siglas CNP aparecen con un fade-in (comienza con un ligero retraso)
        imgCnp.animate().alpha(1f).setDuration(800).setStartDelay(400).start();

        // La hoja "brota" con un rebote orgánico (Overshoot)
        imgHoja.animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(600)
                .setStartDelay(900)
                .setInterpolator(new OvershootInterpolator()) // Esto le da el efecto de rebote
                .start();

        // El subtítulo aparece al final sutilmente
        imgSubtitulo.animate().alpha(1f).translationY(-20f).setDuration(800).setStartDelay(1300).start();

        // 4. Transición a la siguiente pantalla (LoginActivity)
        // Total de la animación es aprox 2.1 segundos. Damos 2.5 seg (2500ms) para que el usuario la aprecie y luego cambiamos de pantalla.
// ... (código de tus animaciones imgTenedor, imgCnp, etc.) ...

        // Salida limpia hacia la pantalla de bienvenida
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2600); // 2600 milisegundos = 2.6 segundos
    } // <- Asegúrate de que esto esté antes de que cierre el método onCreate 2500);
    }
