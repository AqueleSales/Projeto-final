package dao;

import model.Clinica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Implementação do DAO para Clinica, agora usando Spring Boot e JdbcTemplate.
 */
@Repository
public class ClinicaDAO implements IClinicaDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ClinicaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Clinica salvar(Clinica clinica) {
        String sql = "INSERT INTO Clinica (nome, email, rua, numero, bairro, cidade, CEP) VALUES (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, clinica.getNome());
            stmt.setString(2, clinica.getEmail());
            stmt.setString(3, clinica.getRua());
            stmt.setString(4, clinica.getNumero());
            stmt.setString(5, clinica.getBairro());
            stmt.setString(6, clinica.getCidade());
            stmt.setString(7, clinica.getCep());
            return stmt;
        }, keyHolder);

        clinica.setIdClinica(keyHolder.getKey().intValue());
        System.out.println("Clínica salva com sucesso!");
        return clinica;
    }

    @Override
    public boolean atualizar(Clinica clinica) {
        String sql = "UPDATE Clinica SET nome = ?, email = ?, rua = ?, numero = ?, bairro = ?, cidade = ?, CEP = ? WHERE id_clinica = ?";

        int affectedRows = jdbcTemplate.update(sql,
                clinica.getNome(),
                clinica.getEmail(),
                clinica.getRua(),
                clinica.getNumero(),
                clinica.getBairro(),
                clinica.getCidade(),
                clinica.getCep(),
                clinica.getIdClinica()
        );

        return affectedRows > 0;
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM Clinica WHERE id_clinica = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    @Override
    public Clinica buscarPorId(int id) {
        String sql = "SELECT * FROM Clinica WHERE id_clinica = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Clinica clinica = new Clinica();
                clinica.setIdClinica(rs.getInt("id_clinica"));
                clinica.setNome(rs.getString("nome"));
                clinica.setEmail(rs.getString("email"));
                clinica.setRua(rs.getString("rua"));
                clinica.setNumero(rs.getString("numero"));
                clinica.setBairro(rs.getString("bairro"));
                clinica.setCidade(rs.getString("cidade"));
                clinica.setCep(rs.getString("CEP"));
                return clinica;
            });
        } catch (Exception e) {
            System.err.println("Clínica não encontrada: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Clinica> listarTodos() {
        String sql = "SELECT * FROM Clinica";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Clinica clinica = new Clinica();
            clinica.setIdClinica(rs.getInt("id_clinica"));
            clinica.setNome(rs.getString("nome"));
            clinica.setEmail(rs.getString("email"));
            clinica.setRua(rs.getString("rua"));
            clinica.setNumero(rs.getString("numero"));
            clinica.setBairro(rs.getString("bairro"));
            clinica.setCidade(rs.getString("cidade"));
            clinica.setCep(rs.getString("CEP"));
            return clinica;
        });
    }
}

