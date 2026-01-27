package br.com.beca.ms_transacoes.infra.controllers;

import br.com.beca.ms_transacoes.application.dto.CriarTransacaoRequest;
import br.com.beca.ms_transacoes.application.usecases.CriarTransacaoUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    private CriarTransacaoUseCase useCase;

    @PostMapping
    public ResponseEntity<Void> criar(@RequestBody CriarTransacaoRequest request) {

        useCase.executar(request.usuarioId(), request.valor(), request.moeda());

        return ResponseEntity.accepted().build();
    }
}