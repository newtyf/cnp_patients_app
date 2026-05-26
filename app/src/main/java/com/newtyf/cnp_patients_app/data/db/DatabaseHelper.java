package com.newtyf.cnp_patients_app.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: cnp_patients_app
 * Created by: Axel Muñoz
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    static private final String db_name = "app_db";
    static private final Integer db_version = 1;
    static private DatabaseHelper instance = null;

    private DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    static public DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context, db_name, null, db_version);
            return instance;
        } else {
            return instance;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE patients (" +
                "id TEXT PRIMARY KEY"+
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public String healthCheckTables() {
        String[] projection = {"name"};
        String[] selectionArgs = {"table"};
        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor c = db.query("sqlite_master", projection, "type = ?", selectionArgs, null, null, null);

        List<String> tableNames = new ArrayList<>();
        while (c.moveToNext()) {
            String name = c.getString(
                    c.getColumnIndexOrThrow("name")
            );
            tableNames.add(name);
        }
        c.close();
        return tableNames.toString();
    }
}
