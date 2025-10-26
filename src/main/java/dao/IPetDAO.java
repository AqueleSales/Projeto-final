package dao;

import model.Pet;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade Pet (CRUD - Create, Read, Update, Delete).
 */
public interface IPetDAO {

    /**
     * Salva um novo objeto Pet no banco de dados.
     * @param pet O objeto Pet a ser salvo.
     * @return O objeto Pet salvo, possivelmente com o ID gerado.
     */
    Pet salvar(Pet pet);

    /**
     * Atualiza um objeto Pet existente no banco de dados.
     * @param pet O objeto Pet com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    boolean atualizar(Pet pet);

    /**
     * Deleta um Pet do banco de dados com base no seu ID.
     * @param id O ID do Pet a ser deletado.
     * @return true se a deleção foi bem-sucedida, false caso contrário.
     */
    boolean deletar(int id);

    /**
     * Busca um Pet no banco de dados pelo seu ID.
     * @param id O ID do Pet a ser buscado.
     * @return O objeto Pet encontrado, ou null se não for encontrado.
     */
    Pet buscarPorId(int id);

    /**
     * Retorna uma lista com todos os Pets de um dono específico.
     * @param idDono O ID do Dono.
     * @return Uma lista de objetos Pet.
     */
    List<Pet> listarPorDono(int idDono);

    /**
     * Retorna uma lista com todos os Pets cadastrados no banco de dados.
     * @return Uma lista de objetos Pet.
     */
    List<Pet> listarTodos();
}

