package com.healthapp.healthapp.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.healthapp.healthapp.dto.CitaViewDto;
import com.healthapp.healthapp.model.EstadoCita;
import com.healthapp.healthapp.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final PacienteRepository pacienteRepository;

    public List<CitaViewDto> listarCitas(LocalDate fecha, EstadoCita estado) {
        return pacienteRepository.findAll().stream()
                .flatMap(paciente -> paciente.getCitas().stream()
                        .map(cita -> CitaViewDto.builder()
                                .pacienteId(paciente.getId())
                                .pacienteNombreCompleto((paciente.getNombre() + " " + paciente.getApellidos()).trim())
                                .citaId(cita.getId())
                                .fecha(cita.getFecha())
                                .hora(cita.getHora())
                                .especialidad(cita.getEspecialidad())
                                .descripcion(cita.getDescripcion())
                                .estado(cita.getEstado())
                                .build()))
                .filter(cita -> fecha == null || Objects.equals(cita.getFecha(), fecha))
                .filter(cita -> estado == null || cita.getEstado() == estado)
                .sorted(Comparator.comparing(CitaViewDto::getFecha).thenComparing(CitaViewDto::getHora))
                .toList();
    }
}
