package dao;

// Usando o pacote 'conexãoBD' e a classe 'ConexaoSQL' do seu projeto
import conexãoBD.ConexaoSQL;
import conexãoBD.IConexao;
import model.Dono;
import model.Pessoa;
import model.Veterinario; // Importa a classe Veterinario

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do DAO para Pessoa (Versão Atualizada).
 * Salva a Pessoa e sua especialização (Dono ou Veterinario).
 * Deleta Pessoa usando ON DELETE CASCADE.
 */
public class PessoaDAO implements IPessoaDAO {

    private final IConexao conexaoBD;

    public PessoaDAO() {
        this.conexaoBD = new ConexaoSQL();
    }

    @Override
    public Pessoa salvar(Pessoa pessoa) {
        String sqlPessoa = "INSERT INTO Pessoa (nome, cpf, email) VALUES (?, ?, ?)";
        String sqlDono = "INSERT INTO Dono (id_pessoa) VALUES (?)";
        String sqlVeterinario = "INSERT INTO Veterinario (id_pessoa, CRMV) VALUES (?, ?)";
        String sqlTelefone = "INSERT INTO Pessoa_Telefone (id_pessoa, telefone) VALUES (?, ?)";
        Connection conexao = null;

        try {
            conexao = conexaoBD.getConexao();
            conexao.setAutoCommit(false); // Controlar transação manualmente

            // --- Passo 1: Inserir a Pessoa e obter o ID gerado ---
            try (PreparedStatement stmtPessoa = conexao.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS)) {
                stmtPessoa.setString(1, pessoa.getNome());
                stmtPessoa.setString(2, pessoa.getCpf());
                stmtPessoa.setString(3, pessoa.getEmail());
                stmtPessoa.executeUpdate();

                try (ResultSet generatedKeys = stmtPessoa.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pessoa.setIdPessoa(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Falha ao obter o ID da pessoa.");
                    }
                }
            }

            // --- Passo 2: Inserir na tabela de especialização correta ---
            if (pessoa instanceof Veterinario) {
                // Se o objeto é um Veterinario, salva na tabela Veterinario
                try (PreparedStatement stmtVet = conexao.prepareStatement(sqlVeterinario)) {
                    stmtVet.setInt(1, pessoa.getIdPessoa());
                    stmtVet.setString(2, ((Veterinario) pessoa).getCrmv());
                    stmtVet.executeUpdate();
                    System.out.println("Registro de Veterinario salvo!");
                }
            } else {
                // Se for um Dono ou uma Pessoa genérica, salva na tabela Dono
                // (Mantém a lógica que fez nosso teste passar)
                try (PreparedStatement stmtDono = conexao.prepareStatement(sqlDono)) {
                    stmtDono.setInt(1, pessoa.getIdPessoa());
                    stmtDono.executeUpdate();
                    System.out.println("Registro de Dono salvo!");
                }
            }

            // --- Passo 3: Inserir os telefones associados ---
            if (pessoa.getTelefones() != null && !pessoa.getTelefones().isEmpty()) {
                try (PreparedStatement stmtTelefone = conexao.prepareStatement(sqlTelefone)) {
                    for (String telefone : pessoa.getTelefones()) {
                        stmtTelefone.setInt(1, pessoa.getIdPessoa());
                        stmtTelefone.setString(2, telefone);
                        stmtTelefone.addBatch();
                    }
                    stmtTelefone.executeBatch();
                }
            }

            conexao.commit(); // Efetivar a transação
            System.out.println("Pessoa e especialização salvos com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar pessoa/especialização: " + e.getMessage());
            if (conexao != null) {
                try {
                    conexao.rollback(); // Reverter em caso de erro
                    System.err.println("Transação revertida.");
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter a transação: " + ex.getMessage());
                }
            }
            return null;
        } finally {
            if (conexao != null) {
                try {
                    conexao.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Erro ao restaurar auto-commit: " + e.getMessage());
                }
            }
            conexaoBD.closeConexao();
        }
        return pessoa;
    }

    @Override
    public boolean atualizar(Pessoa pessoa) {
        // Atualiza os dados da Pessoa
        String sql = "UPDATE Pessoa SET nome = ?, cpf = ?, email = ? WHERE id_pessoa = ?";
        // TODO: Adicionar lógica para atualizar tabelas filhas (ex: CRMV do Veterinario)
        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getCpf());
            stmt.setString(3, pessoa.getEmail());
            stmt.setInt(4, pessoa.getIdPessoa());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pessoa: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean deletar(int id) {
        // MÉTODO SIMPLIFICADO: Graças ao "ON DELETE CASCADE" no nosso script SQL,
        // só precisamos deletar da tabela Pessoa. O banco de dados
        // cuidará de deletar os registros em Dono, Veterinario, Pessoa_Telefone e Possui.

        String sqlPessoa = "DELETE FROM Pessoa WHERE id_pessoa = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmtPessoa = conexao.prepareStatement(sqlPessoa)) {

            stmtPessoa.setInt(1, id);
            int affectedRows = stmtPessoa.executeUpdate();

            if (affectedRows == 0) {
                System.err.println("Deleção falhou, nenhuma pessoa encontrada com o ID: " + id);
            }

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar pessoa: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public Pessoa buscarPorId(int id) {
        // Esta busca ainda retorna uma Pessoa genérica.
        // TODO: Implementar lógica para checar se é Dono ou Veterinario e retornar o tipo correto.
        String sql = "SELECT p.id_pessoa, p.nome, p.cpf, p.email, pt.telefone " +
                "FROM Pessoa p " +
                "LEFT JOIN Pessoa_Telefone pt ON p.id_pessoa = pt.id_pessoa " +
                "WHERE p.id_pessoa = ?";
        Pessoa pessoa = null;

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    if (pessoa == null) {
                        pessoa = new Pessoa(); // Por enquanto, retorna Pessoa genérica
                        pessoa.setIdPessoa(rs.getInt("id_pessoa"));
                        pessoa.setNome(rs.getString("nome"));
                        pessoa.setCpf(rs.getString("cpf"));
                        pessoa.setEmail(rs.getString("email"));
                    }
                    String telefone = rs.getString("telefone");
                    if (telefone != null) {
                        pessoa.addTelefone(telefone);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pessoa por ID: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return pessoa;
    }

    @Override
    public List<Pessoa> listarTodos() {
        String sql = "SELECT id_pessoa, nome, cpf, email FROM Pessoa";
        List<Pessoa> pessoas = new ArrayList<>();

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pessoa pessoa = new Pessoa();
                pessoa.setIdPessoa(rs.getInt("id_pessoa"));
                pessoa.setNome(rs.getString("nome"));
                pessoa.setCpf(rs.getString("cpf"));
                pessoa.setEmail(rs.getString("email"));
                pessoas.add(pessoa);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar pessoas: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return pessoas;
    }
}

