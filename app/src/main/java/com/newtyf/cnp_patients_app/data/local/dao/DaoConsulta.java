package com.newtyf.cnp_patients_app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.newtyf.cnp_patients_app.data.local.entity.EntidadConsulta;
import com.newtyf.cnp_patients_app.domain.models.ConsultaDTO;
import com.newtyf.cnp_patients_app.domain.models.Consultation;

import java.util.List;

@Dao
public interface DaoConsulta {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertarConsulta(EntidadConsulta consulta);

    @Update
    void actualizarConsulta(EntidadConsulta consulta);

    @Delete
    void eliminarConsulta(EntidadConsulta consulta);

    @Query("SELECT * FROM consultation WHERE id = :id LIMIT 1")
    EntidadConsulta obtenerConsultaPorId(String id);

    // Reemplaza getConsultasDeHoy()
    // Nota: Room inyectará p.first_name, p.last_name, etc. en tu objeto ConsultaDTO
    @Query("SELECT p.first_name || ' ' || p.last_name AS pacienteNombre, " +
            "c.date || ' - ' || c.reason AS detalle " +
            "FROM consultation c " +
            "INNER JOIN patient p ON c.patient_id = p.id " +
            "ORDER BY c.date ASC")
    List<ConsultaDTO> obtenerConsultasDeHoy();

    // Reemplaza getAllConsultationsWithDetails()
    @Query("SELECT c.*, p.first_name || ' ' || p.last_name AS patientName " +
            "FROM consultation c " +
            "INNER JOIN patient p ON c.patient_id = p.id " +
            "ORDER BY c.created_at DESC")
    List<Consultation> obtenerTodasLasConsultasConDetalle();
}