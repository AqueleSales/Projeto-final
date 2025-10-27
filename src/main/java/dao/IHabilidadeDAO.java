package dao;

import model.Habilidade;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade Habilidade.
 */
public interface IHabilidadeDAO {

    /**
     * Salva uma nova Habilidade no banco de dados.
     * @param habilidade O objeto Habilidade a ser salvo.
     * @return O Habilidade salvo com o ID gerado.
     */
    Habilidade salvar(Habilidade habilidade);

    /**
     * Atualiza uma Habilidade existente no banco de dados.
     * @param habilidade O objeto Habilidade com os dados atualizados.
     * @return true se a atualização foi bem-sucedida.
     */
    boolean atualizar(Habilidade habilidade);

    /**
     * Deleta uma Habilidade do banco pelo ID.
     * @param id O ID da Habilidade a ser deletada.
     * @return true se a deleção foi bem-sucedida.
     */
    boolean deletar(int id);

    /**
     * Busca uma Habilidade pelo seu ID.
     * @param id O ID da Habilidade.
     * @return O Habilidade encontrado, ou null.
     */
    Habilidade buscarPorId(int id);

    /**
     * Lista todas as Habilidades cadastradas.
     * @return Uma lista de Habilidade.
     */
    List<Habilidade> listarTodos();
}

