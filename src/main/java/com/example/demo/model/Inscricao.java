package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import java.util.Date;

@Entity
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Alterado para LAZY
    @JoinColumn(name = "competicao_id", nullable = false)
    private Competicao competicao;

    @ManyToOne(fetch = FetchType.LAZY) // Alterado para LAZY
    @JoinColumn(name = "jogador_id") // Pode ser nulo se for inscrição por equipe
    private Usuario jogador;

    @ManyToOne(fetch = FetchType.LAZY) // Alterado para LAZY
    @JoinColumn(name = "equipe_id") // Pode ser nulo se for inscrição individual
    private Equipe equipe;

    @Column(name = "data_inscricao", nullable = false)
    private Date dataInscricao;

    private String status; // Ex: 'pendente', 'confirmada', 'cancelada'

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Competicao getCompeticao() {
        return competicao;
    }

    public void setCompeticao(Competicao competicao) {
        this.competicao = competicao;
    }

    public Usuario getJogador() {
        return jogador;
    }

    public void setJogador(Usuario jogador) {
        this.jogador = jogador;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Date getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(Date dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
