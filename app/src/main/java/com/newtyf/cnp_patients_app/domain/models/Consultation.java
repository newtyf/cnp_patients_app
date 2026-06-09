package com.newtyf.cnp_patients_app.domain.models;

import androidx.room.ColumnInfo; // 🔥 Importación necesaria para Room

public class Consultation {
    private String id;

    // 🔥 Le decimos a Room que esta variable equivale a "nutritionist_id" en la BD
    @ColumnInfo(name = "nutritionist_id")
    private String nutritionistId;
    // 🔥 Le decimos a Room que esta variable equivale a "patient_id" en la BD
    @ColumnInfo(name = "patient_id")
    private String patientId;

    private String patientName; // Campo DTO para la UI (JOIN)
    private String date; // Contiene Fecha y Hora
    private String type;
    private String reason;
    private String notes;

    // 🔥 Agregamos el campo created_at que Room estaba devolviendo y no tenía dónde guardar
    @ColumnInfo(name = "created_at")
    private String createdAt;

    // ==========================================
    // Getters y Setters
    // ==========================================
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNutritionistId() { return nutritionistId; }
    public void setNutritionistId(String nutritionistId) { this.nutritionistId = nutritionistId; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}