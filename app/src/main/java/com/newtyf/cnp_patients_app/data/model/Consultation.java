package com.newtyf.cnp_patients_app.data.model;

public class Consultation {

    private String id;
    private String patientId;
    private String date;
    private String type;
    private String reason;
    private String notes;
    private String createdAt;

    private AnthropometricRecord anthropometricRecord;

    public Consultation() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPatientId() { return patientId; }
    public void setPatientId(String patientId) { this.patientId = patientId; }

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

    public AnthropometricRecord getAnthropometricRecord() { return anthropometricRecord; }
    public void setAnthropometricRecord(AnthropometricRecord anthropometricRecord) { this.anthropometricRecord = anthropometricRecord; }
}
