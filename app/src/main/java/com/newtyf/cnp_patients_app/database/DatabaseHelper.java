package com.newtyf.cnp_patients_app.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

// Autor: pregunta1
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CNPGestion.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creación de tabla patient según especificaciones clínicas
        String createPatientTable = "CREATE TABLE patient (" +
                "id TEXT PRIMARY KEY, " +
                "nutritionist_id TEXT, " +
                "first_name TEXT NOT NULL, " +
                "last_name TEXT, " +
                "id_number TEXT, " +
                "birth_date DATE NOT NULL, " +
                "gender TEXT, " +
                "phone TEXT, " +
                "email TEXT, " +
                "photo_path TEXT, " +
                "notes TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createPatientTable);

        // Creación de tabla de historial de visitas
        String createConsultationTable = "CREATE TABLE consultation (" +
                "id TEXT PRIMARY KEY, " +
                "patient_id TEXT NOT NULL, " +
                "date DATE NOT NULL, " +
                "type TEXT, " +
                "reason TEXT, " +
                "notes TEXT, " +
                "created_at DATETIME DEFAULT CURRENT_TIMESTAMP)";
        db.execSQL(createConsultationTable);

        // Creación de tabla de registros clínicos antropométricos
        String createAnthropometricTable = "CREATE TABLE anthropometric_record (" +
                "id TEXT PRIMARY KEY, " +
                "consultation_id TEXT NOT NULL, " +
                "weight_kg REAL, " +
                "height_cm REAL, " +
                "body_fat_pct REAL, " +
                "muscle_mass_pct REAL, " +
                "bmi REAL, " +
                "bmr_kcal REAL, " +
                "tdee_kcal REAL, " +
                "activity_factor REAL, " +
                "bmr_formula TEXT)";
        db.execSQL(createAnthropometricTable);

        insertarDatosDePrueba(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS anthropometric_record");
        db.execSQL("DROP TABLE IF EXISTS consultation");
        db.execSQL("DROP TABLE IF EXISTS patient");
        onCreate(db);
    }

    // Cuenta el total de pacientes registrados [cite: 385]
    public int getCantidadPacientesActivos() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM patient", null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // Filtra las consultas realizadas en el mes actual [cite: 385]
    public int getCantidadConsultasDelMes() {
        SQLiteDatabase db = this.getReadableDatabase();
        int count = 0;
        String mesActual = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM consultation WHERE date LIKE ?", new String[]{mesActual + "%"});
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    // Extrae de forma combinada los últimos progresos clínicos para el indicador de impacto
    public List<String[]> getProgresoPacientes() {
        List<String[]> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT p.first_name || ' ' || IFNULL(p.last_name, ''), a.weight_kg, a.bmi " +
                "FROM patient p " +
                "JOIN consultation c ON p.id = c.patient_id " +
                "JOIN anthropometric_record a ON c.id = a.consultation_id " +
                "ORDER BY c.created_at DESC LIMIT 4";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String nombreCompleto = cursor.getString(0);
                double peso = cursor.getDouble(1);
                double imc = cursor.getDouble(2);
                lista.add(new String[]{nombreCompleto, "Peso registrado: " + peso + " kg | IMC actual: " + imc});
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Datos de respaldo predeterminados si la base de datos está inicialmente vacía
        if (lista.isEmpty()) {
            lista.add(new String[]{"Ana Torres", "↓ Redujo 2.5 kg este mes"});
            lista.add(new String[]{"Carlos Ruiz", "✓ Alcanzó su meta de hidratación"});
            lista.add(new String[]{"María Vargas", "↓ IMC bajó de 28.5 a 26.2"});
            lista.add(new String[]{"Jorge Pérez", "✓ Triglicéridos en niveles normales"});
        }
        return lista;
    }

    private void insertarDatosDePrueba(SQLiteDatabase db) {
        String mesActual = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
        String hoy = mesActual + "-15";

        String id1 = UUID.randomUUID().toString();
        ContentValues p1 = new ContentValues();
        p1.put("id", id1);
        p1.put("first_name", "Ana");
        p1.put("last_name", "Torres");
        p1.put("birth_date", "1990-01-01");
        db.insert("patient", null, p1);

        String id2 = UUID.randomUUID().toString();
        ContentValues p2 = new ContentValues();
        p2.put("id", id2);
        p2.put("first_name", "Carlos");
        p2.put("last_name", "Ruiz");
        p2.put("birth_date", "1985-06-15");
        db.insert("patient", null, p2);

        String cId1 = UUID.randomUUID().toString();
        ContentValues c1 = new ContentValues();
        c1.put("id", cId1);
        c1.put("patient_id", id1);
        c1.put("date", hoy);
        c1.put("type", "control");
        db.insert("consultation", null, c1);

        ContentValues a1 = new ContentValues();
        a1.put("id", UUID.randomUUID().toString());
        a1.put("consultation_id", cId1);
        a1.put("weight_kg", 68.5);
        a1.put("bmi", 24.2);
        db.insert("anthropometric_record", null, a1);
    }
}