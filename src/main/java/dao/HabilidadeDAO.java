package dao;

import model.Habilidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Implementação do DAO para Habilidade, agora usando Spring Boot e JdbcTemplate.
 */
@Repository
public class HabilidadeDAO implements IHabilidadeDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public HabilidadeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Habilidade salvar(Habilidade habilidade) {
        String sql = "INSERT INTO Habilidade (descricao_habilidade) VALUES (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, habilidade.getDescricaoHabilidade());
            return stmt;
        }, keyHolder);

        habilidade.setIdHabilidade(keyHolder.getKey().intValue());
        System.out.println("Habilidade salva com sucesso!");
        return habilidade;
    }

    @Override
    public boolean atualizar(Habilidade habilidade) {
        String sql = "UPDATE Habilidade SET descricao_habilidade = ? WHERE id_habilidade = ?";

        int affectedRows = jdbcTemplate.update(sql,
                habilidade.getDescricaoHabilidade(),
                habilidade.getIdHabilidade()
        );

        return affectedRows > 0;
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM Habilidade WHERE id_habilidade = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    // Helper (RowMapper) para converter o ResultSet em objeto Habilidade
    private Habilidade mapRowToHabilidade(ResultSet rs, int rowNum) throws SQLException {
        Habilidade habilidade = new Habilidade();
        habilidade.setIdHabilidade(rs.getInt("id_habilidade"));
        habilidade.setDescricaoHabilidade(rs.getString("descricao_habilidade"));
        return habilidade;
    }

    @Override
    public Habilidade buscarPorId(int id) {
        String sql = "SELECT * FROM Habilidade WHERE id_habilidade = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, this::mapRowToHabilidade);
        } catch (Exception e) {
            System.err.println("Habilidade não encontrada: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Habilidade> listarTodos() {
        String sql = "SELECT * FROM Habilidade";
        try {
            return jdbcTemplate.query(sql, this::mapRowToHabilidade);
        } catch (Exception e) {
            System.err.println("Erro ao listar habilidades: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
}

