package br.com.assistente.pet.controller;

import dao.IPessoaDAO;
import model.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expõe os endpoints (URLs) para a entidade Pessoa.
 * O aplicativo de celular vai fazer requisições para estas URLs.
 */
@RestController // Informa ao Spring que esta classe é um Controlador REST
@RequestMapping("/api/pessoas") // Todos os métodos aqui começarão com /api/pessoas
public class PessoaController {

    private final IPessoaDAO pessoaDAO;

    // O Spring vai injetar o PessoaDAO (que já configuramos como @Repository)
    @Autowired
    public PessoaController(IPessoaDAO pessoaDAO) {
        this.pessoaDAO = pessoaDAO;
    }

    /**
     * Endpoint para SALVAR uma nova pessoa.
     * O app de celular vai chamar: POST /api/pessoas
     *
     * @param pessoa Os dados da pessoa (em JSON) vindos do app celular.
     * @return A pessoa salva (com o ID) ou uma mensagem de erro.
     */
    @PostMapping
    public ResponseEntity<Pessoa> salvarPessoa(@RequestBody Pessoa pessoa) {
        try {
            Pessoa pessoaSalva = pessoaDAO.salvar(pessoa);
            return new ResponseEntity<>(pessoaSalva, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar pessoa: " + e.getMessage());
            // Em um app real, retornaríamos um objeto de erro mais detalhado
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para LISTAR todas as pessoas.
     * O app de celular vai chamar: GET /api/pessoas
     *
     * @return Uma lista de todas as pessoas.
     */
    @GetMapping
    public ResponseEntity<List<Pessoa>> listarPessoas() {
        List<Pessoa> pessoas = pessoaDAO.listarTodos();
        return new ResponseEntity<>(pessoas, HttpStatus.OK);
    }

    /**
     * Endpoint para BUSCAR uma pessoa por ID.
     * O app de celular vai chamar: GET /api/pessoas/1 (por exemplo)
     *
     * @param id O ID da pessoa (vindo da URL).
     * @return A pessoa encontrada ou um erro 404 (Not Found).
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
     * O app de celular vai chamar: PUT /api/pessoas/1 (por exemplo)
     *
     * @param id O ID da pessoa a atualizar.
     * @param pessoa Os novos dados da pessoa (em JSON).
     * @return A pessoa atualizada ou um erro.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable int id, @RequestBody Pessoa pessoa) {
        // Garante que o ID da URL seja o mesmo do objeto
        pessoa.setIdPessoa(id);
        boolean atualizou = pessoaDAO.atualizar(pessoa);
        if (atualizou) {
            return new ResponseEntity<>(pessoa, HttpStatus.OK);
        } else {
            // Poderia ser 404 (se não encontrou) ou 500 (erro ao atualizar)
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para DELETAR uma pessoa.
     * O app de celular vai chamar: DELETE /api/pessoas/1 (por exemplo)
     *
     * @param id O ID da pessoa a deletar.
     * @return Uma resposta de sucesso (204 No Content) ou erro.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable int id) {
        boolean deletou = pessoaDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Sucesso, sem conteúdo
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Não encontrou para deletar
        }
    }
}
