package com.healthapp.healthapp.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute("status", HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute("error", "Recurso no encontrado");
        model.addAttribute("message", ex.getMessage());
        return "error/error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleBadRequest(IllegalArgumentException ex, Model model, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        model.addAttribute("status", HttpServletResponse.SC_BAD_REQUEST);
        model.addAttribute("error", "Solicitud invalida");
        model.addAttribute("message", ex.getMessage());
        return "error/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneric(Exception ex, Model model, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        model.addAttribute("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        model.addAttribute("error", "Error interno");
        model.addAttribute("message", ex.getMessage());
        return "error/error";
    }
}
