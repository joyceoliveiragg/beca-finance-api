package br.com.beca.ms_transacoes.infra.feign;

import com.fasterxml.jackson.annotation.JsonAlias;

public record CambioDTO(
        @JsonAlias("USDBRL") Moeda moeda
) {
    public record Moeda(
            @JsonAlias("bid") String cotacao
    ) {}
}