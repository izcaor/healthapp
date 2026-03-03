package com.healthapp.healthapp.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CitaFormDto {

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha debe ser hoy o futura")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime hora;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    private String descripcion;
}
