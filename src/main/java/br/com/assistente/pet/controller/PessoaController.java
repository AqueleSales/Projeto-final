package br.com.assistente.pet.controller;

import dao.IPessoaDAO;
import model.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// --- NOVO IMPORT ---
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expõe os endpoints (URLs) para a entidade Pessoa.
 * ATUALIZADO: Agora injeta PasswordEncoder e usa o método de login seguro.
 */
@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    private final IPessoaDAO pessoaDAO;

    // --- NOVO CAMPO ---
    private final PasswordEncoder passwordEncoder;

    // --- CONSTRUTOR ATUALIZADO ---
    // O Spring vai injetar o PessoaDAO e o PasswordEncoder (do SecurityConfig)
    @Autowired
    public PessoaController(IPessoaDAO pessoaDAO, PasswordEncoder passwordEncoder) {
        this.pessoaDAO = pessoaDAO;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Endpoint para SALVAR uma nova pessoa.
     * NOTA: Este é um endpoint genérico. A CRIAÇÃO de Donos (com criptografia)
     * está sendo feita no 'DonoController' para manter a lógica separada.
     */
    @PostMapping
    public ResponseEntity<Pessoa> salvarPessoa(@RequestBody Pessoa pessoa) {
        try {
            // Este método salvará a pessoa SEM criptografar a senha,
            // pois não foi projetado para isso. O DonoController cuida do registro.
            Pessoa pessoaSalva = pessoaDAO.salvar(pessoa);
            return new ResponseEntity<>(pessoaSalva, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar pessoa: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para LISTAR todas as pessoas.
     */
    @GetMapping
    public ResponseEntity<List<Pessoa>> listarPessoas() {
        List<Pessoa> pessoas = pessoaDAO.listarTodos();
        return new ResponseEntity<>(pessoas, HttpStatus.OK);
    }

    /**
     * Endpoint para BUSCAR uma pessoa por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> buscarPessoaPorId(@PathVariable int id) {
        Pessoa pessoa = pessoaDAO.buscarPorId(id);
        if (pessoa != null) {
            return new ResponseEntity<>(pessoa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para ATUALIZAR uma pessoa.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable int id, @RequestBody Pessoa pessoa) {
        pessoa.setIdPessoa(id);
        boolean atualizou = pessoaDAO.atualizar(pessoa);
        if (atualizou) {
            return new ResponseEntity<>(pessoa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para DELETAR uma pessoa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable int id) {
        boolean deletou = pessoaDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //
    // ----------------------------------------
    // MÉTODO DE LOGIN ATUALIZADO (COM SENHA REAL)
    // ----------------------------------------
    //
    /**
     * Endpoint para LOGIN.
     * O app de celular vai chamar: POST /api/pessoas/login
     *
     * @param loginData Os dados de login (JSON) vindos do app.
     * @return A Pessoa (com o ID) se o login for sucesso, ou erro 401/404.
     */
    @PostMapping("/login")
    public ResponseEntity<Pessoa> login(@RequestBody Pessoa loginData) {
        // 1. Busca o usuário pelo e-mail
        // (O PessoaDAO.buscarPorEmail foi atualizado para trazer a senha)
        Pessoa pessoaNoBanco = pessoaDAO.buscarPorEmail(loginData.getEmail());

        if (pessoaNoBanco != null) {
            // 2. Compara a senha do app com a senha criptografada do banco

            String senhaDoApp = loginData.getSenha(); // Senha pura (ex: "123456")
            String senhaDoBanco = pessoaNoBanco.getSenha(); // Senha hash (ex: "$2a$10$...")

            if (passwordEncoder.matches(senhaDoApp, senhaDoBanco)) {
                // Sucesso! A senha bate.
                return new ResponseEntity<>(pessoaNoBanco, HttpStatus.OK);
            } else {
                // Senha errada
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 401 Não Autorizado
            }
        } else {
            // E-mail não encontrado
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Não Encontrado
        }
    }

} // <-- Fim da classe