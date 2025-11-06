package AssistentePet.controller;

import AssistentePet.dao.ICertificadoVacinaDAO;
import AssistentePet.model.CertificadoVacina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificados")
public class CertificadoVacinaController {

    private final ICertificadoVacinaDAO certificadoDAO;

    @Autowired
    public CertificadoVacinaController(ICertificadoVacinaDAO certificadoDAO) {
        this.certificadoDAO = certificadoDAO;
    }

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

    @GetMapping("/pet/{idPet}")
    public ResponseEntity<List<CertificadoVacina>> listarCertificadosPorPet(@PathVariable("idPet") int idPet) {
        List<CertificadoVacina> historico = certificadoDAO.listarPorPet(idPet);
        return new ResponseEntity<>(historico, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCertificado(@PathVariable("id") int id) {
        boolean deletou = certificadoDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificadoVacina> buscarCertificadoPorId(@PathVariable("id") int id) {
        CertificadoVacina certificado = certificadoDAO.buscarPorId(id);
        if (certificado != null) {
            return new ResponseEntity<>(certificado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
