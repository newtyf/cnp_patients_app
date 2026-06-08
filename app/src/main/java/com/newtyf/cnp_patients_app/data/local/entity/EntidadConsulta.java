package com.newtyf.cnp_patients_app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "consultation")
public class EntidadConsulta {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "nutritionist_id")
    public String nutritionistId;

    @ColumnInfo(name = "patient_id")
    public String patientId;

    public String date;
    public String type;
    public String reason;
    public String notes;

    @ColumnInfo(name = "created_at")
    public String createdAt;
}