package dao;

import model.Clinica;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade Clinica (CRUD).
 */
public interface IClinicaDAO {

    /**
     * Salva um novo objeto Clinica no banco de dados.
     * @param clinica O objeto Clinica a ser salvo.
     * @return O objeto Clinica salvo, com o ID gerado.
     */
    Clinica salvar(Clinica clinica);

    /**
     * Atualiza um objeto Clinica existente no banco de dados.
     * @param clinica O objeto Clinica com os dados atualizados.
     * @return true se a atualização foi bem-sucedida, false caso contrário.
     */
    boolean atualizar(Clinica clinica);

    /**
     * Deleta uma Clinica do banco de dados com base no seu ID.
     * @param id O ID da Clinica a ser deletada.
     * @return true se a deleção foi bem-sucedida, false caso contrário.
     */
    boolean deletar(int id);

    /**
     * Busca uma Clinica no banco de dados pelo seu ID.
     * @param id O ID da Clinica a ser buscada.
     * @return O objeto Clinica encontrado, ou null se não for encontrado.
     */
    Clinica buscarPorId(int id);

    /**
     * Retorna uma lista com todas as Clinicas cadastradas no banco de dados.
     * @return Uma lista de objetos Clinica.
     */
    List<Clinica> listarTodas();
}
