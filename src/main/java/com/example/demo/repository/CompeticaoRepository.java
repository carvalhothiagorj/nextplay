package com.example.demo.repository;

import com.example.demo.model.Competicao;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List; 
import java.util.Optional;

@Repository
public interface CompeticaoRepository extends JpaRepository<Competicao, Long> {
 
    List<Competicao> findByCriadorJogador_Id(Long criadorJogadorId);

	List<Competicao> findByDataInicioLessThanEqualAndDataFimGreaterThanEqual(Date dataFimEvento, Date dataInicioEvento);
	
	@Override
    @EntityGraph(attributePaths = {"criadorJogador"})
    Optional<Competicao> findById(Long id);
	
    List<Competicao> findByNomeCompeticaoContainingIgnoreCase(String nomeCompeticao);

}
