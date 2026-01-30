package br.com.beca.ms_transacoes.application.dto;

import java.math.BigDecimal;

import br.com.beca.ms_transacoes.domain.entities.CategoriaTransacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record CriarTransacaoRequest(

        @NotNull(message = "ID do usuário é obrigatório")
        Long usuarioId,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "A moeda é obrigatória (ex: BRL, USD)")
        String moeda,
        CategoriaTransacao categoria
) {}
