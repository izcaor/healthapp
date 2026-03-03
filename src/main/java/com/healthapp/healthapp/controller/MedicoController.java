package com.healthapp.healthapp.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.healthapp.healthapp.model.EstadoCita;
import com.healthapp.healthapp.service.MedicoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/medico/citas")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping
    public String listarCitas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) EstadoCita estado,
            Model model) {

        model.addAttribute("citas", medicoService.listarCitas(fecha, estado));
        model.addAttribute("fechaFiltro", fecha);
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("estados", EstadoCita.values());

        return "medico/citas-lista";
    }
}
