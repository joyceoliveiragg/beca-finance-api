package br.com.beca.ms_transacoes.infra.feign;

import com.fasterxml.jackson.annotation.JsonAlias;

public record CambioDTO(
        String currency,
        String name,
        Double bid,
        LocalDateTime timestamp
) {}
