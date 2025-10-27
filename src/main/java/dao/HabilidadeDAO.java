package dao;

import conexãoBD.ConexaoSQL;
import conexãoBD.IConexao;
import model.Habilidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface IHabilidadeDAO para interagir com o banco de dados MySQL.
 */
public class HabilidadeDAO implements IHabilidadeDAO {

    private final IConexao conexaoBD;

    public HabilidadeDAO() {
        this.conexaoBD = new ConexaoSQL();
    }

    @Override
    public Habilidade salvar(Habilidade habilidade) {
        String sql = "INSERT INTO Habilidade (descricao_habilidade) VALUES (?)";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, habilidade.getDescricaoHabilidade());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    habilidade.setIdHabilidade(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao obter o ID da habilidade.");
                }
            }
            System.out.println("Habilidade salva com sucesso!");
            return habilidade;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar habilidade: " + e.getMessage());
            return null;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean atualizar(Habilidade habilidade) {
        String sql = "UPDATE Habilidade SET descricao_habilidade = ? WHERE id_habilidade = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, habilidade.getDescricaoHabilidade());
            stmt.setInt(2, habilidade.getIdHabilidade());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar habilidade: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM Habilidade WHERE id_habilidade = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao deletar habilidade: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public Habilidade buscarPorId(int id) {
        String sql = "SELECT * FROM Habilidade WHERE id_habilidade = ?";
        Habilidade habilidade = null;

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    habilidade = new Habilidade();
                    habilidade.setIdHabilidade(rs.getInt("id_habilidade"));
                    habilidade.setDescricaoHabilidade(rs.getString("descricao_habilidade"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar habilidade por ID: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return habilidade;
    }

    @Override
    public List<Habilidade> listarTodos() {
        String sql = "SELECT * FROM Habilidade";
        List<Habilidade> habilidades = new ArrayList<>();

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Habilidade habilidade = new Habilidade();
                habilidade.setIdHabilidade(rs.getInt("id_habilidade"));
                habilidade.setDescricaoHabilidade(rs.getString("descricao_habilidade"));
                habilidades.add(habilidade);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar habilidades: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return habilidades;
    }
}
