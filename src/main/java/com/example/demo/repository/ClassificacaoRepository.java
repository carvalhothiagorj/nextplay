package com.example.demo.repository;

import com.example.demo.model.Classificacao;
import com.example.demo.model.Competicao;
import com.example.demo.model.Usuario;
import com.example.demo.model.Equipe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassificacaoRepository extends JpaRepository<Classificacao, Long> {

    @EntityGraph(attributePaths = {"participanteJogador", "participanteEquipe"})
    List<Classificacao> findByCompeticaoOrderByPontosDescVitoriasAsc(Competicao competicao);

    @EntityGraph(attributePaths = {"competicao", "participanteJogador"})
    Optional<Classificacao> findByCompeticaoAndParticipanteJogador(Competicao competicao, Usuario participanteJogador);

    @EntityGraph(attributePaths = {"competicao", "participanteEquipe"})
    Optional<Classificacao> findByCompeticaoAndParticipanteEquipe(Competicao competicao, Equipe participanteEquipe);

    @EntityGraph(attributePaths = {"competicao"})
    List<Classificacao> findByParticipanteJogador(Usuario participanteJogador);
    
    void deleteAllByCompeticaoId(Long competicaoId);

}
