package com.newtyf.cnp_patients_app.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newtyf.cnp_patients_app.data.db.DatabaseHelper;
import com.newtyf.cnp_patients_app.data.model.AnthropometricRecord;

import java.util.UUID;

public class AnthropometricRepository {

    private final DatabaseHelper dbHelper;

    public AnthropometricRepository(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public AnthropometricRecord getByConsultation(String consultationId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("anthropometric_record", null,
                "consultation_id = ?", new String[]{consultationId},
                null, null, null);
        AnthropometricRecord record = null;
        if (c.moveToFirst()) {
            record = fromCursor(c);
        }
        c.close();
        return record;
    }

    public AnthropometricRecord insert(AnthropometricRecord record) {
        record.setId(UUID.randomUUID().toString());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertOrThrow("anthropometric_record", null, toValues(record));
        return record;
    }

    public boolean update(AnthropometricRecord record) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.update("anthropometric_record", toValues(record),
                "id = ?", new String[]{record.getId()});
        return rows > 0;
    }

    public boolean deleteByConsultation(String consultationId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("anthropometric_record",
                "consultation_id = ?", new String[]{consultationId});
        return rows > 0;
    }

    private AnthropometricRecord fromCursor(Cursor c) {
        AnthropometricRecord r = new AnthropometricRecord();
        r.setId(c.getString(c.getColumnIndexOrThrow("id")));
        r.setConsultationId(c.getString(c.getColumnIndexOrThrow("consultation_id")));
        r.setWeightKg(getDoubleOrNull(c, "weight_kg"));
        r.setHeightCm(getDoubleOrNull(c, "height_cm"));
        r.setBodyFatPct(getDoubleOrNull(c, "body_fat_pct"));
        r.setMuscleMassPct(getDoubleOrNull(c, "muscle_mass_pct"));
        r.setBmi(getDoubleOrNull(c, "bmi"));
        r.setBmrKcal(getDoubleOrNull(c, "bmr_kcal"));
        r.setTdeeKcal(getDoubleOrNull(c, "tdee_kcal"));
        r.setActivityFactor(getDoubleOrNull(c, "activity_factor"));
        r.setBmrFormula(c.getString(c.getColumnIndexOrThrow("bmr_formula")));
        return r;
    }

    private ContentValues toValues(AnthropometricRecord r) {
        ContentValues cv = new ContentValues();
        cv.put("id", r.getId());
        cv.put("consultation_id", r.getConsultationId());
        if (r.getWeightKg() != null)       cv.put("weight_kg", r.getWeightKg());
        if (r.getHeightCm() != null)       cv.put("height_cm", r.getHeightCm());
        if (r.getBodyFatPct() != null)     cv.put("body_fat_pct", r.getBodyFatPct());
        if (r.getMuscleMassPct() != null)  cv.put("muscle_mass_pct", r.getMuscleMassPct());
        if (r.getBmi() != null)            cv.put("bmi", r.getBmi());
        if (r.getBmrKcal() != null)        cv.put("bmr_kcal", r.getBmrKcal());
        if (r.getTdeeKcal() != null)       cv.put("tdee_kcal", r.getTdeeKcal());
        if (r.getActivityFactor() != null) cv.put("activity_factor", r.getActivityFactor());
        if (r.getBmrFormula() != null)     cv.put("bmr_formula", r.getBmrFormula());
        return cv;
    }

    private Double getDoubleOrNull(Cursor c, String col) {
        int idx = c.getColumnIndex(col);
        return (idx != -1 && !c.isNull(idx)) ? c.getDouble(idx) : null;
    }
}
