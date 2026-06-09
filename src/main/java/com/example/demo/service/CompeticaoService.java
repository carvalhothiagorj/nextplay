package com.example.demo.service;

import com.example.demo.model.Competicao;
import com.example.demo.model.Usuario;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CompeticaoService {

    @Autowired
    private CompeticaoRepository competicaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private ClassificacaoRepository classificacaoRepository;

    @Autowired
    private PartidaRepository partidaRepository;

    public Competicao criarCompeticao(Competicao competicao, Long criadorJogadorId) {
        Usuario criadorJogador = usuarioRepository.findById(criadorJogadorId)
                .orElseThrow(() -> new RuntimeException("Jogador criador não encontrado com o ID: " + criadorJogadorId));

        competicao.setCriadorJogador(criadorJogador);
        
        // CORREÇÃO: Inicializa vagasDisponiveis com o valor de maxParticipantes
        if (competicao.getMaxParticipantes() != null) {
            competicao.setVagasDisponiveis(competicao.getMaxParticipantes());
        } else {
            competicao.setVagasDisponiveis(null); // Define como nulo se não houver limite
        }
        
        return competicaoRepository.save(competicao);
    }

    public List<Competicao> findAll() {
     return competicaoRepository.findAll();
    }

    public Optional<Competicao> findById(Long id) {
     return competicaoRepository.findById(id);
    }

    public List<Competicao> findByCriadorJogadorId(Long criadorJogadorId) {
        return competicaoRepository.findByCriadorJogador_Id(criadorJogadorId);
    }
    
    @Transactional
    public void deleteCompetition(Long competicaoId) {
        if (!competicaoRepository.existsById(competicaoId)) {
            throw new RuntimeException("Competição não encontrada com o ID: " + competicaoId);
        }
        inscricaoRepository.deleteAllByCompeticaoId(competicaoId);
        classificacaoRepository.deleteAllByCompeticaoId(competicaoId);
        partidaRepository.deleteAllByCompeticaoId(competicaoId);
        competicaoRepository.deleteById(competicaoId);
    }
    
    @Transactional
    public Competicao updateCompetition(Long id, Competicao competicaoAtualizada) {
        Competicao competicaoExistente = competicaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada com o ID: " + id));

        competicaoExistente.setNomeCompeticao(competicaoAtualizada.getNomeCompeticao());
        competicaoExistente.setModalidade(competicaoAtualizada.getModalidade());
        competicaoExistente.setRequisitos(competicaoAtualizada.getRequisitos());
        competicaoExistente.setDataInicio(competicaoAtualizada.getDataInicio());
        competicaoExistente.setDataFim(competicaoAtualizada.getDataFim());
        competicaoExistente.setCategoria(competicaoAtualizada.getCategoria());

        return competicaoRepository.save(competicaoExistente);
    }
    
    public List<Competicao> searchCompetitionsByName(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return competicaoRepository.findAll(); 
        }
        return competicaoRepository.findByNomeCompeticaoContainingIgnoreCase(nome);
    }

}
