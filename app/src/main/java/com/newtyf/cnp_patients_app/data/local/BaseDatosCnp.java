package com.newtyf.cnp_patients_app.data.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase; // <--- Nueva importación obligatoria

import com.newtyf.cnp_patients_app.data.local.dao.DaoConsulta;
import com.newtyf.cnp_patients_app.data.local.dao.DaoPaciente;
import com.newtyf.cnp_patients_app.data.local.dao.DaoUsuario;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadConsulta;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadPaciente;
import com.newtyf.cnp_patients_app.data.local.entity.EntidadUsuario;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
        entities = {
                EntidadPaciente.class,
                EntidadUsuario.class,
                EntidadConsulta.class
        },
        version = 1,
        exportSchema = false
)
public abstract class BaseDatosCnp extends RoomDatabase {

    public abstract DaoPaciente daoPaciente();
    public abstract DaoUsuario daoUsuario();
    public abstract DaoConsulta daoConsulta();

    private static volatile BaseDatosCnp INSTANCIA;

    private static final int NUMERO_DE_HILOS = 4;
    public static final ExecutorService ejecutorEscrituraBD =
            Executors.newFixedThreadPool(NUMERO_DE_HILOS);

    public static BaseDatosCnp obtenerBaseDatos(final Context context) {
        if (INSTANCIA == null) {
            synchronized (BaseDatosCnp.class) {
                if (INSTANCIA == null) {
                    INSTANCIA = Room.databaseBuilder(context.getApplicationContext(),
                                    BaseDatosCnp.class, "CNPGestion_Room.db")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallbackPrueba) // 🚀 AQUÍ CONECTAMOS EL INYECTOR
                            .build();
                }
            }
        }
        return INSTANCIA;
    }

    // =========================================================================
    // 🚀 CALLBACK DE ROOM: Se ejecuta SOLO la primera vez que se crea la BD
    // =========================================================================
    private static final RoomDatabase.Callback roomCallbackPrueba = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // Usamos SQL puro aquí para evitar bloqueos mientras Room se está construyendo

            // 1. Inyectamos un Nutricionista (Usuario de prueba)
            db.execSQL("INSERT INTO users (name, email, password) " +
                    "VALUES ('Nutricionista Admin', 'admin@cnp.pe', '123456')");

            // 2. Opcional: Inyectamos un Paciente de prueba para que tu Dashboard no esté vacío
            db.execSQL("INSERT INTO patient (id, nutritionist_id, first_name, last_name, id_number, active) " +
                    "VALUES ('mock-paciente-1', 'admin@cnp.pe', 'Ana', 'Torres', '12345678', 1)");
        }
    };
}