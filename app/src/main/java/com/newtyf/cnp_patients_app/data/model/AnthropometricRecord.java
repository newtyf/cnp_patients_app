package com.newtyf.cnp_patients_app.data.model;

public class AnthropometricRecord {

    public static final double[] NAF_FACTORES = { 1.2, 1.375, 1.55, 1.725, 1.9 };

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

    private double calcularIdealWeight(double weightKg, double currentFatPct, double targetFatPct) {
        double leanMass = weightKg * (1 - (currentFatPct / 100));

        return leanMass / (1 - (targetFatPct / 100));
    }

    public Double calcularIdealWeight() {
        return calcularIdealWeight(weightKg, bodyFatPct, 15.0);
    }

    public static Double calcularBmi(Double weightKg, Double heightCm) {
        double alturaM = heightCm / 100.0;
        return weightKg / (alturaM * alturaM);
    }

    public static String categoriaImc(double bmi) {
        if (bmi < 18.5) return "Bajo peso";
        if (bmi < 25.0) return "Normal";
        if (bmi < 30.0) return "Sobrepeso";
        if (bmi < 35.0) return "Obesidad I";
        if (bmi < 40.0) return "Obesidad II";
        return "Obesidad III";
    }

    public static Double calcularBmr(String formula, double weightKg, double heightCm, Double bodyFatPct) {
        switch (formula) {
            case "Harris-Benedict":
                return 88.362 + (13.397 * weightKg) + (4.799 * heightCm) - (5.677 * 30);
            case "Katch-McArdle":
                if (bodyFatPct == null) return null;
                double masaMagra = weightKg * (1 - bodyFatPct / 100.0);
                return 370 + (21.6 * masaMagra);
            default: // Mifflin-St Jeor
                return (10 * weightKg) + (6.25 * heightCm) - (5 * 30) + 5;
        }
    }

    public static double calcularTdee(double bmrKcal, double activityFactor) {
        return bmrKcal * activityFactor;
    }
}
