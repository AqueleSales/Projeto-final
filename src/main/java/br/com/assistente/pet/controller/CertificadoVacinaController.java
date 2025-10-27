package br.com.assistente.pet.controller;

import dao.ICertificadoVacinaDAO;
import model.CertificadoVacina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expõe os endpoints (URLs) para a entidade CertificadoVacina.
 * O aplicativo de celular vai fazer requisições para estas URLs.
 */
@RestController
@RequestMapping("/api/certificados") // Todos os métodos aqui começarão com /api/certificados
public class CertificadoVacinaController {

    private final ICertificadoVacinaDAO certificadoDAO;

    @Autowired
    public CertificadoVacinaController(ICertificadoVacinaDAO certificadoDAO) {
        this.certificadoDAO = certificadoDAO;
    }

    /**
     * Endpoint para SALVAR um novo certificado de vacina.
     * O app de celular vai chamar: POST /api/certificados
     *
     * @param certificado Os dados do certificado (em JSON).
     * @return O certificado salvo (com o ID).
     */
    @PostMapping
    public ResponseEntity<CertificadoVacina> salvarCertificado(@RequestBody CertificadoVacina certificado) {
        try {
            CertificadoVacina salvo = certificadoDAO.salvar(certificado);
            return new ResponseEntity<>(salvo, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar certificado: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para LISTAR todos os certificados de um pet específico.
     * Esta será uma das funções principais do app.
     * O app de celular vai chamar: GET /api/certificados/pet/1
     *
     * @param idPet O ID do pet.
     * @return A lista de certificados (histórico de vacinas) daquele pet.
     */
    @GetMapping("/pet/{idPet}")
    public ResponseEntity<List<CertificadoVacina>> listarCertificadosPorPet(@PathVariable int idPet) {
        List<CertificadoVacina> historico = certificadoDAO.listarPorPet(idPet);
        return new ResponseEntity<>(historico, HttpStatus.OK);
    }

    /**
     * Endpoint para DELETAR um certificado.
     * O app de celular vai chamar: DELETE /api/certificados/1
     *
     * @param id O ID do certificado a deletar.
     * @return Resposta de sucesso (204 No Content) ou erro.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCertificado(@PathVariable int id) {
        boolean deletou = certificadoDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para BUSCAR um certificado por ID.
     * O app de celular vai chamar: GET /api/certificados/1
     *
     * @param id O ID do certificado.
     * @return O certificado encontrado ou 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<CertificadoVacina> buscarCertificadoPorId(@PathVariable int id) {
        CertificadoVacina certificado = certificadoDAO.buscarPorId(id);
        if (certificado != null) {
            return new ResponseEntity<>(certificado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
