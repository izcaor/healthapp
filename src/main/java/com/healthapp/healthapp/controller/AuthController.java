package com.healthapp.healthapp.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.healthapp.healthapp.dto.AuthResponse;
import com.healthapp.healthapp.dto.LoginRequest;
import com.healthapp.healthapp.dto.RegisterRequest;
import com.healthapp.healthapp.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String TOKEN_COOKIE_NAME = "HEALTHAPP_TOKEN";

    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(Model model, Authentication authentication) {
        if (isAuthenticated(authentication)) {
            return "redirect:/";
        }

        if (!model.containsAttribute("loginRequest")) {
            model.addAttribute("loginRequest", new LoginRequest());
        }

        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model, Authentication authentication) {
        if (isAuthenticated(authentication)) {
            return "redirect:/";
        }

        if (!model.containsAttribute("registerRequest")) {
            RegisterRequest request = new RegisterRequest();
            request.setRol("ROLE_PACIENTE");
            model.addAttribute("registerRequest", request);
        }

        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            authService.register(request);
            redirectAttributes.addFlashAttribute("success", "Registro completado. Inicia sesion.");
            return "redirect:/auth/login";
        } catch (IllegalArgumentException ex) {
            bindingResult.reject("register.error", ex.getMessage());
            return "auth/register";
        }
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginRequest") LoginRequest request,
            BindingResult bindingResult,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "auth/login";
        }

        try {
            AuthResponse authResponse = authService.login(request);
            ResponseCookie cookie = ResponseCookie.from(TOKEN_COOKIE_NAME, authResponse.getToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(Duration.ofDays(1))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            if ("ROLE_MEDICO".equals(authResponse.getRol())) {
                return "redirect:/medico/citas";
            }

            return "redirect:/paciente/perfil";
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            redirectAttributes.addFlashAttribute("loginRequest", request);
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(TOKEN_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("Lax")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return "redirect:/auth/login";
    }

    private boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
