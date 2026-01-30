package br.com.beca.ms_transacoes.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transacao {

    private Long id;
    private Long usuarioId;
    private BigDecimal valor;
    private String moeda;
    private CategoriaTransacao categoria;
    private StatusTransacao status;
    private LocalDateTime dataCriacao;

    public Transacao(
            Long id,
            Long usuarioId,
            BigDecimal valor,
            String moeda,
            CategoriaTransacao categoria,
            StatusTransacao status,
            LocalDateTime dataCriacao
    ) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.valor = valor;
        this.moeda = moeda;
        this.categoria = categoria;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    public Transacao(Long usuarioId, BigDecimal valor, String moeda, CategoriaTransacao categoria) {
        this.usuarioId = usuarioId;
        this.valor = valor;
        this.moeda = moeda;
        this.categoria = categoria;
        this.status = StatusTransacao.PENDING;
        this.dataCriacao = LocalDateTime.now();
    }

    public void atualizarStatus(StatusTransacao novoStatus) {
        this.status = novoStatus;
    }

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public BigDecimal getValor() { return valor; }
    public String getMoeda() { return moeda; }
    public CategoriaTransacao getCategoria() {return categoria;}
    public StatusTransacao getStatus() { return status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }

    public void setId(Long id) { this.id = id; }
}
