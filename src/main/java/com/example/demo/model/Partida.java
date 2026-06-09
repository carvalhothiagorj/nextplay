package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.Date;

@Entity
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "competicao_id", nullable = false)
    private Competicao competicao;

    @ManyToOne
    @JoinColumn(name = "participante1_id") // Pode ser Jogador ou Equipe
    private Usuario participante1Jogador;

    @ManyToOne
    @JoinColumn(name = "participante1_equipe_id") // Pode ser Jogador ou Equipe
    private Equipe participante1Equipe;

    @ManyToOne
    @JoinColumn(name = "participante2_id") // Pode ser Jogador ou Equipe
    private Usuario participante2Jogador;

     @ManyToOne
    @JoinColumn(name = "participante2_equipe_id") // Pode ser Jogador ou Equipe
    private Equipe participante2Equipe;

    private String resultado; // Ex: "Vencedor: Participante 1", "Empate"

    @Column(name = "data_hora")
    private Date dataHora;

    @Column(name = "fase_torneio")
    private String faseTorneio; // Ex: "Fase de Grupos", "Final"

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

    public Usuario getParticipante1Jogador() {
        return participante1Jogador;
    }

    public void setParticipante1Jogador(Usuario participante1Jogador) {
        this.participante1Jogador = participante1Jogador;
    }

    public Equipe getParticipante1Equipe() {
        return participante1Equipe;
    }

    public void setParticipante1Equipe(Equipe participante1Equipe) {
        this.participante1Equipe = participante1Equipe;
    }

    public Usuario getParticipante2Jogador() {
        return participante2Jogador;
    }

    public void setParticipante2Jogador(Usuario participante2Jogador) {
        this.participante2Jogador = participante2Jogador;
    }

    public Equipe getParticipante2Equipe() {
        return participante2Equipe;
    }

    public void setParticipante2Equipe(Equipe participante2Equipe) {
        this.participante2Equipe = participante2Equipe;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public String getFaseTorneio() {
        return faseTorneio;
    }

    public void setFaseTorneio(String faseTorneio) {
        this.faseTorneio = faseTorneio;
    }
}
