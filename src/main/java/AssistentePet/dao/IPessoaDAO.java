package AssistentePet.dao;

import AssistentePet.model.Pessoa;
import java.util.List;

public interface IPessoaDAO {

    Pessoa salvar(Pessoa pessoa);

    boolean atualizar(Pessoa pessoa);

    boolean deletar(int id);

    Pessoa buscarPorId(int id);

    List<Pessoa> listarTodos();

    Pessoa buscarPorEmail(String email);

    Pessoa buscarPorCpf(String cpf);
}