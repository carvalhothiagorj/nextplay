package com.example.demo.service;

import com.example.demo.model.ResultadosPartida;
import com.example.demo.model.Partida;
import com.example.demo.repository.ResultadosPartidaRepository;
import com.example.demo.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResultadosPartidaService {

    @Autowired
    private ResultadosPartidaRepository resultadosPartidaRepository;

    @Autowired
    private PartidaRepository partidaRepository;

    public ResultadosPartida salvarDetalhesResultado(Long partidaId, ResultadosPartida detalhesResultado) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));

        detalhesResultado.setPartida(partida);


        return resultadosPartidaRepository.save(detalhesResultado);
    }

    public Optional<ResultadosPartida> getDetalhesResultadoPorPartida(Long partidaId) {
        Partida partida = partidaRepository.findById(partidaId)
                .orElseThrow(() -> new RuntimeException("Partida não encontrada"));

        return resultadosPartidaRepository.findByPartida(partida);
    }

}
