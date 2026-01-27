package br.com.beca.ms_transacoes.infra.feign;

import java.math.BigDecimal;

public record UsuarioDTO(Long id, String nome, BigDecimal saldo) {}
