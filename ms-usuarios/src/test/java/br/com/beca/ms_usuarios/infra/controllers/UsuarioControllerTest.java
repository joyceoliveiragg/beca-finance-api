package br.com.beca.ms_usuarios.infra.controllers;

import br.com.beca.ms_usuarios.application.usecases.CadastrarUsuarioUseCase;
import br.com.beca.ms_usuarios.application.usecases.ImportarUsuariosUseCase;
import br.com.beca.ms_usuarios.infra.persistence.UsuarioRepository;
import br.com.beca.ms_usuarios.infra.persistence.entities.UsuarioEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired MockMvc mvc;

    @MockBean UsuarioRepository repository;
    @MockBean CadastrarUsuarioUseCase cadastrarUsuarioUseCase;
    @MockBean ImportarUsuariosUseCase importarUsuariosUseCase;

    @Test
    void listar_deveRetornarAtivos() throws Exception {
        var u = new UsuarioEntity();
        u.setId(1L);
        u.setNome("Ana");
        u.setEmail("ana@email.com");
        u.setAtivo(true);

        when(repository.findAllByAtivoTrue()).thenReturn(List.of(u));

        mvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("ana@email.com"));
    }

    @Test
    void buscarPorId_deveRetornar404_quandoInativoOuInexistente() throws Exception {
        when(repository.findByIdAndAtivoTrue(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void inativar_deveRetornar204_eMarcarAtivoFalse() throws Exception {
        var u = new UsuarioEntity();
        u.setId(1L);
        u.setAtivo(true);

        when(repository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(u));

        mvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(repository).save(argThat(saved -> saved.getAtivo() != null && !saved.getAtivo()));
    }

    @Test
    void inativar_deveRetornar404_quandoNaoExisteOuJaInativo() throws Exception {
        when(repository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.empty());

        mvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNotFound());

        verify(repository, never()).save(any());
    }
}
