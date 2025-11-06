package AssistentePet.dao;

import AssistentePet.model.Vacina;
import java.util.List;

public interface IVacinaDAO {

    Vacina salvar(Vacina vacina);

    boolean atualizar(Vacina vacina);

    boolean deletar(int id);

    Vacina buscarPorId(int id);

    List<Vacina> listarTodos();
}