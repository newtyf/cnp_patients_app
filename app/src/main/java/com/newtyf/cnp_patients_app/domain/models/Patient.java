package com.newtyf.cnp_patients_app.domain.models;

import android.content.ContentValues;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Patient {

    // Campos exactos de la Base de Datos
    private String id; // El esquema indica que es de tipo TEXT [cite: 122]
    private String nutritionistId; // Relación con el nutricionista
    private String firstName;
    private String lastName;
    private String idNumber; // Equivale a tu DNI
    private String email;
    private String phone;
    private String birthDate;
    private String gender;
    private String photoPath;
    private int active; // 1 para activo, 0 para inactivo [cite: 246]

    // Constructor vacío requerido por muchas librerías de BD
    public Patient() {}

    // Constructor completo
    public Patient(String id, String nutritionistId, String firstName, String lastName,
                   String idNumber, String email, String phone, String birthDate,
                   String gender, String photoPath, int active) {
        this.id = id;
        this.nutritionistId = nutritionistId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.gender = gender;
        this.photoPath = photoPath;
        this.active = active;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNutritionistId() { return nutritionistId; }
    public void setNutritionistId(String nutritionistId) { this.nutritionistId = nutritionistId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public int getActive() { return active; }
    public void setActive(int active) { this.active = active; }

    // ================= LÓGICA DE NEGOCIO Y UTILIDADES =================

    /**
     * Devuelve el nombre completo del paciente para mostrarlo en la Interfaz.
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Calcula la edad en base a la fecha de nacimiento.
     * Asume que el formato guardado en BD es "dd/MM/yyyy". Si tu BD usa "yyyy-MM-dd", cambia el patrón.
     */
    public int getAge() {
        if (this.birthDate == null || this.birthDate.isEmpty()) return 0;
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate date = LocalDate.parse(this.birthDate, format);
            return Period.between(date, LocalDate.now()).getYears();
        } catch (Exception e) {
            return 0; // Manejo seguro de error de parseo
        }
    }

    /**
     * Prepara el objeto para ser insertado rápidamente en la BD mediante DatabaseHelper.
     */
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put("id", this.id);
        values.put("nutritionist_id", this.nutritionistId);
        values.put("first_name", this.firstName);
        values.put("last_name", this.lastName);
        values.put("id_number", this.idNumber);
        values.put("email", this.email);
        values.put("phone", this.phone);
        values.put("birth_date", this.birthDate);
        values.put("gender", this.gender);
        values.put("photo_path", this.photoPath);
        values.put("active", this.active);
        return values;
    }

    /**
     * Método temporal para buscar pacientes en la lista de prueba.
     * Pronto será reemplazado por una consulta a la Base de Datos.
     */
    public static Patient getByIdNumber(List<Patient> patients, String idNumber) {
        if (patients == null || idNumber == null) return null;

        for (Patient p : patients) {
            if (idNumber.equals(p.getIdNumber())) {
                return p;
            }
        }
        return null;
    }
}