package com.example.demo.repository;

import com.example.demo.model.Notificacao;
import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    // Método para encontrar notificações de um usuário específico, ordenadas por data de envio descendente
    List<Notificacao> findByUsuarioOrderByDataEnvioDesc(Usuario usuario);

    // Método para encontrar notificações não lidas de um usuário
    List<Notificacao> findByUsuarioAndLidaFalseOrderByDataEnvioDesc(Usuario usuario);

    // Podemos adicionar outros métodos de busca, se necessário
    // long countByUsuarioAndLidaFalse(Usuario usuario); // Para contar notificações não lidas
}
