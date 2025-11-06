package AssistentePet.controller;

import AssistentePet.dao.ICredencialServicoDAO;
import AssistentePet.model.CredencialServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credenciais")
public class CredencialServicoController {

    private final ICredencialServicoDAO credencialDAO;

    @Autowired
    public CredencialServicoController(ICredencialServicoDAO credencialDAO) {
        this.credencialDAO = credencialDAO;
    }

    @PostMapping
    public ResponseEntity<CredencialServico> salvarCredencial(@RequestBody CredencialServico credencial) {
        try {
            CredencialServico salva = credencialDAO.salvar(credencial);
            return new ResponseEntity<>(salva, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar credencial: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/animal/{idAnimal}")
    public ResponseEntity<CredencialServico> buscarCredencialPorAnimalId(@PathVariable("idAnimal") int idAnimal) {
        // O DAO.buscarPorAnimalId() já cuida de carregar as habilidades (relação M:N)
        CredencialServico credencial = credencialDAO.buscarPorAnimalId(idAnimal);
        if (credencial != null) {
            return new ResponseEntity<>(credencial, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCredencial(@PathVariable("id") int id) {
        // O DAO.deletar() já cuida da transação (deletar da tabela M:N e da tabela principal)
        boolean deletou = credencialDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CredencialServico> buscarCredencialPorId(@PathVariable("id") int id) {
        CredencialServico credencial = credencialDAO.buscarPorId(id);
        if (credencial != null) {
            return new ResponseEntity<>(credencial, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
