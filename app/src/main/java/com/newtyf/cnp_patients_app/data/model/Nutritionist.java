package com.newtyf.cnp_patients_app.data.model;

public class Nutritionist {

    private String id;
    private String email;
    private String fullName;
    private String licenseNumber;
    private String specialty;
    private String pinHash;

    public Nutritionist() {}

    public Nutritionist(String id, String email, String fullName, String licenseNumber, String specialty) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.licenseNumber = licenseNumber;
        this.specialty = specialty;
    }

    public String getId()            { return id; }
    public void setId(String id)     { this.id = id; }

    public String getEmail()             { return email; }
    public void setEmail(String email)   { this.email = email; }

    public String getFullName()                { return fullName; }
    public void setFullName(String fullName)   { this.fullName = fullName; }

    public String getLicenseNumber()                   { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getSpecialty()               { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getPinHash()               { return pinHash; }
    public void setPinHash(String pinHash)   { this.pinHash = pinHash; }

    public boolean hasPin() {
        return pinHash != null && !pinHash.isEmpty();
    }
}
