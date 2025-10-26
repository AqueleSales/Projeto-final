package dao;

// Usando o pacote 'conexãoBD' e a classe 'ConexaoSQL' do seu projeto
import conexãoBD.ConexaoSQL;
import conexãoBD.IConexao;
import model.Clinica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface IClinicaDAO para interagir com o banco de dados MySQL.
 */
public class ClinicaDAO implements IClinicaDAO {

    private final IConexao conexaoBD;

    public ClinicaDAO() {
        this.conexaoBD = new ConexaoSQL();
    }

    @Override
    public Clinica salvar(Clinica clinica) {
        String sql = "INSERT INTO Clinica (nome, email, rua, numero, bairro, cidade, CEP) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, clinica.getNome());
            stmt.setString(2, clinica.getEmail());
            stmt.setString(3, clinica.getRua());
            stmt.setString(4, clinica.getNumero());
            stmt.setString(5, clinica.getBairro());
            stmt.setString(6, clinica.getCidade());
            stmt.setString(7, clinica.getCep());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    clinica.setIdClinica(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao obter o ID da clínica.");
                }
            }
            System.out.println("Clínica salva com sucesso!");
            return clinica;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar clínica: " + e.getMessage());
            return null;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean atualizar(Clinica clinica) {
        String sql = "UPDATE Clinica SET nome = ?, email = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, CEP = ? WHERE id_clinica = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, clinica.getNome());
            stmt.setString(2, clinica.getEmail());
            stmt.setString(3, clinica.getRua());
            stmt.setString(4, clinica.getNumero());
            stmt.setString(5, clinica.getBairro());
            stmt.setString(6, clinica.getCidade());
            stmt.setString(7, clinica.getCep());
            stmt.setInt(8, clinica.getIdClinica());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar clínica: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM Clinica WHERE id_clinica = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar clínica: " + e.getMessage());
            // Se der erro de FK, precisaremos de uma lógica mais complexa (transação)
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public Clinica buscarPorId(int id) {
        String sql = "SELECT * FROM Clinica WHERE id_clinica = ?";
        Clinica clinica = null;

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    clinica = new Clinica();
                    clinica.setIdClinica(rs.getInt("id_clinica"));
                    clinica.setNome(rs.getString("nome"));
                    clinica.setEmail(rs.getString("email"));
                    clinica.setRua(rs.getString("rua"));
                    clinica.setNumero(rs.getString("numero"));
                    clinica.setBairro(rs.getString("bairro"));
                    clinica.setCidade(rs.getString("cidade"));
                    clinica.setCep(rs.getString("CEP"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar clínica por ID: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return clinica;
    }

    @Override
    public List<Clinica> listarTodas() {
        String sql = "SELECT * FROM Clinica";
        List<Clinica> clinicas = new ArrayList<>();

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Clinica clinica = new Clinica();
                clinica.setIdClinica(rs.getInt("id_clinica"));
                clinica.setNome(rs.getString("nome"));
                clinica.setEmail(rs.getString("email"));
                clinica.setRua(rs.getString("rua"));
                clinica.setNumero(rs.getString("numero"));
                clinica.setBairro(rs.getString("bairro"));
                clinica.setCidade(rs.getString("cidade"));
                clinica.setCep(rs.getString("CEP"));
                clinicas.add(clinica);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar clínicas: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return clinicas;
    }
}
