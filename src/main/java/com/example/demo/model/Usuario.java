package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoPerfil", discriminatorType = DiscriminatorType.STRING)


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipoPerfil")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Jogador.class, name = "jogador"),
    @JsonSubTypes.Type(value = Organizador.class, name = "organizador")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(name = "tipoPerfil", insertable = false, updatable = false)
    private String tipoPerfil;


    @Column(name = "data_cadastro", nullable = false)
    private Date dataCadastro;

    private String fotoPerfil;
    
    @Column(length = 500)
    private String bio;

    @OneToMany(mappedBy = "criadorJogador", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Competicao> competicoesCriadas;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(String tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Competicao> getCompeticoesCriadas() {
        return competicoesCriadas;
    }

    public void setCompeticoesCriadas(List<Competicao> competicoesCriadas) {
        this.competicoesCriadas = competicoesCriadas;
    }
}
