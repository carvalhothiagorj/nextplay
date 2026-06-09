package com.example.demo.service;

import com.example.demo.model.Jogador;
import com.example.demo.model.Organizador;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * CORRIGIDO: O método agora é explícito para garantir que os dados
     * específicos do Jogador (dataNascimento, telefone) sejam salvos.
     * Ele verifica o tipo de perfil e cria a entidade correta antes de salvar.
     */
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            throw new RuntimeException("Este e-mail já está cadastrado.");
        }

        // Verifica o tipo de perfil e cria a instância correta
        if ("jogador".equalsIgnoreCase(usuario.getTipoPerfil())) {
            Jogador jogador = (Jogador) usuario; // O objeto já deve ser um Jogador
            jogador.setDataCadastro(new Date());
            return usuarioRepository.save(jogador);
        } else if ("organizador".equalsIgnoreCase(usuario.getTipoPerfil())) {
            Organizador organizador = (Organizador) usuario;
            organizador.setDataCadastro(new Date());
            return usuarioRepository.save(organizador);
        } else {
            throw new IllegalArgumentException("Tipo de perfil inválido: " + usuario.getTipoPerfil());
        }
    }

    public Usuario loginUsuario(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario != null) {
            if (senha.equals(usuario.getSenha())) {
                return usuario;
            }
        }
        return null;
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    /**
     * CORRIGIDO: Lógica de atualização mantida, pois já é segura e explícita
     * para lidar com os campos de Jogador.
     */
    @Transactional
    public Usuario atualizarPerfilUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));

        // Atualiza campos comuns
        if (usuarioAtualizado.getNome() != null) {
            usuarioExistente.setNome(usuarioAtualizado.getNome());
        }
        if (usuarioAtualizado.getFotoPerfil() != null) {
            usuarioExistente.setFotoPerfil(usuarioAtualizado.getFotoPerfil());
        }
        if (usuarioAtualizado.getBio() != null) {
            usuarioExistente.setBio(usuarioAtualizado.getBio());
        }

        // Atualiza campos específicos de Jogador de forma segura
        if (usuarioExistente instanceof Jogador && usuarioAtualizado instanceof Jogador) {
            Jogador jogadorExistente = (Jogador) usuarioExistente;
            Jogador jogadorAtualizado = (Jogador) usuarioAtualizado;

            if (jogadorAtualizado.getDataNascimento() != null) {
                jogadorExistente.setDataNascimento(jogadorAtualizado.getDataNascimento());
            }
            if (jogadorAtualizado.getTelefone() != null) {
                jogadorExistente.setTelefone(jogadorAtualizado.getTelefone());
            }
        }

        return usuarioRepository.save(usuarioExistente);
    }
}
