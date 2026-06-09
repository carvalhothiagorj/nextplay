package com.example.demo.service;

import com.example.demo.model.Competicao;
import com.example.demo.model.Evento;
import com.example.demo.model.Inscricao;
import com.example.demo.model.Usuario;
import com.example.demo.model.Classificacao;
import com.example.demo.repository.CompeticaoRepository;
import com.example.demo.repository.EventoRepository;
import com.example.demo.repository.InscricaoRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.ClassificacaoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Date;

@Service
public class RelatorioService {

    @Autowired
    private CompeticaoRepository competicaoRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClassificacaoRepository classificacaoRepository;

    public List<Usuario> listarParticipantesPorCompeticao(Long competicaoId) {
        Competicao competicao = competicaoRepository.findById(competicaoId)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));

        List<Inscricao> inscricoes = inscricaoRepository.findByCompeticao(competicao);

        List<Usuario> participantes = new ArrayList<>();
        for (Inscricao inscricao : inscricoes) {
            if ("solo".equals(competicao.getTipoCompeticao())) {
                participantes.add(inscricao.getJogador());
            } else if ("equipe".equals(competicao.getTipoCompeticao())) {
                if (inscricao.getEquipe() != null && inscricao.getEquipe().getMembros() != null) {
                    participantes.addAll(inscricao.getEquipe().getMembros().stream()
                                                    .map(m -> m.getUsuario())
                                                    .filter(java.util.Objects::nonNull)
                                                    .collect(Collectors.toList()));
                    if (inscricao.getEquipe().getLider() != null && !participantes.contains(inscricao.getEquipe().getLider())) {
                        participantes.add(inscricao.getEquipe().getLider());
                    }
                }
            }
        }

        return participantes.stream().distinct().collect(Collectors.toList());
    }

    public long getTotalInscricoesPorEvento(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));

        Date dataInicioEvento = evento.getDataInicio();
        Date dataFimEvento = evento.getDataFim();

        List<Competicao> competicoesNoPeriodoDoEvento = competicaoRepository
                .findByDataInicioLessThanEqualAndDataFimGreaterThanEqual(dataFimEvento, dataInicioEvento);

        long totalInscricoes = 0;
        for (Competicao competicao : competicoesNoPeriodoDoEvento) {
            totalInscricoes += inscricaoRepository.countByCompeticao(competicao);
        }

        return totalInscricoes;
    }

    public Map<String, Object> getResumoDesempenhoJogador(Long jogadorId) {
        Usuario jogador = usuarioRepository.findById(jogadorId)
                .orElseThrow(() -> new RuntimeException("Jogador não encontrado"));

        if (!"jogador".equals(jogador.getTipoPerfil())) {
            throw new RuntimeException("Usuário não é um jogador.");
        }

        List<Classificacao> classificacoesIndividuais = classificacaoRepository.findByParticipanteJogador(jogador);

        int totalCompeticoes = classificacoesIndividuais.size();
        int totalVitorias = 0;
        int totalDerrotas = 0;
        int totalEmpates = 0;
        int melhorPosicao = Integer.MAX_VALUE;
        List<Map<String, Object>> desempenhoPorCompeticao = new ArrayList<>();

        for (Classificacao classificacao : classificacoesIndividuais) {
            totalVitorias += classificacao.getVitorias();
            totalDerrotas += classificacao.getDerrotas();
            totalEmpates += classificacao.getEmpates();
            if (classificacao.getPosicao() != null && classificacao.getPosicao() < melhorPosicao) {
                melhorPosicao = classificacao.getPosicao();
            }

            Map<String, Object> detalheCompeticao = new HashMap<>();
            detalheCompeticao.put("competicaoId", classificacao.getCompeticao().getId());
            detalheCompeticao.put("nomeCompeticao", classificacao.getCompeticao().getNomeCompeticao());
            detalheCompeticao.put("posicao", classificacao.getPosicao());
            detalheCompeticao.put("pontos", classificacao.getPontos());
            detalheCompeticao.put("vitorias", classificacao.getVitorias());
            detalheCompeticao.put("derrotas", classificacao.getDerrotas());
            detalheCompeticao.put("empates", classificacao.getEmpates());

            desempenhoPorCompeticao.add(detalheCompeticao);
        }

        Map<String, Object> resumoGeral = new HashMap<>();
        resumoGeral.put("jogadorId", jogador.getId());
        resumoGeral.put("nomeJogador", jogador.getNome());
        resumoGeral.put("totalCompeticoes", totalCompeticoes);
        resumoGeral.put("totalVitorias", totalVitorias);
        resumoGeral.put("totalDerrotas", totalDerrotas);
        resumoGeral.put("totalEmpates", totalEmpates);
        resumoGeral.put("melhorPosicao", melhorPosicao == Integer.MAX_VALUE ? null : melhorPosicao);
        resumoGeral.put("desempenhoPorCompeticao", desempenhoPorCompeticao);

        return resumoGeral;
    }
}