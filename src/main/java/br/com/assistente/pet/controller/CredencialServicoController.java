package br.com.assistente.pet.controller;

import dao.ICredencialServicoDAO;
import model.CredencialServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que expõe os endpoints (URLs) para a entidade CredencialServico.
 * Este é o controlador mais complexo, pois lida com a relação M:N.
 */
@RestController
@RequestMapping("/api/credenciais") // Todos os métodos aqui começarão com /api/credenciais
public class CredencialServicoController {

    private final ICredencialServicoDAO credencialDAO;

    @Autowired
    public CredencialServicoController(ICredencialServicoDAO credencialDAO) {
        this.credencialDAO = credencialDAO;
    }

    /**
     * Endpoint para SALVAR uma nova credencial de serviço.
     * O app de celular vai chamar: POST /api/credenciais
     *
     * @param credencial Os dados da credencial (em JSON), incluindo a lista de habilidades.
     * @return A credencial salva (com o ID).
     */
    @PostMapping
    public ResponseEntity<CredencialServico> salvarCredencial(@RequestBody CredencialServico credencial) {
        try {
            // O DAO.salvar() já cuida de toda a transação (salvar a credencial e a tabela de junção)
            CredencialServico salva = credencialDAO.salvar(credencial);
            return new ResponseEntity<>(salva, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar credencial: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para BUSCAR a credencial de um animal de serviço específico.
     * Esta será uma das funções principais do app (mostrar a credencial do pet).
     * O app de celular vai chamar: GET /api/credenciais/animal/1
     *
     * @param idAnimal O ID do Animal de Serviço.
     * @return A credencial encontrada (com a lista de habilidades) ou 404.
     */
    @GetMapping("/animal/{idAnimal}")
    public ResponseEntity<CredencialServico> buscarCredencialPorAnimalId(@PathVariable int idAnimal) {
        // O DAO.buscarPorAnimalId() já cuida de carregar as habilidades (relação M:N)
        CredencialServico credencial = credencialDAO.buscarPorAnimalId(idAnimal);
        if (credencial != null) {
            return new ResponseEntity<>(credencial, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para DELETAR uma credencial.
     * O app de celular vai chamar: DELETE /api/credenciais/1
     *
     * @param id O ID da credencial a deletar.
     * @return Resposta de sucesso (204 No Content) ou erro.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCredencial(@PathVariable int id) {
        // O DAO.deletar() já cuida da transação (deletar da tabela M:N e da tabela principal)
        boolean deletou = credencialDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para BUSCAR uma credencial pelo seu próprio ID.
     * O app de celular vai chamar: GET /api/credenciais/1
     *
     * @param id O ID da credencial.
     * @return A credencial encontrada ou 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<CredencialServico> buscarCredencialPorId(@PathVariable int id) {
        CredencialServico credencial = credencialDAO.buscarPorId(id);
        if (credencial != null) {
            return new ResponseEntity<>(credencial, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
