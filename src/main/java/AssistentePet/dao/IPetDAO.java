package AssistentePet.dao;

import AssistentePet.model.AnimalDeServico; // <-- Import necessário
import AssistentePet.model.Pet;
import java.util.List;

public interface IPetDAO {

    Pet salvar(Pet pet);

    boolean atualizar(Pet pet);

    boolean deletar(int id);

    Pet buscarPorId(int id);

    List<Pet> listarTodos();

    List<Pet> listarPetsPorDono(int idDono);

    boolean promoverParaAnimalDeServico(AnimalDeServico dadosAnimalServico);

    void garantirPromocao(int idPet);

    boolean demoverDeAnimalDeServico(int idPet);

}