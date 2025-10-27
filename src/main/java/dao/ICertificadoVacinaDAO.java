package dao;

import model.CertificadoVacina;
import java.util.List;

/**
 * Interface que define o contrato para as operações de persistência
 * da entidade CertificadoVacina.
 */
public interface ICertificadoVacinaDAO {

    /**
     * Salva um novo Certificado de Vacina no banco de dados.
     * @param certificado O objeto CertificadoVacina a ser salvo.
     * @return O CertificadoVacina salvo com o ID gerado.
     */
    CertificadoVacina salvar(CertificadoVacina certificado);

    /**
     * Deleta um Certificado de Vacina do banco pelo ID.
     * @param id O ID do Certificado a ser deletado.
     * @return true se a deleção foi bem-sucedida.
     */
    boolean deletar(int id);

    /**
     * Busca um Certificado de Vacina pelo seu ID.
     * @param id O ID do Certificado.
     * @return O CertificadoVacina encontrado, ou null.
     */
    CertificadoVacina buscarPorId(int id);

    /**
     * Lista todos os Certificados de Vacina de um pet específico.
     * @param idPet O ID do Pet.
     * @return Uma lista de CertificadoVacina.
     */
    List<CertificadoVacina> listarPorPet(int idPet);
}

