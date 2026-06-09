package com.newtyf.cnp_patients_app.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Le indicamos a Room que esta clase representa la tabla "patient"
@Entity(tableName = "patient")
public class EntidadPaciente {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "nutritionist_id")
    public String nutritionistId;

    @ColumnInfo(name = "first_name")
    public String firstName;

    @ColumnInfo(name = "last_name")
    public String lastName;

    @ColumnInfo(name = "id_number")
    public String idNumber;

    public String email;
    public String phone;

    @ColumnInfo(name = "birth_date")
    public String birthDate;

    public String gender;

    @ColumnInfo(name = "photo_path")
    public String photoPath;

    public boolean active = true;

    // 🚀 CAMPO FALTANTE SEGÚN TU ESQUEMA DE BASE DE DATOS
    @ColumnInfo(name = "created_at")
    public String createdAt;

    // =========================================================
    // 🛠️ GETTERS NECESARIOS PARA EL PACIENTE ADAPTER
    // =========================================================

    public String getFirstName() {
        // Si por alguna razón el nombre es nulo, devolvemos cadena vacía para evitar crasheos
        return firstName != null ? firstName : "";
    }

    public String getLastName() {
        return lastName != null ? lastName : "";
    }

    // Método de utilidad para obtener el nombre completo fácilmente
    public String getNombreCompleto() {
        return getFirstName() + " " + getLastName();
    }
}