package br.com.beca.ms_usuarios.domain.entities;

public class Usuario {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;

    public Usuario(Long id, String nome, String email, String senha, String cpf) {
        if (cpf == null || cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido");
        }
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public String getCpf() { return cpf; }

}