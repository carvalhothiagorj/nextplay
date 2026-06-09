package com.example.demo.repository;

import com.example.demo.model.Evento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    
	List<Evento> findByNomeEventoContainingIgnoreCase(String nome);

}
