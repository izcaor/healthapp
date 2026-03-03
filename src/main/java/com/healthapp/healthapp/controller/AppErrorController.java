package com.healthapp.healthapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppErrorController {

    @GetMapping("/error/403")
    public String forbidden(Model model) {
        model.addAttribute("status", 403);
        model.addAttribute("error", "Acceso denegado");
        model.addAttribute("message", "No tienes permisos para acceder a este recurso.");
        return "error/error";
    }
}
