package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

@Entity
public class Classificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Alterado para LAZY
    @JoinColumn(name = "competicao_id", nullable = false)
    private Competicao competicao;

    @ManyToOne(fetch = FetchType.LAZY) // Alterado para LAZY
    @JoinColumn(name = "participante_jogador_id") // O participante pode ser um jogador...
    private Usuario participanteJogador;

    @ManyToOne(fetch = FetchType.LAZY) // Alterado para LAZY
    @JoinColumn(name = "participante_equipe_id") // ... ou uma equipe
    private Equipe participanteEquipe;

    private Integer posicao; // Posição na classificação
    private Integer pontos;
    private Integer vitorias;
    private Integer derrotas;
    private Integer empates;

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

    public Usuario getParticipanteJogador() {
        return participanteJogador;
    }

    public void setParticipanteJogador(Usuario participanteJogador) {
        this.participanteJogador = participanteJogador;
    }

    public Equipe getParticipanteEquipe() {
        return participanteEquipe;
    }

    public void setParticipanteEquipe(Equipe participanteEquipe) {
        this.participanteEquipe = participanteEquipe;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public Integer getPontos() {
        return pontos;
    }

    public void setPontos(Integer pontos) {
        this.pontos = pontos;
    }

    public Integer getVitorias() {
        return vitorias;
    }

    public void setVitorias(Integer vitorias) {
        this.vitorias = vitorias;
    }

    public Integer getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(Integer derrotas) {
        this.derrotas = derrotas;
    }

    public Integer getEmpates() {
        return empates;
    }

    public void setEmpates(Integer empates) {
        this.empates = empates;
    }
}
