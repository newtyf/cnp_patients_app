package com.newtyf.cnp_patients_app.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.newtyf.cnp_patients_app.data.local.entity.EntidadUsuario;

import java.util.List;

@Dao
public interface DaoUsuario {

    // Insertar un nuevo usuario (Para la pantalla de Registro)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertarUsuario(EntidadUsuario usuario);

    // 🔥 NUEVO: Verificar credenciales (Para la pantalla de Login)
    @Query("SELECT * FROM users WHERE email = :correo AND password = :clave LIMIT 1")
    EntidadUsuario verificarCredenciales(String correo, String clave);

    // Obtener un usuario específico por su correo (Para la pantalla de Perfil)
    @Query("SELECT * FROM users WHERE email = :correo LIMIT 1")
    EntidadUsuario obtenerUsuarioPorEmail(String correo);

    // Actualizar los datos de un usuario existente (Para la pantalla Editar Personal)
    @Update
    void actualizarUsuario(EntidadUsuario usuario);

    // Obtener todos los usuarios (Opcional, por si se necesita)
    @Query("SELECT * FROM users")
    List<EntidadUsuario> obtenerTodosLosUsuarios();
}