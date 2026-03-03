package com.healthapp.healthapp.service;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.healthapp.healthapp.dto.AuthResponse;
import com.healthapp.healthapp.dto.LoginRequest;
import com.healthapp.healthapp.dto.RegisterRequest;
import com.healthapp.healthapp.exception.ResourceNotFoundException;
import com.healthapp.healthapp.model.Paciente;
import com.healthapp.healthapp.model.Rol;
import com.healthapp.healthapp.model.User;
import com.healthapp.healthapp.repository.PacienteRepository;
import com.healthapp.healthapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PacienteRepository pacienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya esta registrado");
        }

        Rol rol = parseRol(request.getRol());

        User user = User.builder()
                .nombre(request.getNombre())
                .email(request.getEmail().trim().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(rol)
                .build();

        User savedUser = userRepository.save(user);

        if (rol == Rol.ROLE_PACIENTE) {
            Paciente paciente = Paciente.builder()
                    .nombre(request.getNombre())
                    .apellidos("")
                    .fechaNacimiento(LocalDate.of(2000, 1, 1))
                    .telefono("")
                    .usuarioId(savedUser.getId())
                    .build();
            pacienteRepository.save(paciente);
        }
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Credenciales invalidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .nombre(user.getNombre())
                .rol(user.getRol().name())
                .build();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private Rol parseRol(String rolValue) {
        try {
            String normalized = rolValue.trim().toUpperCase();
            if (!normalized.startsWith("ROLE_")) {
                normalized = "ROLE_" + normalized;
            }
            return Rol.valueOf(normalized);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Rol no valido. Usa ROLE_PACIENTE o ROLE_MEDICO");
        }
    }
}
