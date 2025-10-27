package br.com.assistente.pet.controller;

import dao.IHabilidadeDAO;
import model.Habilidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expõe os endpoints (URLs) para a entidade Habilidade.
 * O aplicativo de celular vai fazer requisições para estas URLs.
 */
@RestController
@RequestMapping("/api/habilidades") // Todos os métodos aqui começarão com /api/habilidades
public class HabilidadeController {

    private final IHabilidadeDAO habilidadeDAO;

    @Autowired
    public HabilidadeController(IHabilidadeDAO habilidadeDAO) {
        this.habilidadeDAO = habilidadeDAO;
    }

    /**
     * Endpoint para SALVAR uma nova habilidade.
     * O app de celular vai chamar: POST /api/habilidades
     *
     * @param habilidade Os dados da habilidade (em JSON).
     * @return A habilidade salva (com o ID).
     */
    @PostMapping
    public ResponseEntity<Habilidade> salvarHabilidade(@RequestBody Habilidade habilidade) {
        try {
            Habilidade salva = habilidadeDAO.salvar(habilidade);
            return new ResponseEntity<>(salva, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar habilidade: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para LISTAR todas as habilidades.
     * O app de celular vai chamar: GET /api/habilidades
     *
     * @return Uma lista de todas as habilidades.
     */
    @GetMapping
    public ResponseEntity<List<Habilidade>> listarHabilidades() {
        List<Habilidade> habilidades = habilidadeDAO.listarTodos();
        return new ResponseEntity<>(habilidades, HttpStatus.OK);
    }

    /**
     * Endpoint para BUSCAR uma habilidade por ID.
     * O app de celular vai chamar: GET /api/habilidades/1
     *
     * @param id O ID da habilidade.
     * @return A habilidade encontrada ou 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Habilidade> buscarHabilidadePorId(@PathVariable int id) {
        Habilidade habilidade = habilidadeDAO.buscarPorId(id);
        if (habilidade != null) {
            return new ResponseEntity<>(habilidade, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para ATUALIZAR uma habilidade.
     * O app de celular vai chamar: PUT /api/habilidades/1
     *
     * @param id O ID da habilidade a atualizar.
     * @param habilidade Os novos dados da habilidade (em JSON).
     * @return A habilidade atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Habilidade> atualizarHabilidade(@PathVariable int id, @RequestBody Habilidade habilidade) {
        habilidade.setIdHabilidade(id);
        boolean atualizou = habilidadeDAO.atualizar(habilidade);
        if (atualizou) {
            return new ResponseEntity<>(habilidade, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para DELETAR uma habilidade.
     * O app de celular vai chamar: DELETE /api/habilidades/1
     *
     * @param id O ID da habilidade a deletar.
     * @return Resposta de sucesso (204 No Content) ou erro.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarHabilidade(@PathVariable int id) {
        boolean deletou = habilidadeDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
