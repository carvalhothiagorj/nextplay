// src/main/java/com/example/demo/model/MembrosEquipe.java
package com.example.demo.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "membros_equipe")
@IdClass(MembrosEquipe.MembrosEquipeId.class)
public class MembrosEquipe {

    @Id
    @ManyToOne
    @JoinColumn(name = "equipe_id", nullable = false)
    private Equipe equipe;

    @Id
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false) // <--- Garanta que é 'usuario_id'
    private Usuario usuario; // <--- Garanta que é do tipo Usuario (e não Jogador)

    @Column(name = "data_adesao", nullable = false)
    private Date dataAdesao;

    public MembrosEquipe() { this.dataAdesao = new Date(); }

    public MembrosEquipe(Equipe equipe, Usuario usuario) { // Construtor recebe Usuario
        this.equipe = equipe;
        this.usuario = usuario;
        this.dataAdesao = new Date();
    }

    // --- Getters e Setters ---
    public Equipe getEquipe() { return equipe; }
    public void setEquipe(Equipe equipe) { this.equipe = equipe; }
    public Usuario getUsuario() { return usuario; } // <--- Getter para Usuario
    public void setUsuario(Usuario usuario) { this.usuario = usuario; } // <--- Setter para Usuario
    public Date getDataAdesao() { return dataAdesao; }
    public void setDataAdesao(Date dataAdesao) { this.dataAdesao = dataAdesao; }

    public static class MembrosEquipeId implements Serializable {
        private Long equipe;
        private Long usuario; // <--- Garanta que é 'usuario' (e não 'jogador')

        public MembrosEquipeId() {}
        public MembrosEquipeId(Long equipe, Long usuario) {
            this.equipe = equipe;
            this.usuario = usuario;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MembrosEquipeId that = (MembrosEquipeId) o;
            return Objects.equals(equipe, that.equipe) && Objects.equals(usuario, that.usuario);
        }

        @Override
        public int hashCode() {
            return Objects.hash(equipe, usuario);
        }
    }

	public Object getJogador() {
		// TODO Auto-generated method stub
		return null;
	}
}