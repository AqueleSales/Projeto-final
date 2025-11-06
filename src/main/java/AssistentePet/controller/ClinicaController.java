package AssistentePet.controller;

import AssistentePet.dao.IClinicaDAO;
import AssistentePet.model.Clinica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clinicas")
public class ClinicaController {

    private final IClinicaDAO clinicaDAO;

    @Autowired
    public ClinicaController(IClinicaDAO clinicaDAO) {
        this.clinicaDAO = clinicaDAO;
    }

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

    @GetMapping
    public ResponseEntity<List<Clinica>> listarClinicas() {
        List<Clinica> clinicas = clinicaDAO.listarTodos();
        return new ResponseEntity<>(clinicas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clinica> buscarClinicaPorId(@PathVariable("id") int id) {
        Clinica clinica = clinicaDAO.buscarPorId(id);
        if (clinica != null) {
            return new ResponseEntity<>(clinica, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clinica> atualizarClinica(@PathVariable("id") int id, @RequestBody Clinica clinica) {
        clinica.setIdClinica(id);
        boolean atualizou = clinicaDAO.atualizar(clinica);
        if (atualizou) {
            return new ResponseEntity<>(clinica, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarClinica(@PathVariable("id") int id) {
        boolean deletou = clinicaDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
