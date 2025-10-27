package br.com.assistente.pet.controller;

import dao.ITreinadorDAO;
import model.Treinador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expõe os endpoints (URLs) para a entidade Treinador.
 * O aplicativo de celular vai fazer requisições para estas URLs.
 */
@RestController
@RequestMapping("/api/treinadores") // Todos os métodos aqui começarão com /api/treinadores
public class TreinadorController {

    private final ITreinadorDAO treinadorDAO;

    @Autowired
    public TreinadorController(ITreinadorDAO treinadorDAO) {
        this.treinadorDAO = treinadorDAO;
    }

    /**
     * Endpoint para SALVAR um novo treinador.
     * O app de celular vai chamar: POST /api/treinadores
     *
     * @param treinador Os dados do treinador (em JSON).
     * @return O treinador salvo (com o ID).
     */
    @PostMapping
    public ResponseEntity<Treinador> salvarTreinador(@RequestBody Treinador treinador) {
        try {
            Treinador salvo = treinadorDAO.salvar(treinador);
            return new ResponseEntity<>(salvo, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar treinador: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para LISTAR todos os treinadores.
     * O app de celular vai chamar: GET /api/treinadores
     *
     * @return Uma lista de todos os treinadores.
     */
    @GetMapping
    public ResponseEntity<List<Treinador>> listarTreinadores() {
        List<Treinador> treinadores = treinadorDAO.listarTodos();
        return new ResponseEntity<>(treinadores, HttpStatus.OK);
    }

    /**
     * Endpoint para BUSCAR um treinador por ID.
     * O app de celular vai chamar: GET /api/treinadores/1
     *
     * @param id O ID do treinador.
     * @return O treinador encontrado ou 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Treinador> buscarTreinadorPorId(@PathVariable int id) {
        Treinador treinador = treinadorDAO.buscarPorId(id);
        if (treinador != null) {
            return new ResponseEntity<>(treinador, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para ATUALIZAR um treinador.
     * O app de celular vai chamar: PUT /api/treinadores/1
     *
     * @param id O ID do treinador a atualizar.
     * @param treinador Os novos dados do treinador (em JSON).
     * @return O treinador atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Treinador> atualizarTreinador(@PathVariable int id, @RequestBody Treinador treinador) {
        treinador.setIdTreinador(id);
        boolean atualizou = treinadorDAO.atualizar(treinador);
        if (atualizou) {
            return new ResponseEntity<>(treinador, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para DELETAR um treinador.
     * O app de celular vai chamar: DELETE /api/treinadores/1
     *
     * @param id O ID do treinador a deletar.
     * @return Resposta de sucesso (204 No Content) ou erro.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTreinador(@PathVariable int id) {
        boolean deletou = treinadorDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
