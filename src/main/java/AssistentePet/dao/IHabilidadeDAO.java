package AssistentePet.dao;

import AssistentePet.model.Habilidade;
import java.util.List;

public interface IHabilidadeDAO {

    Habilidade salvar(Habilidade habilidade);

    boolean atualizar(Habilidade habilidade);

    boolean deletar(int id);

    Habilidade buscarPorId(int id);

    List<Habilidade> listarTodos();
}