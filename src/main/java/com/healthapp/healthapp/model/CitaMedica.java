package com.healthapp.healthapp.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaMedica {
    private String id;
    private LocalDate fecha;
    private LocalTime hora;
    private String especialidad;
    private String descripcion;
    private EstadoCita estado;
}
