package br.com.assistente.pet.controller;

import dao.IClinicaDAO;
import model.Clinica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expõe os endpoints (URLs) para a entidade Clinica.
 * O aplicativo de celular vai fazer requisições para estas URLs.
 */
@RestController
@RequestMapping("/api/clinicas") // Todos os métodos aqui começarão com /api/clinicas
public class ClinicaController {

    private final IClinicaDAO clinicaDAO;

    @Autowired
    public ClinicaController(IClinicaDAO clinicaDAO) {
        this.clinicaDAO = clinicaDAO;
    }

    /**
     * Endpoint para SALVAR uma nova clínica.
     * O app de celular vai chamar: POST /api/clinicas
     *
     * @param clinica Os dados da clínica (em JSON).
     * @return A clínica salva (com o ID).
     */
    @PostMapping
    public ResponseEntity<Clinica> salvarClinica(@RequestBody Clinica clinica) {
        try {
            Clinica clinicaSalva = clinicaDAO.salvar(clinica);
            return new ResponseEntity<>(clinicaSalva, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar clínica: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para LISTAR todas as clínicas.
     * O app de celular vai chamar: GET /api/clinicas
     *
     * @return Uma lista de todas as clínicas.
     */
    @GetMapping
    public ResponseEntity<List<Clinica>> listarClinicas() {
        List<Clinica> clinicas = clinicaDAO.listarTodos();
        return new ResponseEntity<>(clinicas, HttpStatus.OK);
    }

    /**
     * Endpoint para BUSCAR uma clínica por ID.
     * O app de celular vai chamar: GET /api/clinicas/1
     *
     * @param id O ID da clínica.
     * @return A clínica encontrada ou 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Clinica> buscarClinicaPorId(@PathVariable int id) {
        Clinica clinica = clinicaDAO.buscarPorId(id);
        if (clinica != null) {
            return new ResponseEntity<>(clinica, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para ATUALIZAR uma clínica.
     * O app de celular vai chamar: PUT /api/clinicas/1
     *
     * @param id O ID da clínica a atualizar.
     * @param clinica Os novos dados da clínica (em JSON).
     * @return A clínica atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Clinica> atualizarClinica(@PathVariable int id, @RequestBody Clinica clinica) {
        clinica.setIdClinica(id);
        boolean atualizou = clinicaDAO.atualizar(clinica);
        if (atualizou) {
            return new ResponseEntity<>(clinica, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para DELETAR uma clínica.
     * O app de celular vai chamar: DELETE /api/clinicas/1
     *
     * @param id O ID da clínica a deletar.
     * @return Resposta de sucesso (204 No Content) ou erro.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarClinica(@PathVariable int id) {
        boolean deletou = clinicaDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
