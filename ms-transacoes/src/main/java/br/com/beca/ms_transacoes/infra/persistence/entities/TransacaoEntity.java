package br.com.beca.ms_transacoes.infra.persistence.entities;

import br.com.beca.ms_transacoes.domain.entities.CategoriaTransacao;
import br.com.beca.ms_transacoes.domain.entities.StatusTransacao;
import br.com.beca.ms_transacoes.domain.entities.Transacao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private BigDecimal valor;
    private String moeda;

    @Enumerated(EnumType.STRING)
    private CategoriaTransacao categoria;


    @Enumerated(EnumType.STRING)
    private StatusTransacao status;

    private LocalDateTime dataCriacao;

    public TransacaoEntity(Transacao transacao) {
        this.id = transacao.getId();
        this.usuarioId = transacao.getUsuarioId();
        this.valor = transacao.getValor();
        this.moeda = transacao.getMoeda();
        this.categoria = transacao.getCategoria();
        this.status = transacao.getStatus();
        this.dataCriacao = transacao.getDataCriacao();
    }

    public Transacao toDomain() {
        return new Transacao(
                this.id,
                this.usuarioId,
                this.valor,
                this.moeda,
                this.categoria,
                this.status,
                this.dataCriacao
        );
    }
}
