package com.example.demo.service;

import com.example.demo.model.Partida;
import com.example.demo.model.Competicao;
import com.example.demo.repository.PartidaRepository;
import com.example.demo.repository.CompeticaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;

    @Autowired
    private CompeticaoRepository competicaoRepository;

    public Partida criarPartida(Partida partida, Long competicaoId) {
        Competicao competicao = competicaoRepository.findById(competicaoId)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));

        partida.setCompeticao(competicao);
        

        return partidaRepository.save(partida);
    }

    public Optional<Partida> findById(Long id) {
        return partidaRepository.findById(id);
    }

    public List<Partida> listarPartidasPorCompeticao(Long competicaoId) {
        Competicao competicao = competicaoRepository.findById(competicaoId)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));
        return partidaRepository.findByCompeticao(competicao);
    }

    public Partida atualizarResultadoPartida(Long partidaId, String resultado) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));

        partida.setResultado(resultado);

        return partidaRepository.save(partida);
    }
}
