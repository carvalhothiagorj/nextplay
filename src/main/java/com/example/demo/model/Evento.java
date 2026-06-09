package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nomeEvento;

    @Column(nullable = false, length = 1000)
    private String descricao;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "data_inicio", nullable = false)
    private Date dataInicio;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "data_fim")
    private Date dataFim;

    @Column(nullable = false)
    private String local;

    @ManyToOne
    @JoinColumn(name = "criador_organizador_id", nullable = false)
    private Usuario criadorOrganizador;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeEvento() {
        return nomeEvento;
    }

    public void setNomeEvento(String nomeEvento) {
        this.nomeEvento = nomeEvento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Usuario getCriadorOrganizador() {
        return criadorOrganizador;
    }

    public void setCriadorOrganizador(Usuario criadorOrganizador) {
        this.criadorOrganizador = criadorOrganizador;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}