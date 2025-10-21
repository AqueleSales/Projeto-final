import dao.IPessoaDAO;
import dao.PessoaDAO;
import model.Pessoa;

import java.util.List;

/**
 * Classe principal para testar as funcionalidades da camada DAO.
 * Executa as operações de CRUD para a entidade Pessoa para verificar
 * se a conexão com o banco e a lógica SQL estão funcionando corretamente.
 */
public class main {

    public static void main(String[] args) {

        IPessoaDAO pessoaDAO = new PessoaDAO();

        // --- 1. TESTE DE SALVAR ---
        System.out.println("--- Testando: Salvar Pessoa ---");
        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome("Gabriel Sales");
        novaPessoa.setCpf("123.456.789-00");
        novaPessoa.setEmail("gabriel.sales@email.com");
        novaPessoa.addTelefone("6199999-8888");
        novaPessoa.addTelefone("613333-4444");

        Pessoa pessoaSalva = pessoaDAO.salvar(novaPessoa);

        if (pessoaSalva != null) {
            System.out.println("Pessoa salva com sucesso! ID: " + pessoaSalva.getIdPessoa());
            System.out.println(pessoaSalva);
        } else {
            System.err.println("Falha ao salvar a pessoa.");
            // Se falhar aqui, não vale a pena continuar os outros testes.
            return;
        }

        System.out.println("\n-----------------------------------\n");


        // --- 2. TESTE DE LISTAR TODOS ---
        System.out.println("--- Testando: Listar Todas as Pessoas ---");
        List<Pessoa> todasAsPessoas = pessoaDAO.listarTodos();
        if (todasAsPessoas.isEmpty()) {
            System.err.println("Nenhuma pessoa encontrada na lista.");
        } else {
            System.out.println("Pessoas encontradas: " + todasAsPessoas.size());
            for (Pessoa p : todasAsPessoas) {
                System.out.println(p);
            }
        }
        System.out.println("\n-----------------------------------\n");


        // --- 3. TESTE DE BUSCAR POR ID ---
        int idParaBuscar = pessoaSalva.getIdPessoa();
        System.out.println("--- Testando: Buscar Pessoa por ID (" + idParaBuscar + ") ---");
        Pessoa pessoaEncontrada = pessoaDAO.buscarPorId(idParaBuscar);
        if (pessoaEncontrada != null) {
            System.out.println("Pessoa encontrada:");
            System.out.println(pessoaEncontrada);
        } else {
            System.err.println("Pessoa com ID " + idParaBuscar + " não foi encontrada.");
        }
        System.out.println("\n-----------------------------------\n");

        // --- 4. TESTE DE ATUALIZAR ---
        System.out.println("--- Testando: Atualizar Pessoa ---");
        if (pessoaEncontrada != null) {
            pessoaEncontrada.setNome("Gabriel Sales Nogueira"); // Nome atualizado
            boolean atualizou = pessoaDAO.atualizar(pessoaEncontrada);
            if(atualizou) {
                System.out.println("Pessoa atualizada com sucesso!");
                // Busca novamente para confirmar a alteração
                Pessoa pessoaAtualizada = pessoaDAO.buscarPorId(idParaBuscar);
                System.out.println("Dados após atualização: " + pessoaAtualizada);
            } else {
                System.err.println("Falha ao atualizar a pessoa.");
            }
        }
        System.out.println("\n-----------------------------------\n");


        // --- 5. TESTE DE DELETAR ---
        System.out.println("--- Testando: Deletar Pessoa ---");
        boolean deletou = pessoaDAO.deletar(idParaBuscar);
        if (deletou) {
            System.out.println("Pessoa com ID " + idParaBuscar + " deletada com sucesso!");
            // Tenta buscar novamente para confirmar que foi deletada
            Pessoa pessoaApagada = pessoaDAO.buscarPorId(idParaBuscar);
            if (pessoaApagada == null) {
                System.out.println("Confirmação: Pessoa não encontrada após deleção.");
            } else {
                System.err.println("ERRO: Pessoa ainda foi encontrada após a deleção.");
            }
        } else {
            System.err.println("Falha ao deletar a pessoa com ID " + idParaBuscar);
        }
    }
}
