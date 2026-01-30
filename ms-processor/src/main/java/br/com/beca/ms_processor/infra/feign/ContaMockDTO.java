package br.com.beca.ms_processor.infra.feign;

import java.math.BigDecimal;

public record ContaMockDTO(
        Long id,
        BigDecimal saldo,
        BigDecimal limite
) {
}