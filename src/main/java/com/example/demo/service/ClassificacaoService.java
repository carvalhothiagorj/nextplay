package com.example.demo.service;

import com.example.demo.model.Classificacao;
import com.example.demo.model.Competicao;
import com.example.demo.model.Inscricao;
import com.example.demo.repository.ClassificacaoRepository;
import com.example.demo.repository.CompeticaoRepository;
import com.example.demo.repository.InscricaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClassificacaoService {

    @Autowired
    private ClassificacaoRepository classificacaoRepository;

    @Autowired
    private CompeticaoRepository competicaoRepository;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Transactional
    public void inicializarClassificacao(Long competicaoId) {
        Competicao competicao = competicaoRepository.findById(competicaoId)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));

        List<Inscricao> inscricoes = inscricaoRepository.findByCompeticao(competicao);

        if (inscricoes.isEmpty()) {
            throw new RuntimeException("Nenhuma inscrição encontrada para esta competição.");
        }

        // Remover classificações existentes para esta competição (se houver)
        classificacaoRepository.deleteAll(classificacaoRepository.findByCompeticaoOrderByPontosDescVitoriasAsc(competicao));

        for (Inscricao inscricao : inscricoes) {
            Classificacao classificacao = new Classificacao();
            classificacao.setCompeticao(competicao);

            if ("solo".equals(competicao.getTipoCompeticao())) {
                classificacao.setParticipanteJogador(inscricao.getJogador());
            } else if ("equipe".equals(competicao.getTipoCompeticao())) {
                classificacao.setParticipanteEquipe(inscricao.getEquipe());
            }

            classificacao.setPosicao(0); // Posição inicial, será calculada depois
            classificacao.setPontos(0);
            classificacao.setVitorias(0);
            classificacao.setDerrotas(0);
            classificacao.setEmpates(0);

            classificacaoRepository.save(classificacao);
        }

        // Após inicializar, podemos calcular as posições iniciais (todos empatados)
        atualizarPosicoes(competicaoId);
    }

    @Transactional
    public void atualizarClassificacao(Long partidaId) {
        
    }

    @Transactional
    public void atualizarPosicoes(Long competicaoId) {
        Competicao competicao = competicaoRepository.findById(competicaoId)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));

        // Ordenar pelo critério definido no repositório (Pontos Desc, Vitórias Asc, etc.)
        List<Classificacao> classificacaoList = classificacaoRepository.findByCompeticaoOrderByPontosDescVitoriasAsc(competicao);

        for (int i = 0; i < classificacaoList.size(); i++) {
            Classificacao classificacao = classificacaoList.get(i);
            classificacao.setPosicao(i + 1);
            classificacaoRepository.save(classificacao);
        }
    }

    public List<Classificacao> getClassificacaoPorCompeticao(Long competicaoId) {
        Competicao competicao = competicaoRepository.findById(competicaoId)
                .orElseThrow(() -> new RuntimeException("Competição não encontrada"));
                
        return classificacaoRepository.findByCompeticaoOrderByPontosDescVitoriasAsc(competicao);
    }
}
