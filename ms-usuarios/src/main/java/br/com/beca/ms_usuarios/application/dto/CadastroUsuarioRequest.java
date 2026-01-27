package br.com.beca.ms_usuarios.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CadastroUsuarioRequest(
        @NotBlank
        String nome,

        @NotBlank
        @Email
        String email,

        @NotBlank
        String senha,

        @NotBlank
        String cpf,

        @NotNull
        BigDecimal saldo
) {}