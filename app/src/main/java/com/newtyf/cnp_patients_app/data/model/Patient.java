package com.newtyf.cnp_patients_app.data.model;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Patient {

    private String id;
    private String nutritionistId;
    private String firstName;
    private String lastName;
    private String dni;
    private String birthDate;
    private String gender;
    private String phone;
    private String email;
    private String photoPath;
    private String notes;
    private String createdAt;

    public Patient() {}

    public Patient(String id, String nutritionistId, String firstName, String lastName,
                   String dni, String birthDate, String gender, String phone,
                   String email, String photoPath, String notes) {
        this.id = id;
        this.nutritionistId = nutritionistId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.photoPath = photoPath;
        this.notes = notes;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = LocalDate.parse(this.birthDate, format);
        return Period.between(date, LocalDate.now()).getYears();
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNutritionistId() { return nutritionistId; }
    public void setNutritionistId(String nutritionistId) { this.nutritionistId = nutritionistId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhotoPath() { return photoPath; }
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
