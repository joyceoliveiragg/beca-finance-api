package br.com.beca.ms_usuarios.infra.controllers;

import br.com.beca.ms_usuarios.application.dto.CadastroUsuarioRequest;
import br.com.beca.ms_usuarios.application.dto.UsuarioResponse;
import br.com.beca.ms_usuarios.application.usecases.CadastrarUsuarioUseCase;
import br.com.beca.ms_usuarios.application.usecases.DebitarSaldoUseCase;
import br.com.beca.ms_usuarios.application.dto.DebitoRequest;
import br.com.beca.ms_usuarios.infra.persistence.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    @Autowired
    private DebitarSaldoUseCase debitarSaldoUseCase;

    @Autowired
    private UsuarioRepository repository;

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody @Valid CadastroUsuarioRequest request, UriComponentsBuilder uriBuilder) {
        UsuarioResponse usuario = cadastrarUsuarioUseCase.executar(request);
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.id()).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(usuario -> ResponseEntity.ok(new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getSaldo()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/debito")
    public ResponseEntity<Void> debitar(@PathVariable Long id, @RequestBody DebitoRequest request) {
        debitarSaldoUseCase.executar(id, request.valor());
        return ResponseEntity.noContent().build();
    }
}