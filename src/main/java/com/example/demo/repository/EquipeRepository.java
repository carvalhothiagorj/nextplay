package com.example.demo.repository;

import com.example.demo.model.Equipe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {

    Equipe findByNomeEquipe(String nomeEquipe);

    List<Equipe> findByLider_Id(Long liderId);
}
