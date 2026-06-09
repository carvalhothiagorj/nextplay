package com.example.demo.controller;

import com.example.demo.model.Classificacao;
import com.example.demo.service.ClassificacaoService;
import com.example.demo.model.Competicao;
import com.example.demo.service.CompeticaoService;
import com.example.demo.model.Equipe;
import com.example.demo.service.EquipeService;
import com.example.demo.model.Evento;
import com.example.demo.service.EventoService;
import com.example.demo.model.Inscricao;
import com.example.demo.service.InscricaoService;
import com.example.demo.model.Notificacao;
import com.example.demo.service.NotificacaoService;
import com.example.demo.model.Partida;
import com.example.demo.service.PartidaService;
import com.example.demo.model.ResultadosPartida;
import com.example.demo.service.ResultadosPartidaService;
import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import com.example.demo.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class ControllerGeral {

    @Autowired
    private ClassificacaoService classificacaoService;

    @Autowired
    private CompeticaoService competicaoService;

    @Autowired
    private EquipeService equipeService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private InscricaoService inscricaoService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private PartidaService partidaService;

    @Autowired
    private ResultadosPartidaService resultadosPartidaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RelatorioService relatorioService;

    @DeleteMapping("/competicoes/{id}")
    public ResponseEntity<?> deleteCompetition(@PathVariable Long id) {
        try {
            competicaoService.deleteCompetition(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Sucesso, sem conteúdo para retornar
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/competicoes/criadas-por-jogador/{jogadorId}")
    public ResponseEntity<?> getCompetitionsByCreator(@PathVariable Long jogadorId) {
        try {
            List<Competicao> competicoes = competicaoService.findByCriadorJogadorId(jogadorId);
            return new ResponseEntity<>(competicoes, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/classificacoes/inicializar/{competicaoId}")
    public ResponseEntity<?> inicializarClassificacao(@PathVariable Long competicaoId) {
        try {
            classificacaoService.inicializarClassificacao(competicaoId);
            return new ResponseEntity<>("Classificação inicializada com sucesso", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/classificacoes/competicao/{competicaoId}")
    public ResponseEntity<?> getClassificacaoPorCompeticao(@PathVariable Long competicaoId) {
        try {
            List<Classificacao> classificacao = classificacaoService.getClassificacaoPorCompeticao(competicaoId);
            return new ResponseEntity<>(classificacao, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/competicoes/criar/{criadorJogadorId}")
    public ResponseEntity<?> criarCompeticao(@RequestBody Competicao competicao, @PathVariable Long criadorJogadorId) {
        try {
            Competicao novaCompeticao = competicaoService.criarCompeticao(competicao, criadorJogadorId);
            return new ResponseEntity<>(novaCompeticao, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/competicoes")
    public ResponseEntity<List<Competicao>> getAllOrSearchCompetitions(
            @RequestParam(value = "nome", required = false) String nome) {
        try {
            List<Competicao> competicoes;
            if (nome != null && !nome.isEmpty()) {
                competicoes = competicaoService.searchCompetitionsByName(nome);
            } else {
                competicoes = competicaoService.findAll();
            }
            return new ResponseEntity<>(competicoes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/competicoes/{id}")
    public ResponseEntity<Competicao> getCompeticaoById(@PathVariable Long id) {
        try {
            return competicaoService.findById(id)
                    .map(competicao -> new ResponseEntity<>(competicao, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/equipes/criar/{liderId}") // Adicionado "/equipes" ao path
    public ResponseEntity<?> criarEquipe(@RequestBody Equipe equipe, @PathVariable Long liderId) {
        try {
            Equipe novaEquipe = equipeService.criarEquipe(equipe, liderId);
            return new ResponseEntity<>(novaEquipe, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            if ("Líder inválido ou não encontrado".equals(errorMessage) || 
                "Já existe uma equipe com este nome".equals(errorMessage) || 
                "Apenas JOGADORES podem ser líderes de equipe.".equals(errorMessage)) {
                return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
            } else {
                System.err.println("Erro inesperado ao criar equipe: " + errorMessage);
                return new ResponseEntity<>("Ocorreu um erro ao processar sua solicitação.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.err.println("Ocorreu um erro interno ao criar equipe: " + e.getMessage());
            return new ResponseEntity<>("Ocorreu um erro interno no servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/eventos/criar/{organizadorId}")
    public ResponseEntity<?> criarEvento(@RequestBody Evento evento, @PathVariable Long organizadorId) {
        try {
            Evento novoEvento = eventoService.criarEvento(evento, organizadorId);
            return new ResponseEntity<>(novoEvento, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/eventos")
    public ResponseEntity<List<Evento>> getAllEvents() {
        try {
            List<Evento> eventos = eventoService.findAll();
            return new ResponseEntity<>(eventos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/eventos/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id, @RequestBody Evento eventoAtualizado) {
        try {
            Evento evento = eventoService.updateEvent(id, eventoAtualizado);
            return new ResponseEntity<>(evento, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/eventos/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            eventoService.deleteById(id);
            return new ResponseEntity<>("Evento excluído com sucesso", HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/inscricoes/individual/{competicaoId}/{jogadorId}")
    public ResponseEntity<?> realizarInscricaoIndividual(
            @PathVariable Long competicaoId,
            @PathVariable Long jogadorId) {
        try {
            Inscricao novaInscricao = inscricaoService.realizarInscricaoIndividual(competicaoId, jogadorId);
            return new ResponseEntity<>(novaInscricao, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/inscricoes/equipe/{competicaoId}/{equipeId}")
    public ResponseEntity<?> realizarInscricaoPorEquipe(
            @PathVariable Long competicaoId,
            @PathVariable Long equipeId) {
        try {
            Inscricao novaInscricao = inscricaoService.realizarInscricaoPorEquipe(competicaoId, equipeId);
            return new ResponseEntity<>(novaInscricao, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/notificacoes/enviar/{usuarioId}")
    public ResponseEntity<?> enviarNotificacao(
            @PathVariable Long usuarioId,
            @RequestParam String mensagem,
            @RequestParam(required = false) String tipo) {
        try {
            Notificacao novaNotificacao = notificacaoService.enviarNotificacao(usuarioId, mensagem, tipo);
            return new ResponseEntity<>(novaNotificacao, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/notificacoes/usuario/{usuarioId}")
    public ResponseEntity<?> getNotificacoesPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<Notificacao> notificacoes = notificacaoService.getNotificacoesPorUsuario(usuarioId);
            return new ResponseEntity<>(notificacoes, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/notificacoes/usuario/{usuarioId}/nao-lidas")
    public ResponseEntity<?> getNotificacoesNaoLidasPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<Notificacao> notificacoes = notificacaoService.getNotificacoesNaoLidasPorUsuario(usuarioId);
            return new ResponseEntity<>(notificacoes, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/notificacoes/marcarComoLida/{notificacaoId}")
    public ResponseEntity<?> marcarComoLida(@PathVariable Long notificacaoId) {
        try {
            Notificacao notificacaoAtualizada = notificacaoService.marcarComoLida(notificacaoId);
            return new ResponseEntity<>(notificacaoAtualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/partidas/criar/{competicaoId}")
    public ResponseEntity<?> criarPartida(@RequestBody Partida partida, @PathVariable Long competicaoId) {
        try {
            Partida novaPartida = partidaService.criarPartida(partida, competicaoId);
            return new ResponseEntity<>(novaPartida, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/partidas/competicao/{competicaoId}")
    public ResponseEntity<?> listarPartidasPorCompeticao(@PathVariable Long competicaoId) {
        try {
            List<Partida> partidas = partidaService.listarPartidasPorCompeticao(competicaoId);
            return new ResponseEntity<>(partidas, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/partidas/atualizarResultado/{partidaId}")
    public ResponseEntity<?> atualizarResultadoPartida(@PathVariable Long partidaId, @RequestBody String resultado) {
        try {
            Partida partidaAtualizada = partidaService.atualizarResultadoPartida(partidaId, resultado);
            return new ResponseEntity<>(partidaAtualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resultados-partida/salvar/{partidaId}")
    public ResponseEntity<?> salvarDetalhesResultado(@PathVariable Long partidaId, @RequestBody ResultadosPartida detalhesResultado) {
        try {
            ResultadosPartida novosDetalhes = resultadosPartidaService.salvarDetalhesResultado(partidaId, detalhesResultado);
            return new ResponseEntity<>(novosDetalhes, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/resultados-partida/partida/{partidaId}")
    public ResponseEntity<Object> getDetalhesResultadoPorPartida(@PathVariable Long partidaId) {
        try {
            return resultadosPartidaService.getDetalhesResultadoPorPartida(partidaId)
                    .map(detalhes -> new ResponseEntity<Object>(detalhes, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<Object>("Detalhes de resultado não encontrados para esta partida", HttpStatus.NOT_FOUND));
        } catch (RuntimeException e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/usuarios/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.registrarUsuario(usuario);
            return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * CORRIGIDO: O método agora aceita um Map<String, String> para evitar problemas de desserialização.
     * Ele extrai o e-mail e a senha do mapa e chama o serviço de login.
     * Isso permite o login apenas com e-mail e senha, como solicitado.
     */
    @PostMapping("/usuarios/login")
    public ResponseEntity<?> loginUsuario(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String senha = credentials.get("senha");
        
        Usuario usuario = usuarioService.loginUsuario(email, senha);
        
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Email ou senha inválidos", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/usuarios/email/{email}")
    public ResponseEntity<?> getUsuarioByEmail(@PathVariable String email) {
        Usuario usuario = usuarioService.findByEmail(email);
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        }
        return new ResponseEntity<>("Usuário não encontrado", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Object> getUsuarioById(@PathVariable Long id) {
        try {
            return usuarioService.findById(id)
                    .map(usuario -> new ResponseEntity<Object>(usuario, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<Object>("Usuário não encontrado", HttpStatus.NOT_FOUND));
        } catch (RuntimeException e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> atualizarPerfilUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        try {
            Usuario usuario = usuarioService.atualizarPerfilUsuario(id, usuarioAtualizado);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
     @GetMapping("/relatorios/competicao/{competicaoId}/participantes")
    public ResponseEntity<?> listarParticipantesPorCompeticao(@PathVariable Long competicaoId) {
        try {
            List<Usuario> participantes = relatorioService.listarParticipantesPorCompeticao(competicaoId);
            return new ResponseEntity<>(participantes, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/relatorios/evento/{eventoId}/total-inscricoes")
    public ResponseEntity<?> getTotalInscricoesPorEvento(@PathVariable Long eventoId) {
        try {
            long totalInscricoes = relatorioService.getTotalInscricoesPorEvento(eventoId);
            return new ResponseEntity<>(totalInscricoes, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/relatorios/jogador/{jogadorId}/desempenho")
    public ResponseEntity<?> getResumoDesempenhoJogador(@PathVariable Long jogadorId) {
        try {
            Map<String, Object> resumoDesempenho = relatorioService.getResumoDesempenhoJogador(jogadorId);
            return new ResponseEntity<>(resumoDesempenho, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping
    public List<Evento> getAllOrSearchEventos(@RequestParam(required = false) String nome) {
        if (nome != null && !nome.isEmpty()) {
            return eventoService.findByNome(nome); 
        } else {
            return eventoService.findAll(); 
        }
    }
    
    @GetMapping("/status/{competicaoId}/{jogadorId}")
    public ResponseEntity<Map<String, Boolean>> verificarStatusInscricao(
            @PathVariable Long competicaoId,
            @PathVariable Long jogadorId) {

        boolean isSubscribed = inscricaoService.isJogadorInscrito(competicaoId, jogadorId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isSubscribed", isSubscribed);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/equipes") 
    public ResponseEntity<List<Equipe>> buscarTodasEquipes() {
        List<Equipe> equipes = equipeService.buscarTodasEquipes();
        return new ResponseEntity<>(equipes, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Equipe> buscarEquipePorId(@PathVariable Long id) {
        return equipeService.buscarEquipePorId(id)
                .map(equipe -> new ResponseEntity<>(equipe, HttpStatus.OK)) // Retorna 200 OK se encontrada
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Retorna 404 Not Found se não encontrada
    }

    
    @PutMapping("/equipes/{id}")
    public ResponseEntity<Equipe> atualizarEquipe(@PathVariable Long id, @RequestBody Equipe equipe) {
        try {
            Equipe equipeAtualizada = equipeService.atualizarEquipe(id, equipe);
            return new ResponseEntity<>(equipeAtualizada, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
        
        @DeleteMapping("/equipes/{id}")
        public ResponseEntity<Void> excluirEquipe(@PathVariable Long id) {
            try {
                equipeService.excluirEquipe(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
            } catch (NoSuchElementException e) {
               
                System.err.println("Erro ao excluir equipe: " + e.getMessage()); 
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
            } catch (IllegalStateException e) {
               
                System.err.println("Erro de integridade de dados ao excluir equipe: " + e.getMessage()); 
                return new ResponseEntity<>(HttpStatus.CONFLICT); 
            } catch (Exception e) {
                
                System.err.println("Erro interno ao excluir equipe: " + e.getMessage()); // Log
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    
        @GetMapping("/lideradas/{idLider}")
        public ResponseEntity<List<Equipe>> getEquipesLideradasPor(@PathVariable Long idLider) {
            try {
                List<Equipe> equipes = equipeService.getEquipesLideradasPor(idLider);
                return ResponseEntity.ok(equipes); 
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); 
            } catch (Exception e) {
               
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); 
            }
        }
        
        @PostMapping("/{equipeId}/adicionar-membro-por-nome") 
        public ResponseEntity<?> adicionarMembroPorNome(@PathVariable Long equipeId, @RequestBody String nomeDoUsuario) {
            try {
                Equipe equipeAtualizada = equipeService.adicionarUsuarioNaEquipePorNome(equipeId, nomeDoUsuario);
                return ResponseEntity.ok(equipeAtualizada);
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            } catch (IllegalStateException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno: " + e.getMessage());
            }
        }

        }
    

