package dao;

import model.Vacina;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade Vacina.
 */
public interface IVacinaDAO {

    /**
     * Salva uma nova Vacina no banco de dados.
     * @param vacina O objeto Vacina a ser salvo.
     * @return A Vacina salva com o ID gerado.
     */
    Vacina salvar(Vacina vacina);

    /**
     * Atualiza uma Vacina existente no banco.
     * @param vacina O objeto Vacina com os dados atualizados.
     * @return true se a atualização foi bem-sucedida.
     */
    boolean atualizar(Vacina vacina);

    /**
     * Deleta uma Vacina do banco pelo ID.
     * @param id O ID da Vacina a ser deletada.
     * @return true se a deleção foi bem-sucedida.
     */
    boolean deletar(int id);

    /**
     * Busca uma Vacina pelo seu ID.
     * @param id O ID da Vacina.
     * @return A Vacina encontrada, ou null.
     */
    Vacina buscarPorId(int id);

    /**
     * Lista todas as Vacinas cadastradas.
     * @return Uma lista de Vacinas.
     */
    List<Vacina> listarTodos();
}

