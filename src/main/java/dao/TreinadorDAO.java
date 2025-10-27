package dao;

import conexãoBD.ConexaoSQL;
import conexãoBD.IConexao;
import model.Treinador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface ITreinadorDAO para interagir com o banco de dados MySQL.
 * Esta classe contém a lógica SQL para as operações de CRUD da entidade Treinador.
 */
public class TreinadorDAO implements ITreinadorDAO {

    private final IConexao conexaoBD;

    public TreinadorDAO() {
        this.conexaoBD = new ConexaoSQL();
    }

    @Override
    public Treinador salvar(Treinador treinador) {
        String sql = "INSERT INTO Treinador (nome, cpf, numero_certificacao_profissional) VALUES (?, ?, ?)";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, treinador.getNome());
            stmt.setString(2, treinador.getCpf());
            stmt.setString(3, treinador.getNumeroCertificacaoProfissional());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    treinador.setIdTreinador(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao obter o ID do treinador.");
                }
            }
            System.out.println("Treinador salvo com sucesso!");
            return treinador;
        } catch (SQLException e) {
            System.err.println("Erro ao salvar treinador: " + e.getMessage());
            return null;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean atualizar(Treinador treinador) {
        String sql = "UPDATE Treinador SET nome = ?, cpf = ?, numero_certificacao_profissional = ? WHERE id_treinador = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, treinador.getNome());
            stmt.setString(2, treinador.getCpf());
            stmt.setString(3, treinador.getNumeroCertificacaoProfissional());
            stmt.setInt(4, treinador.getIdTreinador());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar treinador: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM Treinador WHERE id_treinador = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar treinador: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public Treinador buscarPorId(int id) {
        String sql = "SELECT * FROM Treinador WHERE id_treinador = ?";
        Treinador treinador = null;

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    treinador = new Treinador();
                    treinador.setIdTreinador(rs.getInt("id_treinador"));
                    treinador.setNome(rs.getString("nome"));
                    treinador.setCpf(rs.getString("cpf"));
                    treinador.setNumeroCertificacaoProfissional(rs.getString("numero_certificacao_profissional"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar treinador por ID: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return treinador;
    }

    @Override
    public List<Treinador> listarTodos() {
        String sql = "SELECT * FROM Treinador";
        List<Treinador> treinadores = new ArrayList<>();

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Treinador treinador = new Treinador();
                treinador.setIdTreinador(rs.getInt("id_treinador"));
                treinador.setNome(rs.getString("nome"));
                treinador.setCpf(rs.getString("cpf"));
                treinador.setNumeroCertificacaoProfissional(rs.getString("numero_certificacao_profissional"));
                treinadores.add(treinador);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar treinadores: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return treinadores;
    }
}
