package com.healthapp.healthapp.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.healthapp.healthapp.dto.CitaFormDto;
import com.healthapp.healthapp.model.CitaMedica;
import com.healthapp.healthapp.model.EstadoCita;
import com.healthapp.healthapp.model.User;
import com.healthapp.healthapp.service.AuthService;
import com.healthapp.healthapp.service.PacienteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/paciente/citas")
@RequiredArgsConstructor
public class CitaController {

    private final PacienteService pacienteService;
    private final AuthService authService;

    @GetMapping
    public String listarCitas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) EstadoCita estado,
            Authentication authentication,
            Model model) {

        User user = authService.getUserByEmail(authentication.getName());
        model.addAttribute("citas", pacienteService.listarCitas(user.getId(), fecha, estado));
        model.addAttribute("fechaFiltro", fecha);
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("estados", EstadoCita.values());

        return "paciente/citas-lista";
    }

    @GetMapping("/nueva")
    public String nuevaCitaForm(Model model) {
        if (!model.containsAttribute("citaForm")) {
            model.addAttribute("citaForm", new CitaFormDto());
        }
        model.addAttribute("editing", false);
        return "paciente/cita-form";
    }

    @PostMapping("/nueva")
    public String crearCita(@Valid @ModelAttribute("citaForm") CitaFormDto form,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("editing", false);
            return "paciente/cita-form";
        }

        User user = authService.getUserByEmail(authentication.getName());
        pacienteService.crearCita(user.getId(), form);

        redirectAttributes.addFlashAttribute("success", "Cita creada correctamente");
        return "redirect:/paciente/citas";
    }

    @GetMapping("/{citaId}")
    public String detalleCita(@PathVariable String citaId, Authentication authentication, Model model) {
        User user = authService.getUserByEmail(authentication.getName());
        CitaMedica cita = pacienteService.obtenerCita(user.getId(), citaId);

        model.addAttribute("cita", cita);
        return "paciente/cita-detalle";
    }

    @GetMapping("/{citaId}/editar")
    public String editarCitaForm(@PathVariable String citaId, Authentication authentication, Model model) {
        User user = authService.getUserByEmail(authentication.getName());
        CitaMedica cita = pacienteService.obtenerCita(user.getId(), citaId);

        CitaFormDto form = new CitaFormDto();
        form.setFecha(cita.getFecha());
        form.setHora(cita.getHora());
        form.setEspecialidad(cita.getEspecialidad());
        form.setDescripcion(cita.getDescripcion());

        model.addAttribute("citaForm", form);
        model.addAttribute("editing", true);
        model.addAttribute("citaId", citaId);
        model.addAttribute("estado", cita.getEstado());
        return "paciente/cita-form";
    }

    @PostMapping("/{citaId}/editar")
    public String editarCita(@PathVariable String citaId,
            @Valid @ModelAttribute("citaForm") CitaFormDto form,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("editing", true);
            model.addAttribute("citaId", citaId);
            return "paciente/cita-form";
        }

        User user = authService.getUserByEmail(authentication.getName());
        pacienteService.editarCita(user.getId(), citaId, form);

        redirectAttributes.addFlashAttribute("success", "Cita actualizada correctamente");
        return "redirect:/paciente/citas/" + citaId;
    }

    @PostMapping("/{citaId}/cancelar")
    public String cancelarCita(@PathVariable String citaId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        User user = authService.getUserByEmail(authentication.getName());
        pacienteService.cancelarCita(user.getId(), citaId);

        redirectAttributes.addFlashAttribute("success", "Cita cancelada correctamente");
        return "redirect:/paciente/citas";
    }
}
