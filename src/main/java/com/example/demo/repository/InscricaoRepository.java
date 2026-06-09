package com.example.demo.repository;

import com.example.demo.model.Inscricao;
import com.example.demo.model.Competicao;
import com.example.demo.model.Usuario;
import com.example.demo.model.Equipe;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    @EntityGraph(attributePaths = {"jogador", "equipe"})
    List<Inscricao> findByCompeticao(Competicao competicao);

   
    @EntityGraph(attributePaths = {"competicao", "jogador"})
    Optional<Inscricao> findByCompeticaoAndJogador(Competicao competicao, Usuario jogador);

   
    @EntityGraph(attributePaths = {"competicao", "equipe"})
    Optional<Inscricao> findByCompeticaoAndEquipe(Competicao competicao, Equipe equipe);
    
    boolean existsByCompeticaoIdAndJogadorId(Long competicaoId, Long jogadorId);

    long countByCompeticao(Competicao competicao);
   
    void deleteAllByCompeticaoId(Long competicaoId);
    
}
