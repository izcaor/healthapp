package com.healthapp.healthapp.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/auth/login";
        }

        boolean isMedico = authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_MEDICO".equals(authority.getAuthority()));

        if (isMedico) {
            return "redirect:/medico/citas";
        }

        return "redirect:/paciente/perfil";
    }
}
