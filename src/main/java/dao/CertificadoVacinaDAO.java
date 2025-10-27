package dao;

import model.CertificadoVacina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Implementação do DAO para CertificadoVacina, agora usando Spring Boot e JdbcTemplate.
 */
@Repository
public class CertificadoVacinaDAO implements ICertificadoVacinaDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CertificadoVacinaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CertificadoVacina salvar(CertificadoVacina certificado) {
        String sql = "INSERT INTO CertificadoVacina (data_aplicacao, lote, proxima_dose, id_pet, id_vacina, id_veterinario, id_clinica) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setDate(1, Date.valueOf(certificado.getDataAplicacao()));
            stmt.setString(2, certificado.getLote());
            stmt.setDate(3, certificado.getProximaDose() != null ? Date.valueOf(certificado.getProximaDose()) : null);
            stmt.setInt(4, certificado.getIdPet());
            stmt.setInt(5, certificado.getIdVacina());
            stmt.setInt(6, certificado.getIdVeterinario());
            stmt.setInt(7, certificado.getIdClinica());
            return stmt;
        }, keyHolder);

        certificado.setIdCertificadoVac(keyHolder.getKey().intValue());
        System.out.println("Certificado de vacina salvo com sucesso!");
        return certificado;
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM CertificadoVacina WHERE id_certificado_vac = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    // Helper para mapear o ResultSet para o objeto (RowMapper)
    private CertificadoVacina mapRowToCertificado(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        CertificadoVacina cert = new CertificadoVacina();
        cert.setIdCertificadoVac(rs.getInt("id_certificado_vac"));
        cert.setDataAplicacao(rs.getDate("data_aplicacao").toLocalDate());
        cert.setLote(rs.getString("lote"));

        Date proximaDoseSql = rs.getDate("proxima_dose");
        if (proximaDoseSql != null) {
            cert.setProximaDose(proximaDoseSql.toLocalDate());
        }

        cert.setIdPet(rs.getInt("id_pet"));
        cert.setIdVacina(rs.getInt("id_vacina"));
        cert.setIdVeterinario(rs.getInt("id_veterinario"));
        cert.setIdClinica(rs.getInt("id_clinica"));
        return cert;
    }

    @Override
    public CertificadoVacina buscarPorId(int id) {
        String sql = "SELECT * FROM CertificadoVacina WHERE id_certificado_vac = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, this::mapRowToCertificado);
        } catch (Exception e) {
            System.err.println("Certificado não encontrado: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<CertificadoVacina> listarPorPet(int idPet) {
        String sql = "SELECT * FROM CertificadoVacina WHERE id_pet = ?";
        try {
            return jdbcTemplate.query(sql, new Object[]{idPet}, this::mapRowToCertificado);
        } catch (Exception e) {
            System.err.println("Erro ao listar certificados por pet: " + e.getMessage());
            return java.util.Collections.emptyList(); // Retorna lista vazia em caso de erro
        }
    }
}

