package br.com.beca.ms_usuarios.application.dto;

public record ResultadoImportacaoResponse(
        int importados,
        int erros
) {}
