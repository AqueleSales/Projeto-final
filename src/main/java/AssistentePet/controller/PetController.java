package AssistentePet.controller;

import AssistentePet.dao.IPetDAO;
import AssistentePet.model.AnimalDeServico;
import AssistentePet.model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PetController {

    private final IPetDAO petDAO;

    @Autowired
    public PetController(IPetDAO petDAO) {
        this.petDAO = petDAO;
    }

    @PostMapping("/api/pets")
    public ResponseEntity<Pet> salvarPet(@RequestBody Pet pet) {
        try {
            Pet petSalvo = petDAO.salvar(pet);
            return new ResponseEntity<>(petSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar pet comum: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/animais-de-servico")
    public ResponseEntity<AnimalDeServico> salvarAnimalDeServico(@RequestBody AnimalDeServico pet) {
        try {
            AnimalDeServico petSalvo = (AnimalDeServico) petDAO.salvar(pet);
            return new ResponseEntity<>(petSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar Animal de Serviço: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/pets")
    public ResponseEntity<List<Pet>> listarTodosPets() {
        List<Pet> pets = petDAO.listarTodos();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/api/pets/{id}")
    public ResponseEntity<Pet> buscarPetPorId(@PathVariable("id") int id) {
        Pet pet = petDAO.buscarPorId(id);
        if (pet != null) {
            return new ResponseEntity<>(pet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/pets/dono/{idDono}")
    public ResponseEntity<List<Pet>> listarPetsPorDono(@PathVariable("idDono") int idDono) {
        List<Pet> pets = petDAO.listarPetsPorDono(idDono);
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @PutMapping("/api/pets/{id}")
    public ResponseEntity<Pet> atualizarPet(@PathVariable("id") int id, @RequestBody Pet pet) {
        pet.setIdPet(id);
        boolean atualizou = petDAO.atualizar(pet);
        if (atualizou) {
            return new ResponseEntity<>(pet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/api/pets/{id}")
    public ResponseEntity<Void> deletarPet(@PathVariable("id") int id) {
        boolean deletou = petDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/api/pets/promover")
    public ResponseEntity<Void> promoverPet(@RequestBody AnimalDeServico pet) {
        try {
            boolean sucesso = petDAO.promoverParaAnimalDeServico(pet);
            if (sucesso) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            System.err.println("Erro ao promover Pet: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}