package AssistentePet.controller;

import AssistentePet.dao.IVacinaDAO;
import AssistentePet.model.Vacina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vacinas")
public class VacinaController {

    private final IVacinaDAO vacinaDAO;

    @Autowired
    public VacinaController(IVacinaDAO vacinaDAO) {
        this.vacinaDAO = vacinaDAO;
    }

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

    @GetMapping
    public ResponseEntity<List<Vacina>> listarVacinas() {
        List<Vacina> vacinas = vacinaDAO.listarTodos();
        return new ResponseEntity<>(vacinas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vacina> buscarVacinaPorId(@PathVariable("id") int id) {
        Vacina vacina = vacinaDAO.buscarPorId(id);
        if (vacina != null) {
            return new ResponseEntity<>(vacina, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vacina> atualizarVacina(@PathVariable("id") int id, @RequestBody Vacina vacina) {
        vacina.setIdVacina(id);
        boolean atualizou = vacinaDAO.atualizar(vacina);
        if (atualizou) {
            return new ResponseEntity<>(vacina, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVacina(@PathVariable("id") int id) {
        boolean deletou = vacinaDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
