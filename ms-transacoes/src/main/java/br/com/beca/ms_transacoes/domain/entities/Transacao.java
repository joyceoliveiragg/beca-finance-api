package br.com.beca.ms_transacoes.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private BigDecimal valor;
    private String moeda;

    @Enumerated(EnumType.STRING)
    private StatusTransacao status;

    private LocalDateTime dataCriacao;

    public Transacao(Long usuarioId, BigDecimal valor, String moeda) {
        this.usuarioId = usuarioId;
        this.valor = valor;
        this.moeda = moeda;
        this.status = StatusTransacao.PENDING;
        this.dataCriacao = LocalDateTime.now();
    }

    public void atualizarStatus(StatusTransacao novoStatus) {
        this.status = novoStatus;
    }
}