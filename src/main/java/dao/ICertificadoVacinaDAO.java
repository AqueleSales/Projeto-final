package dao;

import model.CertificadoVacina;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade CertificadoVacina.
 */
public interface ICertificadoVacinaDAO {

    /**
     * Salva um novo objeto CertificadoVacina no banco de dados.
     * @param certificado O objeto CertificadoVacina a ser salvo.
     * @return O objeto CertificadoVacina salvo, com o ID gerado.
     */
    CertificadoVacina salvar(CertificadoVacina certificado);

    /**
     * Deleta um CertificadoVacina do banco de dados com base no seu ID.
     * @param id O ID do CertificadoVacina a ser deletado.
     * @return true se a deleção foi bem-sucedida, false caso contrário.
     */
    boolean deletar(int id);

    /**
     * Busca um CertificadoVacina no banco de dados pelo seu ID.
     * @param id O ID do CertificadoVacina a ser buscado.
     * @return O objeto CertificadoVacina encontrado, ou null se não for encontrado.
     */
    CertificadoVacina buscarPorId(int id);

    /**
     * Retorna uma lista com todos os Certificados de Vacina de um pet específico.
     * Este é o método principal para exibir o histórico de vacinação.
     * @param idPet O ID do Pet.
     * @return Uma lista de objetos CertificadoVacina.
     */
    List<CertificadoVacina> listarPorPet(int idPet);
}
