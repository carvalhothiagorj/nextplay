package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;

@Entity
public class ResultadosPartida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;

    @Column(columnDefinition = "TEXT")
    private String detalhesResultado; 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    public String getDetalhesResultado() {
        return detalhesResultado;
    }

    public void setDetalhesResultado(String detalhesResultado) {
        this.detalhesResultado = detalhesResultado;
    }
}
