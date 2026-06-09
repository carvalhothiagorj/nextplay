package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.MembrosEquipe;
import com.example.demo.model.Equipe;
import com.example.demo.model.Usuario; 
import java.util.Optional;

public interface MembrosEquipeRepository extends JpaRepository<MembrosEquipe, MembrosEquipe.MembrosEquipeId> {
   
    Optional<MembrosEquipe> findByEquipeAndUsuario(Equipe equipe, Usuario usuario);
}