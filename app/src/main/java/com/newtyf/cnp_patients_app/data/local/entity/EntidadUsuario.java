package com.newtyf.cnp_patients_app.data.local.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

// Se define el nombre de la tabla y se asegura que el email sea único
@Entity(tableName = "users", indices = {@Index(value = "email", unique = true)})
public class EntidadUsuario {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String email;
    public String password;
}