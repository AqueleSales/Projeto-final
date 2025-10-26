package dao;

import model.Vacina;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade Vacina (CRUD).
 */
public interface IVacinaDAO {

    /**
     * Salva um novo objeto Vacina no banco de dados.
     * @param vacina O objeto Vacina a ser salvo.
     * @return O objeto Vacina salvo, com o ID gerado.
     */
    Vacina salvar(Vacina vacina);

    /**
     * Atualiza um objeto Vacina existente no banco de dados.
     * @param vacina O objeto Vacina com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    boolean atualizar(Vacina vacina);

    /**
     * Deleta uma Vacina do banco de dados com base no seu ID.
     * @param id O ID da Vacina a ser deletada.
     * @return true se a deleção foi bem-sucedida, false caso contrário.
     */
    boolean deletar(int id);

    /**
     * Busca uma Vacina no banco de dados pelo seu ID.
     * @param id O ID da Vacina a ser buscada.
     * @return O objeto Vacina encontrado, ou null se não for encontrado.
     */
    Vacina buscarPorId(int id);

    /**
     * Retorna uma lista com todas as Vacinas cadastradas no banco de dados.
     * @return Uma lista de objetos Vacina.
     */
    List<Vacina> listarTodas();
}
