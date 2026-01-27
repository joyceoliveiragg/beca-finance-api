package br.com.beca.ms_usuarios.infra.persistence.entities;

import jakarta.persistence.*;
import lombok.*;
import br.com.beca.ms_usuarios.domain.entities.Usuario;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    @Column(unique = true)
    private String cpf;

    public static UsuarioEntity fromDomain(Usuario usuario) {
        return new UsuarioEntity(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getCpf()
        );
    }

    public Usuario toDomain() {
        return new Usuario(
                this.id,
                this.nome,
                this.email,
                this.senha,
                this.cpf
        );
    }
}