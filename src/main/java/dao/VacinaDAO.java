package dao;

import model.Vacina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Implementação do DAO para Vacina, agora usando Spring Boot e JdbcTemplate.
 */
@Repository
public class VacinaDAO implements IVacinaDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public VacinaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Vacina salvar(Vacina vacina) {
        String sql = "INSERT INTO Vacina (nome_vacina, tipo) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, vacina.getNomeVacina());
            stmt.setString(2, vacina.getTipo());
            return stmt;
        }, keyHolder);

        vacina.setIdVacina(keyHolder.getKey().intValue());
        System.out.println("Vacina salva com sucesso!");
        return vacina;
    }

    @Override
    public boolean atualizar(Vacina vacina) {
        String sql = "UPDATE Vacina SET nome_vacina = ?, tipo = ? WHERE id_vacina = ?";

        int affectedRows = jdbcTemplate.update(sql,
                vacina.getNomeVacina(),
                vacina.getTipo(),
                vacina.getIdVacina()
        );

        return affectedRows > 0;
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM Vacina WHERE id_vacina = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    @Override
    public Vacina buscarPorId(int id) {
        String sql = "SELECT * FROM Vacina WHERE id_vacina = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Vacina vacina = new Vacina();
                vacina.setIdVacina(rs.getInt("id_vacina"));
                vacina.setNomeVacina(rs.getString("nome_vacina"));
                vacina.setTipo(rs.getString("tipo"));
                return vacina;
            });
        } catch (Exception e) {
            System.err.println("Vacina não encontrada: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Vacina> listarTodos() {
        String sql = "SELECT * FROM Vacina";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Vacina vacina = new Vacina();
            vacina.setIdVacina(rs.getInt("id_vacina"));
            vacina.setNomeVacina(rs.getString("nome_vacina"));
            vacina.setTipo(rs.getString("tipo"));
            return vacina;
        });
    }
}

