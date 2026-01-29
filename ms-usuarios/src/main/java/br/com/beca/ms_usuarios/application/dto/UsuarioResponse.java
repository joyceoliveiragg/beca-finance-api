package br.com.beca.ms_usuarios.application.dto;

import br.com.beca.ms_usuarios.domain.entities.Usuario;

public record UsuarioResponse(Long id, String nome, String email) {
    public UsuarioResponse(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}