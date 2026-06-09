package com.newtyf.cnp_patients_app.domain.models;

public class ConsultaDTO {
    private String pacienteNombre;
    private String detalle;

    public ConsultaDTO(String pacienteNombre, String detalle) {
        this.pacienteNombre = pacienteNombre;
        this.detalle = detalle;
    }

    public String getPacienteNombre() { return pacienteNombre; }
    public String getDetalle() { return detalle; }
}