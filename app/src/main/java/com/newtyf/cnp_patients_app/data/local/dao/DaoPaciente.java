package com.newtyf.cnp_patients_app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.newtyf.cnp_patients_app.data.local.entity.EntidadPaciente;

import java.util.List;

@Dao
public interface DaoPaciente {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarPaciente(EntidadPaciente paciente);

    @Update
    void actualizarPaciente(EntidadPaciente paciente);

    @Delete
    void eliminarPaciente(EntidadPaciente paciente);

    // 🚀 MÉTODO AGREGADO PARA SOLUCIONAR EL ERROR DE COMPILACIÓN
    // Se añade un ordenamiento alfabético para una mejor UX
    @Query("SELECT * FROM patient ORDER BY first_name ASC, last_name ASC")
    List<EntidadPaciente> obtenerTodos();

    @Query("SELECT * FROM patient ORDER BY id DESC LIMIT 10")
    List<EntidadPaciente> obtenerPacientesRecientes();

    @Query("SELECT * FROM patient WHERE id = :id LIMIT 1")
    EntidadPaciente obtenerPacientePorId(String id);

    @Query("SELECT * FROM patient WHERE first_name LIKE '%' || :busqueda || '%' OR last_name LIKE '%' || :busqueda || '%'")
    List<EntidadPaciente> buscarPacientes(String busqueda);
}