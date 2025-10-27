package dao;

import model.Treinador;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade Treinador (CRUD - Create, Read, Update, Delete).
 */
public interface ITreinadorDAO {

    /**
     * Salva um novo objeto Treinador no banco de dados.
     * @param treinador O objeto Treinador a ser salvo.
     * @return O objeto Treinador salvo, possivelmente com o ID gerado.
     */
    Treinador salvar(Treinador treinador);

    /**
     * Atualiza um objeto Treinador existente no banco de dados.
     * @param treinador O objeto Treinador com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    boolean atualizar(Treinador treinador);

    /**
     * Deleta um Treinador do banco de dados com base no seu ID.
     * @param id O ID do Treinador a ser deletado.
     * @return true se a deleção foi bem-sucedida, false caso contrário.
     */
    boolean deletar(int id);

    /**
     * Busca um Treinador no banco de dados pelo seu ID.
     * @param id O ID do Treinador a ser buscado.
     * @return O objeto Treinador encontrado, ou null se não for encontrado.
     */
    Treinador buscarPorId(int id);

    /**
     * Retorna uma lista com todos os Treinadores cadastrados no banco de dados.
     * @return Uma lista de objetos Treinador.
     */
    List<Treinador> listarTodos();
}
