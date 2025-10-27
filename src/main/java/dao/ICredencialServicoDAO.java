package dao;

import model.CredencialServico;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade CredencialServico.
 */
public interface ICredencialServicoDAO {

    /**
     * Salva uma nova credencial e suas habilidades (relação M:N).
     * @param credencial O objeto CredencialServico a ser salvo.
     * @return A credencial salva com o ID.
     */
    CredencialServico salvar(CredencialServico credencial);

    /**
     * Busca uma credencial pelo ID do animal de serviço.
     * @param idAnimal O ID do AnimalDeServico.
     * @return A credencial encontrada, já com a lista de habilidades.
     */
    CredencialServico buscarPorAnimalId(int idAnimal);

    /**
     * Busca uma credencial pelo seu próprio ID.
     * @param id O ID da Credencial.
     * @return A credencial encontrada, já com a lista de habilidades.
     */
    CredencialServico buscarPorId(int id);

    /**
     * Deleta uma credencial e suas relações M:N.
     * @param id O ID da credencial a deletar.
     * @return true se a deleção foi bem-sucedida.
     */
    boolean deletar(int id);
}

