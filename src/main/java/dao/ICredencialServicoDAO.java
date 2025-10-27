package dao;

import model.CredencialServico;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade CredencialServico e sua relação M:N com Habilidade.
 */
public interface ICredencialServicoDAO {

    /**
     * Salva uma nova CredencialServico no banco de dados,
     * incluindo suas associações com Habilidades (relação M:N).
     *
     * @param credencial O objeto CredencialServico a ser salvo.
     * @return O objeto CredencialServico salvo, com o ID gerado.
     */
    CredencialServico salvar(CredencialServico credencial);

    /**
     * Busca uma CredencialServico pelo ID do Animal de Serviço.
     * A relação é 1:1, então cada animal só tem uma credencial.
     *
     * @param idAnimalServico O ID do Animal de Serviço.
     * @return A CredencialServico encontrada, ou null.
     */
    CredencialServico buscarPorIdAnimal(int idAnimalServico);

    /**
     * Deleta uma CredencialServico do banco de dados.
     * A deleção em cascata (configurada no BD) deve remover
     * as associações em Credencial_Habilidade.
     *
     * @param idCredencial O ID da credencial a ser deletada.
     * @return true se a deleção foi bem-sucedida, false caso contrário.
     */
    boolean deletar(int idCredencial);

    // Métodos de atualização e listagem podem ser adicionados conforme necessidade.
}
