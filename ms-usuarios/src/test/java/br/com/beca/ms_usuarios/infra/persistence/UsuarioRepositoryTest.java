package br.com.beca.ms_usuarios.infra.persistence;

import br.com.beca.ms_usuarios.infra.persistence.entities.UsuarioEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Test
    void findByEmailAndAtivoTrue_deveIgnorarInativos() {
        var ativo = new UsuarioEntity();
        ativo.setNome("Ana");
        ativo.setEmail("ana@email.com");
        ativo.setCpf("123");
        ativo.setSenha("x");
        ativo.setAtivo(true);

        var inativo = new UsuarioEntity();
        inativo.setNome("Bruno");
        inativo.setEmail("bruno@email.com");
        inativo.setCpf("456");
        inativo.setSenha("x");
        inativo.setAtivo(false);

        repository.save(ativo);
        repository.save(inativo);

        assertThat(repository.findByEmailAndAtivoTrue("ana@email.com")).isPresent();
        assertThat(repository.findByEmailAndAtivoTrue("bruno@email.com")).isEmpty();
    }

    @Test
    void findAllByAtivoTrue_deveListarSomenteAtivos() {
        var u1 = new UsuarioEntity();
        u1.setNome("U1");
        u1.setEmail("u1@email.com");
        u1.setCpf("111");
        u1.setSenha("x");
        u1.setAtivo(true);

        var u2 = new UsuarioEntity();
        u2.setNome("U2");
        u2.setEmail("u2@email.com");
        u2.setCpf("222");
        u2.setSenha("x");
        u2.setAtivo(false);

        repository.save(u1);
        repository.save(u2);

        var ativos = repository.findAllByAtivoTrue();
        assertThat(ativos).hasSize(1);
        assertThat(ativos.get(0).getEmail()).isEqualTo("u1@email.com");
    }
}
