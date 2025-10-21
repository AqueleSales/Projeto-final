package dao;

import model.Pessoa;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade Pessoa (CRUD - Create, Read, Update, Delete).
 */
public interface IPessoaDAO {

    /**
     * Salva um novo objeto Pessoa no banco de dados.
     * @param pessoa O objeto Pessoa a ser salvo.
     * @return O objeto Pessoa salvo, possivelmente com o ID gerado.
     */
    Pessoa salvar(Pessoa pessoa);

    /**
     * Atualiza um objeto Pessoa existente no banco de dados.
     * @param pessoa O objeto Pessoa com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    boolean atualizar(Pessoa pessoa);

    /**
     * Deleta uma Pessoa do banco de dados com base no seu ID.
     * @param id O ID da Pessoa a ser deletada.
     * @return true se a deleção foi bem-sucedida, false caso contrário.
     */
    boolean deletar(int id);

    /**
     * Busca uma Pessoa no banco de dados pelo seu ID.
     * @param id O ID da Pessoa a ser buscada.
     * @return O objeto Pessoa encontrado, ou null se não for encontrado.
     */
    Pessoa buscarPorId(int id);

    /**
     * Retorna uma lista com todas as Pessoas cadastradas no banco de dados.
     * @return Uma lista de objetos Pessoa.
     */
    List<Pessoa> listarTodos();
}
