package com.healthapp.healthapp.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.healthapp.healthapp.model.EstadoCita;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaViewDto {
    private String pacienteId;
    private String pacienteNombreCompleto;
    private String citaId;
    private LocalDate fecha;
    private LocalTime hora;
    private String especialidad;
    private String descripcion;
    private EstadoCita estado;
}
