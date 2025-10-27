package dao;

import model.CredencialServico;
import model.Habilidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * Implementação do DAO para CredencialServico, usando Spring Boot e JdbcTemplate.
 * Gerencia a lógica transacional para a relação M:N com Habilidade.
 */
@Repository
public class CredencialServicoDAO implements ICredencialServicoDAO {

    private final JdbcTemplate jdbcTemplate;
    private final IHabilidadeDAO habilidadeDAO; // Dependência para buscar habilidades

    @Autowired
    public CredencialServicoDAO(JdbcTemplate jdbcTemplate, IHabilidadeDAO habilidadeDAO) {
        this.jdbcTemplate = jdbcTemplate;
        this.habilidadeDAO = habilidadeDAO;
    }

    @Override
    @Transactional
    public CredencialServico salvar(CredencialServico credencial) {
        String sqlCredencial = "INSERT INTO Credencial_Servico (data_emissao, data_validade, id_animal_servico, id_treinador) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // 1. Salvar a Credencial_Servico
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlCredencial, Statement.RETURN_GENERATED_KEYS);
            stmt.setDate(1, java.sql.Date.valueOf(credencial.getDataEmissao()));
            stmt.setDate(2, java.sql.Date.valueOf(credencial.getDataValidade()));
            stmt.setInt(3, credencial.getIdAnimalServico());
            stmt.setInt(4, credencial.getIdTreinador());
            return stmt;
        }, keyHolder);

        int idCredencial = keyHolder.getKey().intValue();
        credencial.setIdCredencial(idCredencial);

        // 2. Salvar a Relação M:N (Credencial_Habilidade)
        String sqlHabilidade = "INSERT INTO Credencial_Habilidade (id_credencial, id_habilidade) VALUES (?, ?)";
        if (credencial.getHabilidades() != null && !credencial.getHabilidades().isEmpty()) {
            for (Habilidade habilidade : credencial.getHabilidades()) {
                jdbcTemplate.update(sqlHabilidade, idCredencial, habilidade.getIdHabilidade());
            }
        }

        return credencial;
    }

    @Override
    public CredencialServico buscarPorId(int id) {
        String sql = "SELECT * FROM Credencial_Servico WHERE id_credencial = ?";
        try {
            CredencialServico credencial = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                CredencialServico cs = new CredencialServico();
                cs.setIdCredencial(rs.getInt("id_credencial"));
                cs.setDataEmissao(rs.getDate("data_emissao").toLocalDate());
                cs.setDataValidade(rs.getDate("data_validade").toLocalDate());
                cs.setIdAnimalServico(rs.getInt("id_animal_servico"));
                cs.setIdTreinador(rs.getInt("id_treinador"));
                return cs;
            });

            // Carregar as habilidades (Relação M:N)
            if (credencial != null) {
                carregarHabilidades(credencial);
            }
            return credencial;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public CredencialServico buscarPorAnimalId(int idAnimal) {
        String sql = "SELECT * FROM Credencial_Servico WHERE id_animal_servico = ?";
        try {
            CredencialServico credencial = jdbcTemplate.queryForObject(sql, new Object[]{idAnimal}, (rs, rowNum) -> {
                CredencialServico cs = new CredencialServico();
                cs.setIdCredencial(rs.getInt("id_credencial"));
                cs.setDataEmissao(rs.getDate("data_emissao").toLocalDate());
                cs.setDataValidade(rs.getDate("data_validade").toLocalDate());
                cs.setIdAnimalServico(rs.getInt("id_animal_servico"));
                cs.setIdTreinador(rs.getInt("id_treinador"));
                return cs;
            });

            // Carregar as habilidades (Relação M:N)
            if (credencial != null) {
                carregarHabilidades(credencial);
            }
            return credencial;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Método auxiliar para carregar a lista de habilidades (M:N)
     */
    private void carregarHabilidades(CredencialServico credencial) {
        String sql = "SELECT id_habilidade FROM Credencial_Habilidade WHERE id_credencial = ?";
        List<Integer> idsHabilidades = jdbcTemplate.queryForList(sql, new Object[]{credencial.getIdCredencial()}, Integer.class);

        for (int idHab : idsHabilidades) {
            Habilidade habilidade = habilidadeDAO.buscarPorId(idHab);
            if (habilidade != null) {
                credencial.addHabilidade(habilidade);
            }
        }
    }

    // --- MÉTODO QUE FALTAVA ---
    @Override
    @Transactional
    public boolean deletar(int id) {
        try {
            // 1. Deletar da tabela de junção M:N
            String sqlHabilidades = "DELETE FROM Credencial_Habilidade WHERE id_credencial = ?";
            jdbcTemplate.update(sqlHabilidades, id);

            // 2. Deletar da tabela principal
            String sqlCredencial = "DELETE FROM Credencial_Servico WHERE id_credencial = ?";
            int affectedRows = jdbcTemplate.update(sqlCredencial, id);

            return affectedRows > 0;
        } catch (Exception e) {
            System.err.println("Erro ao deletar credencial: " + e.getMessage());
            // A transação será revertida automaticamente pelo Spring em caso de exceção
            return false;
        }
    }
}

