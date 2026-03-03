package com.healthapp.healthapp.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.healthapp.healthapp.dto.CitaFormDto;
import com.healthapp.healthapp.dto.PacienteUpdateDto;
import com.healthapp.healthapp.exception.ResourceNotFoundException;
import com.healthapp.healthapp.model.CitaMedica;
import com.healthapp.healthapp.model.EstadoCita;
import com.healthapp.healthapp.model.Paciente;
import com.healthapp.healthapp.repository.PacienteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public Paciente obtenerPacientePorUsuario(String usuarioId) {
        return pacienteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe paciente asociado al usuario"));
    }

    public Paciente actualizarPaciente(String usuarioId, PacienteUpdateDto dto) {
        Paciente paciente = obtenerPacientePorUsuario(usuarioId);
        paciente.setNombre(dto.getNombre());
        paciente.setApellidos(dto.getApellidos());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setTelefono(dto.getTelefono());
        return pacienteRepository.save(paciente);
    }

    public List<CitaMedica> listarCitas(String usuarioId, LocalDate fecha, EstadoCita estado) {
        Paciente paciente = obtenerPacientePorUsuario(usuarioId);

        return paciente.getCitas().stream()
                .filter(cita -> fecha == null || Objects.equals(cita.getFecha(), fecha))
                .filter(cita -> estado == null || cita.getEstado() == estado)
                .sorted(Comparator.comparing(CitaMedica::getFecha).thenComparing(CitaMedica::getHora))
                .toList();
    }

    public CitaMedica obtenerCita(String usuarioId, String citaId) {
        Paciente paciente = obtenerPacientePorUsuario(usuarioId);

        return paciente.getCitas().stream()
                .filter(cita -> Objects.equals(cita.getId(), citaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));
    }

    public void crearCita(String usuarioId, CitaFormDto dto) {
        Paciente paciente = obtenerPacientePorUsuario(usuarioId);

        CitaMedica cita = CitaMedica.builder()
                .id(UUID.randomUUID().toString())
                .fecha(dto.getFecha())
                .hora(dto.getHora())
                .especialidad(dto.getEspecialidad())
                .descripcion(dto.getDescripcion())
                .estado(EstadoCita.PROGRAMADA)
                .build();

        paciente.getCitas().add(cita);
        pacienteRepository.save(paciente);
    }

    public void editarCita(String usuarioId, String citaId, CitaFormDto dto) {
        Paciente paciente = obtenerPacientePorUsuario(usuarioId);

        Optional<CitaMedica> citaOpt = paciente.getCitas().stream()
                .filter(cita -> Objects.equals(cita.getId(), citaId))
                .findFirst();

        CitaMedica cita = citaOpt.orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new IllegalArgumentException("No puedes editar una cita cancelada");
        }

        cita.setFecha(dto.getFecha());
        cita.setHora(dto.getHora());
        cita.setEspecialidad(dto.getEspecialidad());
        cita.setDescripcion(dto.getDescripcion());

        pacienteRepository.save(paciente);
    }

    public void cancelarCita(String usuarioId, String citaId) {
        Paciente paciente = obtenerPacientePorUsuario(usuarioId);

        CitaMedica cita = paciente.getCitas().stream()
                .filter(item -> Objects.equals(item.getId(), citaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        cita.setEstado(EstadoCita.CANCELADA);
        pacienteRepository.save(paciente);
    }
}
