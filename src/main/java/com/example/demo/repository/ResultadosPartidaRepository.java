package com.example.demo.repository;

import com.example.demo.model.ResultadosPartida;
import com.example.demo.model.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultadosPartidaRepository extends JpaRepository<ResultadosPartida, Long> {
    // Método para encontrar detalhes de resultado por partida
    Optional<ResultadosPartida> findByPartida(Partida partida);
}
