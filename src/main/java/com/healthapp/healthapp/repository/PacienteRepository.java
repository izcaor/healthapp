package com.healthapp.healthapp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.healthapp.healthapp.model.Paciente;

public interface PacienteRepository extends MongoRepository<Paciente, String> {

    Optional<Paciente> findByUsuarioId(String usuarioId);
}
