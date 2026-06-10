package com.newtyf.cnp_patients_app.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newtyf.cnp_patients_app.data.db.DatabaseHelper;
import com.newtyf.cnp_patients_app.data.model.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PatientRepository {

    private final DatabaseHelper dbHelper;

    public PatientRepository(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public List<Patient> getAll(String nutritionistId) {
        List<Patient> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(
                "patient", null,
                "nutritionist_id = ?", new String[]{nutritionistId},
                null, null, "created_at DESC"
        );
        while (c.moveToNext()) {
            lista.add(fromCursor(c));
        }
        c.close();
        return lista;
    }

    public Patient getById(String id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("patient", null, "id = ?", new String[]{id}, null, null, null);
        Patient patient = null;
        if (c.moveToFirst()) {
            patient = fromCursor(c);
        }
        c.close();
        return patient;
    }

    public Patient insert(Patient patient) {
        patient.setId(UUID.randomUUID().toString());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.insertOrThrow("patient", null, toValues(patient));
        return patient;
    }

    public boolean update(Patient patient) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.update("patient", toValues(patient), "id = ?", new String[]{patient.getId()});
        return rows > 0;
    }

    public boolean delete(String id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("patient", "id = ?", new String[]{id});
        return rows > 0;
    }

    public List<Patient> search(String nutritionistId, String query) {
        List<Patient> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String like = "%" + query + "%";
        Cursor c = db.query(
                "patient", null,
                "nutritionist_id = ? AND (first_name LIKE ? OR last_name LIKE ? OR dni LIKE ?)",
                new String[]{nutritionistId, like, like, like},
                null, null, "created_at DESC"
        );
        while (c.moveToNext()) {
            lista.add(fromCursor(c));
        }
        c.close();
        return lista;
    }

    private ContentValues toValues(Patient p) {
        ContentValues cv = new ContentValues();
        cv.put("id", p.getId());
        cv.put("nutritionist_id", p.getNutritionistId());
        cv.put("first_name", p.getFirstName());
        cv.put("last_name", p.getLastName());
        cv.put("dni", p.getDni());
        cv.put("birth_date", p.getBirthDate());
        cv.put("gender", p.getGender());
        cv.put("phone", p.getPhone());
        cv.put("email", p.getEmail());
        cv.put("photo_path", p.getPhotoPath());
        cv.put("notes", p.getNotes());
        return cv;
    }

    private Patient fromCursor(Cursor c) {
        Patient p = new Patient();
        p.setId(c.getString(c.getColumnIndexOrThrow("id")));
        p.setNutritionistId(c.getString(c.getColumnIndexOrThrow("nutritionist_id")));
        p.setFirstName(c.getString(c.getColumnIndexOrThrow("first_name")));
        p.setLastName(c.getString(c.getColumnIndexOrThrow("last_name")));
        p.setDni(c.getString(c.getColumnIndexOrThrow("dni")));
        p.setBirthDate(c.getString(c.getColumnIndexOrThrow("birth_date")));
        p.setGender(c.getString(c.getColumnIndexOrThrow("gender")));
        p.setPhone(c.getString(c.getColumnIndexOrThrow("phone")));
        p.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
        p.setPhotoPath(c.getString(c.getColumnIndexOrThrow("photo_path")));
        p.setNotes(c.getString(c.getColumnIndexOrThrow("notes")));
        p.setCreatedAt(c.getString(c.getColumnIndexOrThrow("created_at")));
        return p;
    }
}
