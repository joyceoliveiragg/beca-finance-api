package br.com.beca.ms_usuarios.infra.controllers;

import br.com.beca.ms_usuarios.application.dto.CadastroUsuarioRequest;
import br.com.beca.ms_usuarios.application.dto.UsuarioResponse;
import br.com.beca.ms_usuarios.application.usecases.CadastrarUsuarioUseCase;
import br.com.beca.ms_usuarios.infra.persistence.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; 
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import br.com.beca.ms_usuarios.application.usecases.ImportarUsuariosUseCase;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private CadastrarUsuarioUseCase cadastrarUsuarioUseCase;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private ImportarUsuariosUseCase importarUsuariosUseCase;

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody @Valid CadastroUsuarioRequest request, UriComponentsBuilder uriBuilder) {
        UsuarioResponse usuario = cadastrarUsuarioUseCase.executar(request);
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.id()).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(usuarioEntity -> ResponseEntity.ok(new UsuarioResponse(
                        usuarioEntity.getId(),
                        usuarioEntity.getNome(),
                        usuarioEntity.getEmail()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importar(@RequestParam("file") MultipartFile file) {
        var resultado = importarUsuariosUseCase.executar(file);
        return ResponseEntity.ok(resultado);
    }


}