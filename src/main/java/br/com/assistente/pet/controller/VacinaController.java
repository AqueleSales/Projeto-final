package br.com.assistente.pet.controller;

import dao.IVacinaDAO;
import model.Vacina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expõe os endpoints (URLs) para a entidade Vacina.
 * O aplicativo de celular vai fazer requisições para estas URLs.
 */
@RestController
@RequestMapping("/api/vacinas") // Todos os métodos aqui começarão com /api/vacinas
public class VacinaController {

    private final IVacinaDAO vacinaDAO;

    @Autowired
    public VacinaController(IVacinaDAO vacinaDAO) {
        this.vacinaDAO = vacinaDAO;
    }

    /**
     * Endpoint para SALVAR uma nova vacina.
     * O app de celular vai chamar: POST /api/vacinas
     *
     * @param vacina Os dados da vacina (em JSON).
     * @return A vacina salva (com o ID).
     */
    @PostMapping
    public ResponseEntity<Vacina> salvarVacina(@RequestBody Vacina vacina) {
        try {
            Vacina vacinaSalva = vacinaDAO.salvar(vacina);
            return new ResponseEntity<>(vacinaSalva, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar vacina: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para LISTAR todas as vacinas.
     * O app de celular vai chamar: GET /api/vacinas
     *
     * @return Uma lista de todas as vacinas.
     */
    @GetMapping
    public ResponseEntity<List<Vacina>> listarVacinas() {
        List<Vacina> vacinas = vacinaDAO.listarTodos();
        return new ResponseEntity<>(vacinas, HttpStatus.OK);
    }

    /**
     * Endpoint para BUSCAR uma vacina por ID.
     * O app de celular vai chamar: GET /api/vacinas/1
     *
     * @param id O ID da vacina.
     * @return A vacina encontrada ou 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vacina> buscarVacinaPorId(@PathVariable int id) {
        Vacina vacina = vacinaDAO.buscarPorId(id);
        if (vacina != null) {
            return new ResponseEntity<>(vacina, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para ATUALIZAR uma vacina.
     * O app de celular vai chamar: PUT /api/vacinas/1
     *
     * @param id O ID da vacina a atualizar.
     * @param vacina Os novos dados da vacina (em JSON).
     * @return A vacina atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Vacina> atualizarVacina(@PathVariable int id, @RequestBody Vacina vacina) {
        vacina.setIdVacina(id);
        boolean atualizou = vacinaDAO.atualizar(vacina);
        if (atualizou) {
            return new ResponseEntity<>(vacina, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para DELETAR uma vacina.
     * O app de celular vai chamar: DELETE /api/vacinas/1
     *
     * @param id O ID da vacina a deletar.
     * @return Resposta de sucesso (204 No Content) ou erro.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVacina(@PathVariable int id) {
        boolean deletou = vacinaDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
