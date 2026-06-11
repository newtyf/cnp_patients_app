package com.newtyf.cnp_patients_app.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newtyf.cnp_patients_app.data.db.DatabaseHelper;
import com.newtyf.cnp_patients_app.data.model.AnthropometricRecord;
import com.newtyf.cnp_patients_app.data.model.Consultation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConsultationRepository {

    private final DatabaseHelper dbHelper;

    public ConsultationRepository(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public List<Consultation> getByPatient(String patientId) {
        List<Consultation> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql =
            "SELECT c.*, " +
            "  ar.id            AS ar_id," +
            "  ar.weight_kg     AS ar_weight_kg," +
            "  ar.height_cm     AS ar_height_cm," +
            "  ar.body_fat_pct  AS ar_body_fat_pct," +
            "  ar.muscle_mass_pct AS ar_muscle_mass_pct," +
            "  ar.bmi           AS ar_bmi," +
            "  ar.bmr_kcal      AS ar_bmr_kcal," +
            "  ar.tdee_kcal     AS ar_tdee_kcal," +
            "  ar.activity_factor AS ar_activity_factor," +
            "  ar.bmr_formula   AS ar_bmr_formula " +
            "FROM consultation c " +
            "LEFT JOIN anthropometric_record ar ON ar.consultation_id = c.id " +
            "WHERE c.patient_id = ? " +
            "ORDER BY c.date DESC";

        Cursor cursor = db.rawQuery(sql, new String[]{patientId});
        while (cursor.moveToNext()) {
            lista.add(fromCursor(cursor));
        }
        cursor.close();
        return lista;
    }

    public Consultation getById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String sql =
            "SELECT c.*, " +
            "  ar.id            AS ar_id," +
            "  ar.weight_kg     AS ar_weight_kg," +
            "  ar.height_cm     AS ar_height_cm," +
            "  ar.body_fat_pct  AS ar_body_fat_pct," +
            "  ar.muscle_mass_pct AS ar_muscle_mass_pct," +
            "  ar.bmi           AS ar_bmi," +
            "  ar.bmr_kcal      AS ar_bmr_kcal," +
            "  ar.tdee_kcal     AS ar_tdee_kcal," +
            "  ar.activity_factor AS ar_activity_factor," +
            "  ar.bmr_formula   AS ar_bmr_formula " +
            "FROM consultation c " +
            "LEFT JOIN anthropometric_record ar ON ar.consultation_id = c.id " +
            "WHERE c.id = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{id});
        Consultation consultation = null;
        if (cursor.moveToFirst()) {
            consultation = fromCursor(cursor);
        }
        cursor.close();
        return consultation;
    }

    public Consultation insert(Consultation consultation) {
        consultation.setId(UUID.randomUUID().toString());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertOrThrow("consultation", null, consultationToValues(consultation));
        return consultation;
    }

    public boolean update(Consultation consultation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.update("consultation", consultationToValues(consultation), "id = ?", new String[]{consultation.getId()});
        return rows > 0;
    }

    public boolean delete(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("consultation", "id = ?", new String[]{id});
        return rows > 0;
    }

    private Consultation fromCursor(Cursor c) {
        Consultation consultation = new Consultation();
        consultation.setId(c.getString(c.getColumnIndexOrThrow("id")));
        consultation.setPatientId(c.getString(c.getColumnIndexOrThrow("patient_id")));
        consultation.setDate(c.getString(c.getColumnIndexOrThrow("date")));
        consultation.setType(c.getString(c.getColumnIndexOrThrow("type")));
        consultation.setReason(c.getString(c.getColumnIndexOrThrow("reason")));
        consultation.setNotes(c.getString(c.getColumnIndexOrThrow("notes")));
        consultation.setCreatedAt(c.getString(c.getColumnIndexOrThrow("created_at")));

        int arIdIdx = c.getColumnIndex("ar_id");
        if (arIdIdx != -1 && !c.isNull(arIdIdx)) {
            AnthropometricRecord record = new AnthropometricRecord();
            record.setId(c.getString(arIdIdx));
            record.setConsultationId(consultation.getId());
            record.setWeightKg(getDoubleOrNull(c, "ar_weight_kg"));
            record.setHeightCm(getDoubleOrNull(c, "ar_height_cm"));
            record.setBodyFatPct(getDoubleOrNull(c, "ar_body_fat_pct"));
            record.setMuscleMassPct(getDoubleOrNull(c, "ar_muscle_mass_pct"));
            record.setBmi(getDoubleOrNull(c, "ar_bmi"));
            record.setBmrKcal(getDoubleOrNull(c, "ar_bmr_kcal"));
            record.setTdeeKcal(getDoubleOrNull(c, "ar_tdee_kcal"));
            record.setActivityFactor(getDoubleOrNull(c, "ar_activity_factor"));
            record.setBmrFormula(c.getString(c.getColumnIndexOrThrow("ar_bmr_formula")));
            consultation.setAnthropometricRecord(record);
        }

        return consultation;
    }

    private ContentValues consultationToValues(Consultation c) {
        ContentValues cv = new ContentValues();
        cv.put("id", c.getId());
        cv.put("patient_id", c.getPatientId());
        cv.put("date", c.getDate());
        cv.put("type", c.getType());
        cv.put("reason", c.getReason());
        cv.put("notes", c.getNotes());
        return cv;
    }

    private Double getDoubleOrNull(Cursor c, String col) {
        int idx = c.getColumnIndex(col);
        return (idx != -1 && !c.isNull(idx)) ? c.getDouble(idx) : null;
    }
}
