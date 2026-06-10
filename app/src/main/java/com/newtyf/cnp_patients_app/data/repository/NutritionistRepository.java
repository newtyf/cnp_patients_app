package com.newtyf.cnp_patients_app.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.newtyf.cnp_patients_app.data.db.DatabaseHelper;
import com.newtyf.cnp_patients_app.data.model.Nutritionist;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class NutritionistRepository {

    private final DatabaseHelper dbHelper;

    public NutritionistRepository(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    public void insert(Nutritionist nutritionist) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        nutritionist.setId(UUID.randomUUID().toString());
        db.insertOrThrow("nutritionist", null, toValues(nutritionist));
    }

    /** Devuelve el primer (y único) nutricionista registrado localmente, o null si no existe. */
    public Nutritionist getFirst() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("nutritionist", null, null, null, null, null, null, "1");
        if (c.moveToFirst()) {
            Nutritionist n = fromCursor(c);
            c.close();
            return n;
        }
        c.close();
        return null;
    }

    public boolean hasRegistered() {
        return getFirst() != null;
    }

    /** Guarda el PIN hasheado en SHA-256. */
    public void updatePin(String nutritionistId, String rawPin) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("pin_hash", sha256(rawPin));
        db.update("nutritionist", values, "id = ?", new String[]{nutritionistId});
    }

    /** Devuelve true si el PIN ingresado coincide con el hash guardado. */
    public boolean checkPin(String nutritionistId, String rawPin) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query("nutritionist", new String[]{"pin_hash"}, "id = ?", new String[]{nutritionistId}, null, null, null);
        if (c.moveToFirst()) {
            String stored = c.getString(c.getColumnIndexOrThrow("pin_hash"));
            c.close();
            return sha256(rawPin).equals(stored);
        }
        c.close();
        return false;
    }

    private ContentValues toValues(Nutritionist n) {
        ContentValues cv = new ContentValues();
        cv.put("id",             n.getId());
        cv.put("email",          n.getEmail());
        cv.put("full_name",      n.getFullName());
        cv.put("license_number", n.getLicenseNumber());
        cv.put("specialty",      n.getSpecialty());
        if (n.getPinHash() != null) cv.put("pin_hash", n.getPinHash());
        return cv;
    }

    private Nutritionist fromCursor(Cursor c) {
        Nutritionist n = new Nutritionist();
        n.setId(c.getString(c.getColumnIndexOrThrow("id")));
        n.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
        n.setFullName(c.getString(c.getColumnIndexOrThrow("full_name")));
        n.setLicenseNumber(c.getString(c.getColumnIndexOrThrow("license_number")));
        n.setSpecialty(c.getString(c.getColumnIndexOrThrow("specialty")));
        n.setPinHash(c.getString(c.getColumnIndexOrThrow("pin_hash")));
        return n;
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }
}
