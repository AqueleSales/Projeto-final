package AssistentePet.dao;

import AssistentePet.model.Clinica;
import java.util.List;

public interface IClinicaDAO {

    Clinica salvar(Clinica clinica);

    boolean atualizar(Clinica clinica);

    boolean deletar(int id);

    Clinica buscarPorId(int id);

    List<Clinica> listarTodos();
}