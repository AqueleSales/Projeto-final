package dao;

import model.Treinador;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade Treinador.
 */
public interface ITreinadorDAO {

    /**
     * Salva um novo Treinador no banco de dados.
     * @param treinador O objeto Treinador a ser salvo.
     * @return O Treinador salvo com o ID gerado.
     */
    Treinador salvar(Treinador treinador);

    /**
     * Atualiza um Treinador existente no banco de dados.
     * @param treinador O objeto Treinador com os dados atualizados.
     * @return true se a atualização foi bem-sucedida.
     */
    boolean atualizar(Treinador treinador);

    /**
     * Deleta um Treinador do banco pelo ID.
     * @param id O ID da Treinador a ser deletado.
     * @return true se a deleção foi bem-sucedida.
     */
    boolean deletar(int id);

    /**
     * Busca um Treinador pelo seu ID.
     * @param id O ID da Treinador.
     * @return O Treinador encontrado, ou null.
     */
    Treinador buscarPorId(int id);

    /**
     * Lista todos os Treinadores cadastrados.
     * @return Uma lista de Treinador.
     */
    List<Treinador> listarTodos();
}

