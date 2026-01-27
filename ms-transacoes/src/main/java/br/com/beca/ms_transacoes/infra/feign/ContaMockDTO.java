package br.com.beca.ms_transacoes.infra.feign;

import java.math.BigDecimal;

public record ContaMockDTO(String id, BigDecimal saldo, BigDecimal limite) {}