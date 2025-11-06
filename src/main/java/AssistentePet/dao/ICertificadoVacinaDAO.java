package AssistentePet.dao;

import AssistentePet.model.CertificadoVacina;
import java.util.List;

public interface ICertificadoVacinaDAO {

    CertificadoVacina salvar(CertificadoVacina certificado);

    boolean deletar(int id);

    CertificadoVacina buscarPorId(int id);

    List<CertificadoVacina> listarPorPet(int idPet);
}