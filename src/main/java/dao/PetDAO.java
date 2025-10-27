package dao;

import model.AnimalDeServico;
import model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Implementação do DAO para Pet, agora usando Spring Boot e JdbcTemplate.
 */
@Repository
public class PetDAO implements IPetDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PetDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Pet salvar(Pet pet) {
        String sqlPet = "INSERT INTO Pet (nome, especie, raca, data_nasc) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // 1. Salvar o Pet
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlPet, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getEspecie());
            stmt.setString(3, pet.getRaca());
            stmt.setDate(4, java.sql.Date.valueOf(pet.getDataNascimento()));
            return stmt;
        }, keyHolder);

        int idPet = keyHolder.getKey().intValue();
        pet.setIdPet(idPet);

        // 2. Salvar a ligação na tabela Possui
        // Usamos o campo 'idDonoTransporte' que definimos no modelo
        if (pet.getIdDonoTransporte() > 0) {
            String sqlPossui = "INSERT INTO Possui (id_dono, id_pet) VALUES (?, ?)";
            jdbcTemplate.update(sqlPossui, pet.getIdDonoTransporte(), idPet);
        } else {
            // Se não foi passado um Dono, é um erro na lógica de negócio
            throw new RuntimeException("ID do Dono é inválido, não é possível salvar a relação.");
        }

        // 3. Salvar Especialização (AnimalDeServico)
        if (pet instanceof AnimalDeServico) {
            System.out.println("Salvando dados do Animal de Serviço...");
            String sqlAnimalServico = "INSERT INTO Animal_de_Servico (id_pet, numero_registro_oficial, status) VALUES (?, ?, ?)";
            AnimalDeServico as = (AnimalDeServico) pet;
            jdbcTemplate.update(sqlAnimalServico, idPet, as.getNumeroRegistroOficial(), as.getStatus());
        }

        System.out.println("Pet e relações salvas com sucesso!");
        return pet;
    }

    @Override
    public boolean atualizar(Pet pet) {
        String sql = "UPDATE Pet SET nome = ?, especie = ?, raca = ?, data_nasc = ? WHERE id_pet = ?";

        int affectedRows = jdbcTemplate.update(sql,
                pet.getNome(),
                pet.getEspecie(),
                pet.getRaca(),
                pet.getDataNascimento(),
                pet.getIdPet()
        );

        // TODO: Adicionar lógica para atualizar AnimalDeServico

        return affectedRows > 0;
    }

    @Override
    public boolean deletar(int id) {
        // Graças ao 'ON DELETE CASCADE', só precisamos deletar da tabela Pet.
        String sql = "DELETE FROM Pet WHERE id_pet = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    /**
     * Esta classe interna (RowMapper) ensina o Spring a converter
     * uma linha do ResultSet (do banco) em um objeto Pet ou AnimalDeServico.
     */
    private static final class PetRowMapper implements RowMapper<Pet> {
        @Override
        public Pet mapRow(ResultSet rs, int rowNum) throws SQLException {
            // Primeiro, verifica se este Pet é um Animal de Serviço
            String sqlCheckServico = "SELECT numero_registro_oficial, status FROM Animal_de_Servico WHERE id_pet = ?";

            Pet pet;
            try (PreparedStatement stmtCheck = rs.getStatement().getConnection().prepareStatement(sqlCheckServico)) {
                stmtCheck.setInt(1, rs.getInt("p.id_pet"));
                try (ResultSet rsServico = stmtCheck.executeQuery()) {
                    if (rsServico.next()) {
                        // É um Animal de Serviço
                        AnimalDeServico as = new AnimalDeServico();
                        as.setNumeroRegistroOficial(rsServico.getString("numero_registro_oficial"));
                        as.setStatus(rsServico.getString("status"));
                        pet = as;
                    } else {
                        // É um Pet Comum
                        pet = new Pet();
                    }
                }
            }

            // Popula os dados comuns do Pet
            pet.setIdPet(rs.getInt("p.id_pet"));
            pet.setNome(rs.getString("p.nome"));
            pet.setEspecie(rs.getString("p.especie"));
            pet.setRaca(rs.getString("p.raca"));
            pet.setDataNascimento(rs.getDate("p.data_nasc").toLocalDate());

            // Pega o ID do Dono da tabela Possui
            pet.setIdDonoTransporte(rs.getInt("po.id_dono"));

            return pet;
        }
    }

    @Override
    public Pet buscarPorId(int id) {
        // SQL complexo que junta Pet (p) e Possui (po)
        String sql = "SELECT p.*, po.id_dono " +
                "FROM Pet p " +
                "JOIN Possui po ON p.id_pet = po.id_pet " +
                "WHERE p.id_pet = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new PetRowMapper());
        } catch (Exception e) {
            System.err.println("Pet não encontrado: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Pet> listarPorDono(int idDono) {
        String sql = "SELECT p.*, po.id_dono " +
                "FROM Pet p " +
                "JOIN Possui po ON p.id_pet = po.id_pet " +
                "WHERE po.id_dono = ?";

        return jdbcTemplate.query(sql, new Object[]{idDono}, new PetRowMapper());
    }

    @Override
    public List<Pet> listarTodos() {
        String sql = "SELECT p.*, po.id_dono " +
                "FROM Pet p " +
                "JOIN Possui po ON p.id_pet = po.id_pet";

        return jdbcTemplate.query(sql, new PetRowMapper());
    }
}

