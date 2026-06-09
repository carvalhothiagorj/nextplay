package com.example.demo.repository;

import com.example.demo.model.Partida;
import com.example.demo.model.Competicao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByCompeticao(Competicao competicao);
    
    void deleteAllByCompeticaoId(Long competicaoId);

}
