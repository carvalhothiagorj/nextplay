package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Competicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * CORRIGIDO: Adicionada a anotação @JsonBackReference para quebrar o ciclo
     * de serialização entre Competicao e Usuario, resolvendo o problema da
     * lista de competições que não era exibida.
     */
    @ManyToOne
    @JoinColumn(name = "criador_jogador_id", nullable = false)
    @JsonBackReference 
    private Usuario criadorJogador;

    @Column(nullable = false)
    private String nomeCompeticao;

    @Column(nullable = false)
    private String modalidade;

    @Column(length = 1000)
    private String requisitos;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "data_inicio", nullable = false)
    private Date dataInicio;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "data_fim")
    private Date dataFim;

    @Column(name = "max_participantes")
    private Integer maxParticipantes;

    @Column(name = "vagas_disponiveis")
    private Integer vagasDisponiveis;

    @Column(name = "tipo_competicao", nullable = false)
    private String tipoCompeticao;

    @Column(name = "categoria")
    private String categoria;

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getCriadorJogador() {
        return criadorJogador;
    }

    public void setCriadorJogador(Usuario criadorJogador) {
        this.criadorJogador = criadorJogador;
    }

    public String getNomeCompeticao() {
        return nomeCompeticao;
    }

    public void setNomeCompeticao(String nomeCompeticao) {
        this.nomeCompeticao = nomeCompeticao;
    }

    public String getModalidade() {
        return modalidade;
    }

    public void setModalidade(String modalidade) {
        this.modalidade = modalidade;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Integer getMaxParticipantes() {
        return maxParticipantes;
    }

    public void setMaxParticipantes(Integer maxParticipantes) {
        this.maxParticipantes = maxParticipantes;
    }

    public Integer getVagasDisponiveis() {
        return vagasDisponiveis;
    }

    public void setVagasDisponiveis(Integer vagasDisponiveis) {
        this.vagasDisponiveis = vagasDisponiveis;
    }

    public String getTipoCompeticao() {
        return tipoCompeticao;
    }

    public void setTipoCompeticao(String tipoCompeticao) {
        this.tipoCompeticao = tipoCompeticao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
