package br.com.beca.ms_transacoes.application.dto;

import java.math.BigDecimal;

public record ResumoFinanceiroDTO(
        Long usuarioId,
        BigDecimal totalGasto,
        int quantidadeTransacoes
) {}