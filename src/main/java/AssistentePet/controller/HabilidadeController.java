package AssistentePet.controller;

import AssistentePet.dao.IHabilidadeDAO;
import AssistentePet.model.Habilidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habilidades")
public class HabilidadeController {

    private final IHabilidadeDAO habilidadeDAO;

    @Autowired
    public HabilidadeController(IHabilidadeDAO habilidadeDAO) {
        this.habilidadeDAO = habilidadeDAO;
    }

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

    @GetMapping
    public ResponseEntity<List<Habilidade>> listarHabilidades() {
        List<Habilidade> habilidades = habilidadeDAO.listarTodos();
        return new ResponseEntity<>(habilidades, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Habilidade> buscarHabilidadePorId(@PathVariable("id") int id) {
        Habilidade habilidade = habilidadeDAO.buscarPorId(id);
        if (habilidade != null) {
            return new ResponseEntity<>(habilidade, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Habilidade> atualizarHabilidade(@PathVariable("id") int id, @RequestBody Habilidade habilidade) {
        habilidade.setIdHabilidade(id);
        boolean atualizou = habilidadeDAO.atualizar(habilidade);
        if (atualizou) {
            return new ResponseEntity<>(habilidade, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarHabilidade(@PathVariable("id") int id) {
        boolean deletou = habilidadeDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
