package com.newtyf.cnp_patients_app.data.model;

public class AnthropometricRecord {

    private String id;
    private String consultationId;
    private Double weightKg;
    private Double heightCm;
    private Double bodyFatPct;
    private Double muscleMassPct;
    private Double bmi;
    private Double bmrKcal;
    private Double tdeeKcal;
    private Double activityFactor;
    private String bmrFormula;

    public AnthropometricRecord() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getConsultationId() { return consultationId; }
    public void setConsultationId(String consultationId) { this.consultationId = consultationId; }

    public Double getWeightKg() { return weightKg; }
    public void setWeightKg(Double weightKg) { this.weightKg = weightKg; }

    public Double getHeightCm() { return heightCm; }
    public void setHeightCm(Double heightCm) { this.heightCm = heightCm; }

    public Double getBodyFatPct() { return bodyFatPct; }
    public void setBodyFatPct(Double bodyFatPct) { this.bodyFatPct = bodyFatPct; }

    public Double getMuscleMassPct() { return muscleMassPct; }
    public void setMuscleMassPct(Double muscleMassPct) { this.muscleMassPct = muscleMassPct; }

    public Double getBmi() { return bmi; }
    public void setBmi(Double bmi) { this.bmi = bmi; }

    public Double getBmrKcal() { return bmrKcal; }
    public void setBmrKcal(Double bmrKcal) { this.bmrKcal = bmrKcal; }

    public Double getTdeeKcal() { return tdeeKcal; }
    public void setTdeeKcal(Double tdeeKcal) { this.tdeeKcal = tdeeKcal; }

    public Double getActivityFactor() { return activityFactor; }
    public void setActivityFactor(Double activityFactor) { this.activityFactor = activityFactor; }

    public String getBmrFormula() { return bmrFormula; }
    public void setBmrFormula(String bmrFormula) { this.bmrFormula = bmrFormula; }
}
