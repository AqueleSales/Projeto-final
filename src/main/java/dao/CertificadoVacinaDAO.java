package dao;

// Usando o pacote 'conexãoBD' e a classe 'ConexaoSQL' do seu projeto
import conexãoBD.ConexaoSQL;
import conexãoBD.IConexao;
import model.CertificadoVacina;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface ICertificadoVacinaDAO para interagir com o banco de dados MySQL.
 */
public class CertificadoVacinaDAO implements ICertificadoVacinaDAO {

    private final IConexao conexaoBD;

    public CertificadoVacinaDAO() {
        this.conexaoBD = new ConexaoSQL();
    }

    @Override
    public CertificadoVacina salvar(CertificadoVacina certificado) {
        String sql = "INSERT INTO CertificadoVacina (data_aplicacao, lote, proxima_dose, id_pet, id_vacina, id_veterinario, id_clinica) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setDate(1, Date.valueOf(certificado.getDataAplicacao()));
            stmt.setString(2, certificado.getLote());
            // proxima_dose pode ser nula
            if (certificado.getProximaDose() != null) {
                stmt.setDate(3, Date.valueOf(certificado.getProximaDose()));
            } else {
                stmt.setNull(3, Types.DATE);
            }
            stmt.setInt(4, certificado.getIdPet());
            stmt.setInt(5, certificado.getIdVacina());
            stmt.setInt(6, certificado.getIdVeterinario());
            stmt.setInt(7, certificado.getIdClinica());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    certificado.setIdCertificadoVac(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Falha ao obter o ID do certificado.");
                }
            }
            System.out.println("Certificado de vacina salvo com sucesso!");
            return certificado;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar certificado: " + e.getMessage());
            return null;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM CertificadoVacina WHERE id_certificado_vac = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar certificado: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public CertificadoVacina buscarPorId(int id) {
        String sql = "SELECT * FROM CertificadoVacina WHERE id_certificado_vac = ?";
        CertificadoVacina certificado = null;

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    certificado = new CertificadoVacina();
                    certificado.setIdCertificadoVac(rs.getInt("id_certificado_vac"));
                    certificado.setDataAplicacao(rs.getDate("data_aplicacao").toLocalDate());
                    certificado.setLote(rs.getString("lote"));

                    Date proximaDoseSql = rs.getDate("proxima_dose");
                    if (proximaDoseSql != null) {
                        certificado.setProximaDose(proximaDoseSql.toLocalDate());
                    }

                    certificado.setIdPet(rs.getInt("id_pet"));
                    certificado.setIdVacina(rs.getInt("id_vacina"));
                    certificado.setIdVeterinario(rs.getInt("id_veterinario"));
                    certificado.setIdClinica(rs.getInt("id_clinica"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar certificado por ID: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return certificado;
    }

    @Override
    public List<CertificadoVacina> listarPorPet(int idPet) {
        String sql = "SELECT * FROM CertificadoVacina WHERE id_pet = ?";
        List<CertificadoVacina> certificados = new ArrayList<>();

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idPet);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CertificadoVacina certificado = new CertificadoVacina();
                    certificado.setIdCertificadoVac(rs.getInt("id_certificado_vac"));
                    certificado.setDataAplicacao(rs.getDate("data_aplicacao").toLocalDate());
                    certificado.setLote(rs.getString("lote"));

                    Date proximaDoseSql = rs.getDate("proxima_dose");
                    if (proximaDoseSql != null) {
                        certificado.setProximaDose(proximaDoseSql.toLocalDate());
                    }

                    certificado.setIdPet(rs.getInt("id_pet"));
                    certificado.setIdVacina(rs.getInt("id_vacina"));
                    certificado.setIdVeterinario(rs.getInt("id_veterinario"));
                    certificado.setIdClinica(rs.getInt("id_clinica"));

                    certificados.add(certificado);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar certificados por pet: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return certificados;
    }
}
