package com.healthapp.healthapp.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "pacientes")
public class Paciente {

    @Id
    private String id;

    private String nombre;

    private String apellidos;

    private LocalDate fechaNacimiento;

    private String telefono;

    private String usuarioId;

    @Builder.Default
    private List<CitaMedica> citas = new ArrayList<>();
}
