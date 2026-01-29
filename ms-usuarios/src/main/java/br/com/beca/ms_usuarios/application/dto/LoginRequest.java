package br.com.beca.ms_usuarios.application.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank
        String login,

        @NotBlank
        String senha
) {}