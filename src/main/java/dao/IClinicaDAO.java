package dao;

import model.Clinica;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade Clinica.
 * (Não muda com o Spring, pois define apenas os métodos de negócio).
 */
public interface IClinicaDAO {

    /**
     * Salva uma nova Clinica no banco de dados.
     * @param clinica O objeto Clinica a ser salvo.
     * @return A Clinica salva com o ID gerado.
     */
    Clinica salvar(Clinica clinica);

    /**
     * Atualiza uma Clinica existente no banco.
     * @param clinica O objeto Clinica com os dados atualizados.
     * @return true se a atualização foi bem-sucedida.
     */
    boolean atualizar(Clinica clinica);

    /**
     * Deleta uma Clinica do banco pelo ID.
     * @param id O ID da Clinica a ser deletada.
     * @return true se a deleção foi bem-sucedida.
     */
    boolean deletar(int id);

    /**
     * Busca uma Clinica pelo seu ID.
     * @param id O ID da Clinica.
     * @return A Clinica encontrada, ou null.
     */
    Clinica buscarPorId(int id);

    /**
     * Lista todas as Clinicas cadastradas.
     * @return Uma lista de Clinicas.
     */
    List<Clinica> listarTodos();
}

