package dao;

// Usando o pacote 'conexãoBD' e a classe 'ConexaoSQL' do seu projeto
import conexãoBD.ConexaoSQL;
import conexãoBD.IConexao;
import model.Vacina;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface IVacinaDAO para interagir com o banco de dados MySQL.
 */
public class VacinaDAO implements IVacinaDAO {

    private final IConexao conexaoBD;

    public VacinaDAO() {
        this.conexaoBD = new ConexaoSQL();
    }

    @Override
    public Vacina salvar(Vacina vacina) {
        String sql = "INSERT INTO Vacina (nome_vacina, tipo) VALUES (?, ?)";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, vacina.getNomeVacina());
            stmt.setString(2, vacina.getTipo());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vacina.setIdVacina(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao obter o ID da vacina.");
                }
            }
            System.out.println("Vacina salva com sucesso!");
            return vacina;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar vacina: " + e.getMessage());
            return null;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean atualizar(Vacina vacina) {
        String sql = "UPDATE Vacina SET nome_vacina = ?, tipo = ? WHERE id_vacina = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, vacina.getNomeVacina());
            stmt.setString(2, vacina.getTipo());
            stmt.setInt(3, vacina.getIdVacina());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar vacina: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM Vacina WHERE id_vacina = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar vacina: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public Vacina buscarPorId(int id) {
        String sql = "SELECT * FROM Vacina WHERE id_vacina = ?";
        Vacina vacina = null;

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    vacina = new Vacina();
                    vacina.setIdVacina(rs.getInt("id_vacina"));
                    vacina.setNomeVacina(rs.getString("nome_vacina"));
                    vacina.setTipo(rs.getString("tipo"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar vacina por ID: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return vacina;
    }

    @Override
    public List<Vacina> listarTodas() {
        String sql = "SELECT * FROM Vacina";
        List<Vacina> vacinas = new ArrayList<>();

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vacina vacina = new Vacina();
                vacina.setIdVacina(rs.getInt("id_vacina"));
                vacina.setNomeVacina(rs.getString("nome_vacina"));
                vacina.setTipo(rs.getString("tipo"));
                vacinas.add(vacina);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar vacinas: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return vacinas;
    }
}
