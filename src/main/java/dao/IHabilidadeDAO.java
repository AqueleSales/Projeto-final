package dao;

import model.Habilidade;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade Habilidade (CRUD).
 */
public interface IHabilidadeDAO {

    /**
     * Salva um novo objeto Habilidade no banco de dados.
     * @param habilidade O objeto Habilidade a ser salvo.
     * @return O objeto Habilidade salvo, com o ID gerado.
     */
    Habilidade salvar(Habilidade habilidade);

    /**
     * Atualiza um objeto Habilidade existente no banco de dados.
     * @param habilidade O objeto Habilidade com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    boolean atualizar(Habilidade habilidade);

    /**
     * Deleta uma Habilidade do banco de dados com base no seu ID.
     * @param id O ID da Habilidade a ser deletada.
     * @return true se a deleção foi bem-sucedida, false caso contrário.
     */
    boolean deletar(int id);

    /**
     * Busca uma Habilidade no banco de dados pelo seu ID.
     * @param id O ID da Habilidade a ser buscada.
     * @return O objeto Habilidade encontrado, ou null se não for encontrado.
     */
    Habilidade buscarPorId(int id);

    /**
     * Retorna uma lista com todas as Habilidades cadastradas no banco de dados.
     * @return Uma lista de objetos Habilidade.
     */
    List<Habilidade> listarTodos();
}
