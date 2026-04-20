package com.newtyf.cnp_patients_app.models;

public class Patient {
    private String name;
    private String dni;
    private String email;
    private String phone;
    private String birthDate;
    private String objective;

    public Patient(String name, String dni, String email, String phone, String birthDate, String objective) {
        this.name = name;
        this.dni = dni;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.objective = objective;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getObjective() { return objective; }
    public void setObjective(String objective) { this.objective = objective; }
}
