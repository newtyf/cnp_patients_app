package com.newtyf.cnp_patients_app;

import android.app.Application;
import android.util.Log;

import com.newtyf.cnp_patients_app.data.db.DatabaseHelper;
import com.newtyf.cnp_patients_app.data.model.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: cnp_patients_app
 * Created by: Axel Muñoz
 */
public class NutritionistApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseHelper db = DatabaseHelper.getInstance(this);
        Log.i("DATABASE", "Database Initialized: " + db.getDatabaseName());
        Log.i("DATABASE", "Database Tables: " + db.healthCheckTables());
    }
}
