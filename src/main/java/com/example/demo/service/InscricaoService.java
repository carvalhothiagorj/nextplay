package com.example.demo.service;

import com.example.demo.model.Competicao;
import com.example.demo.model.Equipe;
import com.example.demo.model.Inscricao;
import com.example.demo.model.MembrosEquipe;
import com.example.demo.model.Usuario;
import com.example.demo.repository.CompeticaoRepository;
import com.example.demo.repository.EquipeRepository;
import com.example.demo.repository.InscricaoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class InscricaoService {

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private CompeticaoRepository competicaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private NotificacaoService notificacaoService; // INJETANDO O SERVIÇO DE NOTIFICAÇÃO

    @Transactional
    public Inscricao realizarInscricaoIndividual(Long competicaoId, Long jogadorId) {
        Competicao competicao = competicaoRepository.findById(competicaoId)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));

        if (!"solo".equalsIgnoreCase(competicao.getTipoCompeticao())) {
            throw new RuntimeException("Esta competição não permite inscrição individual.");
        }

        Usuario jogador = usuarioRepository.findById(jogadorId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        if (inscricaoRepository.findByCompeticaoAndJogador(competicao, jogador).isPresent()) {
            throw new RuntimeException("Você já está inscrito nesta competição.");
        }

        if (competicao.getVagasDisponiveis() != null && competicao.getVagasDisponiveis() <= 0) {
            throw new RuntimeException("Não há vagas disponíveis.");
        }

        if (competicao.getVagasDisponiveis() != null) {
            competicao.setVagasDisponiveis(competicao.getVagasDisponiveis() - 1);
            competicaoRepository.save(competicao);
        }

        Inscricao inscricao = new Inscricao();
        inscricao.setCompeticao(competicao);
        inscricao.setJogador(jogador);
        inscricao.setDataInscricao(new Date());
        inscricao.setStatus("Confirmada");

        Inscricao novaInscricao = inscricaoRepository.save(inscricao);

        // ENVIAR NOTIFICAÇÃO DE INSCRIÇÃO INDIVIDUAL
        String mensagem = String.format("Você foi inscrito(a) com sucesso na competição '%s' (%s).",
                competicao.getNomeCompeticao(), competicao.getModalidade());
        notificacaoService.enviarNotificacao(jogadorId, mensagem, "Inscricao");

        return novaInscricao;
    }

    @Transactional
    public Inscricao realizarInscricaoPorEquipe(Long competicaoId, Long equipeId, Long liderId) {
        Competicao competicao = competicaoRepository.findById(competicaoId)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));

        if (!"equipe".equalsIgnoreCase(competicao.getTipoCompeticao())) {
            throw new RuntimeException("Esta competição não permite inscrição por equipe.");
        }

        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new RuntimeException("Equipe não encontrada"));

        if (!equipe.getLider().getId().equals(liderId)) {
            throw new RuntimeException("Apenas o líder da equipe pode realizar a inscrição.");
        }

        if (inscricaoRepository.findByCompeticaoAndEquipe(competicao, equipe).isPresent()) {
            throw new RuntimeException("Esta equipe já está inscrita na competição.");
        }

        if (competicao.getVagasDisponiveis() != null && competicao.getVagasDisponiveis() <= 0) {
            throw new RuntimeException("Não há vagas disponíveis.");
        }

        if (competicao.getVagasDisponiveis() != null) {
            competicao.setVagasDisponiveis(competicao.getVagasDisponiveis() - 1);
            competicaoRepository.save(competicao);
        }

        Inscricao inscricao = new Inscricao();
        inscricao.setCompeticao(competicao);
        inscricao.setEquipe(equipe);
        inscricao.setDataInscricao(new Date());
        inscricao.setStatus("Confirmada");

        Inscricao novaInscricao = inscricaoRepository.save(inscricao);

        String mensagem = String.format("Sua equipe '%s' foi inscrita com sucesso na competição '%s' (%s).",
                equipe.getNomeEquipe(), competicao.getNomeCompeticao(), competicao.getModalidade());
        notificacaoService.enviarNotificacao(liderId, mensagem, "InscricaoEquipe");

        // Loop corrigido para usar membro.getUsuario()
        for (MembrosEquipe membro : equipe.getMembros()) {
            // Verifica se o objeto Usuario (membro.getUsuario()) não é nulo
            if (membro.getUsuario() != null) { 
                // Acessa o ID do Usuario através de membro.getUsuario().getId()
                if (!membro.getUsuario().getId().equals(liderId)) {
                    String msgMembro = String.format("Sua equipe '%s' foi inscrita na competição '%s'.", equipe.getNomeEquipe(), competicao.getNomeCompeticao());
                    // Envia notificação para o ID do Usuario
                    notificacaoService.enviarNotificacao(membro.getUsuario().getId(), msgMembro, "InscricaoEquipe");
                }
            } else {
                // Log opcional para identificar membros sem um usuário associado
                System.err.println("Atenção: Membro da equipe sem usuário (Usuario) associado encontrado para a equipe " + equipe.getNomeEquipe());
            }
        }

        return novaInscricao;
    }
    
    public boolean verificarInscricao(Long competicaoId, Long jogadorId) {
        return inscricaoRepository.existsByCompeticaoIdAndJogadorId(competicaoId, jogadorId);
    }

    public Inscricao realizarInscricaoPorEquipe(Long competicaoId, Long equipeId) {
        // Este método ainda é um stub e retorna null, conforme o seu código original.
        return null;
    }
    
    public boolean isJogadorInscrito(Long competicaoId, Long jogadorId) {
        // Chama o método do repositório para verificar a existência
        return inscricaoRepository.existsByCompeticaoIdAndJogadorId(competicaoId, jogadorId);
    }
}