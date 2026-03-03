package com.healthapp.healthapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.healthapp.healthapp.dto.PacienteUpdateDto;
import com.healthapp.healthapp.model.Paciente;
import com.healthapp.healthapp.model.User;
import com.healthapp.healthapp.service.AuthService;
import com.healthapp.healthapp.service.PacienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/paciente")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;
    private final AuthService authService;

    @GetMapping("/perfil")
    public String verPerfil(Authentication authentication, Model model) {
        User user = authService.getUserByEmail(authentication.getName());
        Paciente paciente = pacienteService.obtenerPacientePorUsuario(user.getId());

        if (!model.containsAttribute("pacienteForm")) {
            PacienteUpdateDto form = new PacienteUpdateDto();
            form.setNombre(paciente.getNombre());
            form.setApellidos(paciente.getApellidos());
            form.setFechaNacimiento(paciente.getFechaNacimiento());
            form.setTelefono(paciente.getTelefono());
            model.addAttribute("pacienteForm", form);
        }

        model.addAttribute("paciente", paciente);
        model.addAttribute("usuario", user);
        return "paciente/perfil";
    }

    @PostMapping("/perfil")
    public String actualizarPerfil(@Valid @ModelAttribute("pacienteForm") PacienteUpdateDto form,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        User user = authService.getUserByEmail(authentication.getName());

        if (bindingResult.hasErrors()) {
            Paciente paciente = pacienteService.obtenerPacientePorUsuario(user.getId());
            model.addAttribute("paciente", paciente);
            model.addAttribute("usuario", user);
            return "paciente/perfil";
        }

        pacienteService.actualizarPaciente(user.getId(), form);
        redirectAttributes.addFlashAttribute("success", "Datos personales actualizados");
        return "redirect:/paciente/perfil";
    }
}
