package com.example.demo.service;

import com.example.demo.model.Notificacao;
import com.example.demo.model.Usuario;
import com.example.demo.repository.NotificacaoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Notificacao enviarNotificacao(Long usuarioId, String mensagem, String tipo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setMensagem(mensagem);
        notificacao.setDataEnvio(new Date());
        notificacao.setLida(false); // Inicialmente não lida
        notificacao.setTipo(tipo);

        return notificacaoRepository.save(notificacao);
    }

    public List<Notificacao> getNotificacoesPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return notificacaoRepository.findByUsuarioOrderByDataEnvioDesc(usuario);
    }

    public List<Notificacao> getNotificacoesNaoLidasPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return notificacaoRepository.findByUsuarioAndLidaFalseOrderByDataEnvioDesc(usuario);
    }

    public Notificacao marcarComoLida(Long notificacaoId) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));

        notificacao.setLida(true);

        return notificacaoRepository.save(notificacao);
    }

    
}
