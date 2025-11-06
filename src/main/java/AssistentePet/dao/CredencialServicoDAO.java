package AssistentePet.dao;

import AssistentePet.model.CredencialServico;
import AssistentePet.model.Habilidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CredencialServicoDAO implements ICredencialServicoDAO {

    private final JdbcTemplate jdbcTemplate;
    private final IHabilidadeDAO habilidadeDAO;
    private final IPetDAO petDAO;

    @Autowired
    public CredencialServicoDAO(JdbcTemplate jdbcTemplate, IHabilidadeDAO habilidadeDAO, IPetDAO petDAO) { // <-- PARÂMETRO ADICIONADO
        this.jdbcTemplate = jdbcTemplate;
        this.habilidadeDAO = habilidadeDAO;
        this.petDAO = petDAO;
    }

    @Override
    @Transactional
    public CredencialServico salvar(CredencialServico credencial) {

        petDAO.garantirPromocao(credencial.getIdAnimalServico());
        String sqlCredencial = "INSERT INTO Credencial_Servico (data_emissao, data_validade, id_animal_servico, id_treinador) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlCredencial, Statement.RETURN_GENERATED_KEYS);
            stmt.setDate(1, java.sql.Date.valueOf(credencial.getDataEmissao()));
            if (credencial.getDataValidade() != null) {
                stmt.setDate(2, java.sql.Date.valueOf(credencial.getDataValidade()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }
            stmt.setInt(3, credencial.getIdAnimalServico());
            stmt.setInt(4, credencial.getIdTreinador());
            return stmt;
        }, keyHolder);
        int idCredencial = keyHolder.getKey().intValue();
        credencial.setIdCredencial(idCredencial);
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
                // Correção para ler data nula
                java.sql.Date dataValidade = rs.getDate("data_validade");
                if(dataValidade != null) {
                    cs.setDataValidade(dataValidade.toLocalDate());
                }
                cs.setIdAnimalServico(rs.getInt("id_animal_servico"));
                cs.setIdTreinador(rs.getInt("id_treinador"));
                return cs;
            });
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
                java.sql.Date dataValidade = rs.getDate("data_validade");
                if(dataValidade != null) {
                    cs.setDataValidade(dataValidade.toLocalDate());
                }
                cs.setIdAnimalServico(rs.getInt("id_animal_servico"));
                cs.setIdTreinador(rs.getInt("id_treinador"));
                return cs;
            });
            if (credencial != null) {
                carregarHabilidades(credencial);
            }
            return credencial;
        } catch (Exception e) {
            return null;
        }
    }

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

    @Override
    @Transactional
    public boolean deletar(int id) {
        try {
            String sqlHabilidades = "DELETE FROM Credencial_Habilidade WHERE id_credencial = ?";
            jdbcTemplate.update(sqlHabilidades, id);
            String sqlCredencial = "DELETE FROM Credencial_Servico WHERE id_credencial = ?";
            int affectedRows = jdbcTemplate.update(sqlCredencial, id);
            return affectedRows > 0;
        } catch (Exception e) {
            System.err.println("Erro ao deletar credencial: " + e.getMessage());
            return false;
        }
    }
}