package AssistentePet.dao;

import AssistentePet.model.AnimalDeServico;
import AssistentePet.model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

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

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlPet, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getEspecie());
            stmt.setString(3, pet.getRaca());
            if (pet.getDataNascimento() != null) {
                stmt.setDate(4, java.sql.Date.valueOf(pet.getDataNascimento()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }
            return stmt;
        }, keyHolder);
        int idPet = keyHolder.getKey().intValue();
        pet.setIdPet(idPet);
        if (pet.getIdDonoTransporte() > 0) {
            String sqlPossui = "INSERT INTO Possui (id_dono, id_pet) VALUES (?, ?)";
            jdbcTemplate.update(sqlPossui, pet.getIdDonoTransporte(), idPet);
        } else {
            throw new RuntimeException("ID do Dono é inválido, não é possível salvar a relação.");
        }
        if (pet instanceof AnimalDeServico) {
            System.out.println("Salvando dados do Animal de Serviço (via 'salvar')...");
            String sqlAnimalServico = "INSERT INTO Animal_de_Servico (id_pet, numero_registro_oficial, status) VALUES (?, ?, ?)";
            AnimalDeServico as = (AnimalDeServico) pet;
            jdbcTemplate.update(sqlAnimalServico, idPet, as.getNumeroRegistroOficial(), as.getStatus());
        }
        System.out.println("Pet e relações salvas com sucesso!");
        return pet;
    }

    private RowMapper<Pet> petRowMapperCompleto() {
        return (rs, rowNum) -> {
            Object joinedIdPet = rs.getObject("joined_id_pet");
            Pet pet;
            if (joinedIdPet == null) {
                pet = new Pet();
            } else {
                AnimalDeServico animalServico = new AnimalDeServico();
                animalServico.setStatus(rs.getString("status"));
                animalServico.setNumeroRegistroOficial(rs.getString("numero_registro_oficial"));
                pet = animalServico;
            }
            pet.setIdPet(rs.getInt("id_pet"));
            pet.setNome(rs.getString("nome"));
            pet.setEspecie(rs.getString("especie"));
            pet.setRaca(rs.getString("raca"));
            java.sql.Date sqlDate = rs.getDate("data_nasc");
            if (sqlDate != null) {
                pet.setDataNascimento(sqlDate.toLocalDate());
            }
            return pet;
        };
    }

    private RowMapper<Pet> petRowMapperSimples() {
        return (rs, rowNum) -> {
            Pet pet = new Pet();
            pet.setIdPet(rs.getInt("id_pet"));
            pet.setNome(rs.getString("nome"));
            pet.setEspecie(rs.getString("especie"));
            pet.setRaca(rs.getString("raca"));
            java.sql.Date sqlDate = rs.getDate("data_nasc");
            if (sqlDate != null) {
                pet.setDataNascimento(sqlDate.toLocalDate());
            }
            return pet;
        };
    }

    @Override
    public List<Pet> listarPetsPorDono(int idDono) {
        String sql = "SELECT p.*, a.status, a.numero_registro_oficial, a.id_pet as joined_id_pet " +
                "FROM Pet p " +
                "JOIN Possui pos ON p.id_pet = pos.id_pet " +
                "LEFT JOIN Animal_de_Servico a ON p.id_pet = a.id_pet " +
                "WHERE pos.id_dono = ?";
        return jdbcTemplate.query(sql, new Object[]{idDono}, petRowMapperCompleto());
    }

    @Override
    public boolean atualizar(Pet pet) {
        String sql = "UPDATE Pet SET nome = ?, especie = ?, raca = ?, data_nasc = ? WHERE id_pet = ?";
        java.sql.Date sqlDate = null;
        if (pet.getDataNascimento() != null) {
            sqlDate = java.sql.Date.valueOf(pet.getDataNascimento());
        }
        int affectedRows = jdbcTemplate.update(sql,
                pet.getNome(),
                pet.getEspecie(),
                pet.getRaca(),
                sqlDate,
                pet.getIdPet()
        );
        return affectedRows > 0;
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM Pet WHERE id_pet = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    @Override
    public Pet buscarPorId(int id) {
        String sql = "SELECT p.*, a.status, a.numero_registro_oficial, a.id_pet as joined_id_pet " +
                "FROM Pet p " +
                "LEFT JOIN Animal_de_Servico a ON p.id_pet = a.id_pet " +
                "WHERE p.id_pet = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, petRowMapperCompleto());
        } catch (Exception e) {
            System.err.println("Erro ao buscar Pet por ID (com join): " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Pet> listarTodos() {
        String sql = "SELECT * FROM Pet";
        try {
            return jdbcTemplate.query(sql, petRowMapperSimples());
        } catch (Exception e) {
            return java.util.Collections.emptyList();
        }
    }

    @Override
    public boolean promoverParaAnimalDeServico(AnimalDeServico dadosAnimalServico) {
        String sqlCheck = "SELECT COUNT(*) FROM Animal_de_Servico WHERE id_pet = ?";
        Integer count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, dadosAnimalServico.getIdPet());

        if (count != null && count > 0) {
            System.out.println("Atualizando Animal de Serviço (via Dono/Treinador)...");
            String sqlUpdate = "UPDATE Animal_de_Servico SET numero_registro_oficial = ?, status = ? WHERE id_pet = ?";
            int affectedRows = jdbcTemplate.update(sqlUpdate,
                    dadosAnimalServico.getNumeroRegistroOficial(),
                    dadosAnimalServico.getStatus(),
                    dadosAnimalServico.getIdPet()
            );
            return affectedRows > 0;
        } else {
            System.out.println("Promovendo Pet para Animal de Serviço (via Dono/Treinador)...");
            String sqlInsert = "INSERT INTO Animal_de_Servico (id_pet, numero_registro_oficial, status) VALUES (?, ?, ?)";
            int affectedRows = jdbcTemplate.update(sqlInsert,
                    dadosAnimalServico.getIdPet(),
                    dadosAnimalServico.getNumeroRegistroOficial(),
                    dadosAnimalServico.getStatus()
            );
            return affectedRows > 0;
        }
    }

    @Override
    public void garantirPromocao(int idPet) {
        String sqlCheck = "SELECT COUNT(*) FROM Animal_de_Servico WHERE id_pet = ?";
        Integer count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, idPet);
        if (count == null || count == 0) {
            System.out.println("Promovendo Pet ID " + idPet + " para Animal de Serviço (via Treinador)...");
            String sqlInsert = "INSERT INTO Animal_de_Servico (id_pet, numero_registro_oficial, status) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlInsert, idPet, null, "Em Treinamento");
        }
    }

    @Override
    @Transactional
    public boolean demoverDeAnimalDeServico(int idPet) {
        System.out.println("Rebaixando Pet ID " + idPet + " de volta para Pet comum...");
        String sqlCredencial = "DELETE FROM Credencial_Servico WHERE id_animal_servico = ?";
        jdbcTemplate.update(sqlCredencial, idPet);
        String sql = "DELETE FROM Animal_de_Servico WHERE id_pet = ?";
        int affectedRows = jdbcTemplate.update(sql, idPet);

        return affectedRows > 0;
    }
}