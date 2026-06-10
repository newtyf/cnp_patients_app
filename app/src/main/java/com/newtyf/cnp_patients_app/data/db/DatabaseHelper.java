package com.newtyf.cnp_patients_app.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "app_db";
    private static final int DB_VERSION = 3;
    private static DatabaseHelper instance = null;

    private DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON");
        db.execSQL(
            "CREATE TABLE nutritionist (" +
            "  id                TEXT PRIMARY KEY," +
            "  remote_id         TEXT UNIQUE," +
            "  email             TEXT NOT NULL," +
            "  full_name         TEXT," +
            "  license_number    TEXT," +
            "  specialty         TEXT," +
            "  avatar_path       TEXT," +
            "  subscription_plan TEXT," +
            "  subscription_until TEXT," +
            "  access_token      TEXT," +
            "  refresh_token     TEXT," +
            "  token_expires_at  TEXT," +
            "  last_sync         TEXT," +
            "  pin_hash          TEXT," +
            "  use_biometric     INTEGER DEFAULT 0" +
            ")"
        );
        db.execSQL(
            "CREATE TABLE patient (" +
            "  id               TEXT PRIMARY KEY," +
            "  nutritionist_id  TEXT NOT NULL," +
            "  first_name       TEXT NOT NULL," +
            "  last_name        TEXT NOT NULL," +
            "  dni              TEXT UNIQUE," +
            "  birth_date       TEXT NOT NULL," +
            "  gender           TEXT," +
            "  phone            TEXT," +
            "  email            TEXT," +
            "  photo_path       TEXT," +
            "  notes            TEXT," +
            "  created_at       TEXT DEFAULT (datetime('now'))," +
            "  FOREIGN KEY (nutritionist_id) REFERENCES nutritionist(id)" +
            ")"
        );
        db.execSQL(
            "CREATE TABLE consultation (" +
            "  id          TEXT PRIMARY KEY," +
            "  patient_id  TEXT NOT NULL," +
            "  date        TEXT NOT NULL," +
            "  type        TEXT," +
            "  reason      TEXT," +
            "  notes       TEXT," +
            "  created_at  TEXT DEFAULT (datetime('now'))," +
            "  FOREIGN KEY (patient_id) REFERENCES patient(id)" +
            ")"
        );
        db.execSQL(
            "CREATE TABLE anthropometric_record (" +
            "  id               TEXT PRIMARY KEY," +
            "  consultation_id  TEXT NOT NULL," +
            "  weight_kg        REAL," +
            "  height_cm        REAL," +
            "  body_fat_pct     REAL," +
            "  muscle_mass_pct  REAL," +
            "  bmi              REAL," +
            "  bmr_kcal         REAL," +
            "  tdee_kcal        REAL," +
            "  activity_factor  REAL," +
            "  bmr_formula      TEXT," +
            "  FOREIGN KEY (consultation_id) REFERENCES consultation(id)" +
            ")"
        );
        db.execSQL(
            "CREATE TABLE diet_plan (" +
            "  id               TEXT PRIMARY KEY," +
            "  nutritionist_id  TEXT NOT NULL," +
            "  name             TEXT NOT NULL," +
            "  description      TEXT," +
            "  target_calories  INTEGER," +
            "  protein_g        REAL," +
            "  carbs_g          REAL," +
            "  fat_g            REAL," +
            "  status           TEXT DEFAULT 'draft'," +
            "  FOREIGN KEY (nutritionist_id) REFERENCES nutritionist(id)" +
            ")"
        );
        db.execSQL(
            "CREATE TABLE patient_plan (" +
            "  id            TEXT PRIMARY KEY," +
            "  patient_id    TEXT NOT NULL," +
            "  plan_id       TEXT NOT NULL," +
            "  assigned_date TEXT NOT NULL," +
            "  start_date    TEXT," +
            "  end_date      TEXT," +
            "  active        INTEGER DEFAULT 1," +
            "  FOREIGN KEY (patient_id) REFERENCES patient(id)," +
            "  FOREIGN KEY (plan_id) REFERENCES diet_plan(id)" +
            ")"
        );
        db.execSQL(
            "CREATE TABLE diet_day (" +
            "  id           TEXT PRIMARY KEY," +
            "  plan_id      TEXT NOT NULL," +
            "  day_of_week  INTEGER," +
            "  date         TEXT," +
            "  FOREIGN KEY (plan_id) REFERENCES diet_plan(id)" +
            ")"
        );
        db.execSQL(
            "CREATE TABLE meal (" +
            "  id           TEXT PRIMARY KEY," +
            "  diet_day_id  TEXT NOT NULL," +
            "  type         TEXT," +
            "  meal_order    INTEGER DEFAULT 0," +
            "  FOREIGN KEY (diet_day_id) REFERENCES diet_day(id)" +
            ")"
        );
        db.execSQL(
            "CREATE TABLE food (" +
            "  id                TEXT PRIMARY KEY," +
            "  name              TEXT NOT NULL," +
            "  category          TEXT," +
            "  calories_per_100g REAL," +
            "  protein_per_100g  REAL," +
            "  carbs_per_100g    REAL," +
            "  fat_per_100g      REAL," +
            "  source            TEXT" +
            ")"
        );
        db.execSQL(
            "CREATE TABLE meal_item (" +
            "  id                  TEXT PRIMARY KEY," +
            "  meal_id             TEXT NOT NULL," +
            "  food_id             TEXT NOT NULL," +
            "  quantity_g          REAL NOT NULL," +
            "  serving_description TEXT," +
            "  calories            REAL," +
            "  protein_g           REAL," +
            "  carbs_g             REAL," +
            "  fat_g               REAL," +
            "  FOREIGN KEY (meal_id) REFERENCES meal(id)," +
            "  FOREIGN KEY (food_id) REFERENCES food(id)" +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS meal_item");
        db.execSQL("DROP TABLE IF EXISTS food");
        db.execSQL("DROP TABLE IF EXISTS meal");
        db.execSQL("DROP TABLE IF EXISTS diet_day");
        db.execSQL("DROP TABLE IF EXISTS patient_plan");
        db.execSQL("DROP TABLE IF EXISTS diet_plan");
        db.execSQL("DROP TABLE IF EXISTS anthropometric_record");
        db.execSQL("DROP TABLE IF EXISTS consultation");
        db.execSQL("DROP TABLE IF EXISTS patient");
        db.execSQL("DROP TABLE IF EXISTS nutritionist");
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON");
        }
    }

    public String healthCheckTables() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query("sqlite_master", new String[]{"name"}, "type = ?", new String[]{"table"}, null, null, null);
        List<String> names = new ArrayList<>();
        while (c.moveToNext()) {
            names.add(c.getString(c.getColumnIndexOrThrow("name")));
        }
        c.close();
        return names.toString();
    }
}
