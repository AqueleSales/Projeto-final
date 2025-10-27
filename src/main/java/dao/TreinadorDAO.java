package dao;

import model.Treinador;
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
 * Implementação do DAO para Treinador, agora usando Spring Boot e JdbcTemplate.
 */
@Repository
public class TreinadorDAO implements ITreinadorDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TreinadorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Treinador salvar(Treinador treinador) {
        String sql = "INSERT INTO Treinador (nome, cpf, numero_certificacao_profissional) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, treinador.getNome());
            stmt.setString(2, treinador.getCpf());
            stmt.setString(3, treinador.getNumeroCertificacaoProfissional());
            return stmt;
        }, keyHolder);

        treinador.setIdTreinador(keyHolder.getKey().intValue());
        System.out.println("Treinador salvo com sucesso!");
        return treinador;
    }

    @Override
    public boolean atualizar(Treinador treinador) {
        String sql = "UPDATE Treinador SET nome = ?, cpf = ?, numero_certificacao_profissional = ? WHERE id_treinador = ?";

        int affectedRows = jdbcTemplate.update(sql,
                treinador.getNome(),
                treinador.getCpf(),
                treinador.getNumeroCertificacaoProfissional(),
                treinador.getIdTreinador()
        );

        return affectedRows > 0;
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM Treinador WHERE id_treinador = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    // Helper (RowMapper) para converter o ResultSet em objeto Treinador
    private Treinador mapRowToTreinador(ResultSet rs, int rowNum) throws SQLException {
        Treinador treinador = new Treinador();
        treinador.setIdTreinador(rs.getInt("id_treinador"));
        treinador.setNome(rs.getString("nome"));
        treinador.setCpf(rs.getString("cpf"));
        treinador.setNumeroCertificacaoProfissional(rs.getString("numero_certificacao_profissional"));
        return treinador;
    }

    @Override
    public Treinador buscarPorId(int id) {
        String sql = "SELECT * FROM Treinador WHERE id_treinador = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, this::mapRowToTreinador);
        } catch (Exception e) {
            System.err.println("Treinador não encontrado: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Treinador> listarTodos() {
        String sql = "SELECT * FROM Treinador";
        try {
            return jdbcTemplate.query(sql, this::mapRowToTreinador);
        } catch (Exception e) {
            System.err.println("Erro ao listar treinadores: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
}

