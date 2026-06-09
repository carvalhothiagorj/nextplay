package com.example.demo.service;

import com.example.demo.model.Evento;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EventoRepository;
import com.example.demo.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Evento criarEvento(Evento evento, Long organizadorId) {

        Usuario organizador = usuarioRepository.findById(organizadorId)
                .orElseThrow(() -> new RuntimeException("Organizador não encontrado"));

        evento.setCriadorOrganizador(organizador);

        if (evento.getStatus() == null || evento.getStatus().isEmpty()) {
            evento.setStatus("futuro");
        }

        return eventoRepository.save(evento);
    }

    public List<Evento> findAll() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> findById(Long id) {
        return eventoRepository.findById(id);
    }

    public Evento updateEvent(Long id, Evento eventoAtualizado) {
        return eventoRepository.findById(id)
                .map(eventoExistente -> {
                    eventoExistente.setNomeEvento(eventoAtualizado.getNomeEvento());
                    eventoExistente.setDescricao(eventoAtualizado.getDescricao());
                    eventoExistente.setDataInicio(eventoAtualizado.getDataInicio());
                    eventoExistente.setDataFim(eventoAtualizado.getDataFim());
                    eventoExistente.setLocal(eventoAtualizado.getLocal());
                    eventoExistente.setStatus(eventoAtualizado.getStatus());
                    return eventoRepository.save(eventoExistente);
                })
                .orElseThrow(() -> new RuntimeException("Evento não encontrado com ID: " + id));
    }

    /**
     * Exclui um evento pelo seu ID.
     * @param id O ID do evento a ser excluído.
     * @throws RuntimeException se o evento não for encontrado.
     */
    public void deleteById(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new RuntimeException("Evento não encontrado com ID: " + id);
        }
        eventoRepository.deleteById(id);
    }
    
    public List<Evento> findByNome(String nome) {
        return eventoRepository.findByNomeEventoContainingIgnoreCase(nome);
    }
}
