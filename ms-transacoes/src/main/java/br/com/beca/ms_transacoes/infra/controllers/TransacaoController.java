package br.com.beca.ms_transacoes.infra.controllers;

import br.com.beca.ms_transacoes.application.dto.CriarTransacaoRequest;
import br.com.beca.ms_transacoes.application.dto.ResumoFinanceiroDTO;
import br.com.beca.ms_transacoes.application.usecases.CriarTransacaoUseCase;
import br.com.beca.ms_transacoes.domain.entities.Transacao;
import br.com.beca.ms_transacoes.infra.persistence.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import br.com.beca.ms_transacoes.application.services.RelatorioService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private CriarTransacaoUseCase useCase;

    @Autowired
    private TransacaoRepository repository;

    @Autowired
    private RelatorioService relatorioService;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody CriarTransacaoRequest request) {
        useCase.executar(request.usuarioId(), request.valor(), request.moeda());
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/resumo/{usuarioId}")
    public ResponseEntity<ResumoFinanceiroDTO> obterResumo(@PathVariable Long usuarioId) {

        BigDecimal total = repository.calcularTotalGasto(usuarioId);

        List<Transacao> lista = repository.findByUsuarioId(usuarioId);

        ResumoFinanceiroDTO resumo = new ResumoFinanceiroDTO(usuarioId, total, lista.size());
        return ResponseEntity.ok(resumo);
    }

    @GetMapping("/relatorio/{usuarioId}")
    public ResponseEntity<byte[]> baixarRelatorio(@PathVariable Long usuarioId) {
        byte[] pdfBytes = relatorioService.gerarExtratoPdf(usuarioId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=extrato.pdf").contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
    }
}