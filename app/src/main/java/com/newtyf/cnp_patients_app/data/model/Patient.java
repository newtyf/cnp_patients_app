package com.newtyf.cnp_patients_app.data.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public int getAge() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(this.birthDate, format);
        return Period.between(date, LocalDate.now()).getYears();
    }

    static public Patient getByDni(List<Patient> patients, String dni) {
        for (Patient p : patients) {
            if (p.getDni().equals(dni)) {
                return p;
            }
        }

        return null;
    }
}
