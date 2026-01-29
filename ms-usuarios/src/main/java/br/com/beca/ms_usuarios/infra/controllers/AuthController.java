package br.com.beca.ms_usuarios.infra.controllers;

import br.com.beca.ms_usuarios.application.dto.LoginRequest;
import br.com.beca.ms_usuarios.application.dto.UsuarioResponse;
import br.com.beca.ms_usuarios.infra.persistence.entities.UsuarioEntity;
import br.com.beca.ms_usuarios.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequest data) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());

        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UsuarioEntity) auth.getPrincipal());

        return ResponseEntity.ok(new TokenResponse(token));
    }

    public record TokenResponse(String token) {}
}