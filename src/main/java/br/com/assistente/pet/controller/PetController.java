package br.com.assistente.pet.controller;

import dao.IPetDAO;
import model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST que expõe os endpoints (URLs) para a entidade Pet.
 * O aplicativo de celular vai fazer requisições para estas URLs.
 */
@RestController
@RequestMapping("/api/pets") // Todos os métodos aqui começarão com /api/pets
public class PetController {

    private final IPetDAO petDAO;

    @Autowired
    public PetController(IPetDAO petDAO) {
        this.petDAO = petDAO;
    }

    /**
     * Endpoint para SALVAR um novo pet.
     * O app de celular vai chamar: POST /api/pets
     *
     * @param pet Os dados do pet (em JSON).
     * @return O pet salvo (com o ID).
     */
    @PostMapping
    public ResponseEntity<Pet> salvarPet(@RequestBody Pet pet) {
        try {
            Pet petSalvo = petDAO.salvar(pet);
            return new ResponseEntity<>(petSalvo, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Erro ao salvar pet: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para LISTAR todos os pets.
     * O app de celular vai chamar: GET /api/pets
     *
     * @return Uma lista de todos os pets.
     */
    @GetMapping
    public ResponseEntity<List<Pet>> listarTodosPets() {
        List<Pet> pets = petDAO.listarTodos();
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    /**
     * Endpoint para BUSCAR um pet por ID.
     * O app de celular vai chamar: GET /api/pets/1
     *
     * @param id O ID do pet.
     * @return O pet encontrado ou 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Pet> buscarPetPorId(@PathVariable int id) {
        Pet pet = petDAO.buscarPorId(id);
        if (pet != null) {
            return new ResponseEntity<>(pet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para LISTAR pets de um dono específico.
     * O app de celular vai chamar: GET /api/pets/dono/1
     *
     * @param idDono O ID do dono.
     * @return A lista de pets daquele dono.
     */
    @GetMapping("/dono/{idDono}")
    public ResponseEntity<List<Pet>> listarPetsPorDono(@PathVariable int idDono) {
        List<Pet> pets = petDAO.listarPorDono(idDono);
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    /**
     * Endpoint para ATUALIZAR um pet.
     * O app de celular vai chamar: PUT /api/pets/1
     *
     * @param id O ID do pet a atualizar.
     * @param pet Os novos dados do pet (em JSON).
     * @return O pet atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Pet> atualizarPet(@PathVariable int id, @RequestBody Pet pet) {
        pet.setIdPet(id);
        boolean atualizou = petDAO.atualizar(pet);
        if (atualizou) {
            // No mundo real, buscaríamos o pet atualizado do banco antes de retornar
            return new ResponseEntity<>(pet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint para DELETAR um pet.
     * O app de celular vai chamar: DELETE /api/pets/1
     *
     * @param id O ID do pet a deletar.
     * @return Resposta de sucesso (204 No Content) ou erro.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPet(@PathVariable int id) {
        boolean deletou = petDAO.deletar(id);
        if (deletou) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
