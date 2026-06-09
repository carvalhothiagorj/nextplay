package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Equipe;
import com.example.demo.model.Jogador;
import com.example.demo.model.MembrosEquipe;
import com.example.demo.model.Usuario;
import com.example.demo.repository.EquipeRepository;
import com.example.demo.repository.MembrosEquipeRepository;
import com.example.demo.repository.UsuarioRepository;

import jakarta.transaction.Transactional; 

@Service
public class EquipeService {
	
	@Autowired
	private MembrosEquipeRepository membrosEquipeRepository;

    @Autowired
    private EquipeRepository equipeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    
    public Equipe criarEquipe(Equipe equipe, Long liderId) {
        Usuario lider = usuarioRepository.findById(liderId)
                .orElseThrow(() -> new RuntimeException("Líder inválido ou não encontrado."));

        if (!"JOGADOR".equalsIgnoreCase(lider.getTipoPerfil())) {
            throw new RuntimeException("Apenas JOGADORES podem ser líderes de equipe.");
        }

        if (equipeRepository.findByNomeEquipe(equipe.getNomeEquipe()) != null) {
            throw new RuntimeException("Já existe uma equipe com este nome.");
        }

        equipe.setLider(lider);
       
        equipe.setDataCriacao(new Date()); 
        return equipeRepository.save(equipe);
    }

    public List<Equipe> buscarTodasEquipes() {
        return equipeRepository.findAll();
    }


    public Optional<Equipe> buscarEquipePorId(Long id) {
        return equipeRepository.findById(id);
    }

    public Equipe atualizarEquipe(Long id, Equipe equipeDetalhes) {
        return equipeRepository.findById(id)
                .map(equipeExistente -> {
                   
                    if (equipeDetalhes.getNomeEquipe() != null &&
                        !equipeDetalhes.getNomeEquipe().equals(equipeExistente.getNomeEquipe())) {
                        if (equipeRepository.findByNomeEquipe(equipeDetalhes.getNomeEquipe()) != null) {
                            throw new RuntimeException("Já existe outra equipe com o nome: " + equipeDetalhes.getNomeEquipe());
                        }
                        equipeExistente.setNomeEquipe(equipeDetalhes.getNomeEquipe());
                    }

                    return equipeRepository.save(equipeExistente);
                }).orElseThrow(() -> new RuntimeException("Equipe com ID " + id + " não encontrada para atualização."));
    }

    
    public void excluirEquipe(Long id) {
        if (!equipeRepository.existsById(id)) {
            throw new RuntimeException("Equipe com ID " + id + " não encontrada para exclusão.");
        }
        equipeRepository.deleteById(id);
    }

    public List<Equipe> findByLiderId(Long liderId) {
        return equipeRepository.findByLider_Id(liderId);
    }
    
    public List<Equipe> getEquipesLideradasPor(Long idLider) {
        if (idLider == null || idLider <= 0) {
          
            throw new IllegalArgumentException("ID do líder inválido.");
        }
        
        return equipeRepository.findByLider_Id(idLider);
    }
    
    @Transactional
    public Equipe adicionarUsuarioNaEquipePorNome(Long equipeId, String nomeDoUsuario) {
        
        if (nomeDoUsuario == null || nomeDoUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário fornecido é inválido ou vazio. Por favor, forneça um nome válido.");
        }

       
        Equipe equipe = equipeRepository.findById(equipeId)
                .orElseThrow(() -> new IllegalArgumentException("Equipe com ID " + equipeId + " não encontrada. Verifique o ID da equipe."));

       
        Usuario usuario = usuarioRepository.findByNome(nomeDoUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o nome: " + nomeDoUsuario + ". Certifique-se de que o usuário está cadastrado e o nome está correto."));

       
        if (membrosEquipeRepository.findByEquipeAndUsuario(equipe, usuario).isPresent()) {
             throw new IllegalStateException("O usuário " + nomeDoUsuario + " já é membro da equipe " + equipe.getNomeEquipe() + ".");
        }

       
        MembrosEquipe novoMembro = new MembrosEquipe(equipe, usuario); 
        membrosEquipeRepository.save(novoMembro);

       
        equipe.getMembros().add(novoMembro);

        return equipe;
    }
    
}