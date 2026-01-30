package br.com.beca.ms_processor.application.dto;

import java.math.BigDecimal;

public record TransacaoEvento(
        Long id,
        Long usuarioId,
        BigDecimal valor,
        String moeda
) {
}