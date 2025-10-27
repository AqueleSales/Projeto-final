package dao;

import conexãoBD.ConexaoSQL;
import conexãoBD.IConexao;
import model.AnimalDeServico;
import model.Pet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação da interface IPetDAO para interagir com o banco de dados MySQL.
 * Esta classe contém a lógica SQL para as operações de CRUD da entidade Pet
 * e sua especialização AnimalDeServico.
 */
public class PetDAO implements IPetDAO {

    private final IConexao conexaoBD;
    private static final String SQL_SELECT_PETS =
            "SELECT p.*, ads.numero_registro_oficial, ads.status " +
                    "FROM Pet p " +
                    "LEFT JOIN Animal_de_Servico ads ON p.id_pet = ads.id_pet";

    public PetDAO() {
        this.conexaoBD = new ConexaoSQL();
    }

    @Override
    public Pet salvar(Pet pet) {
        String sqlPet = "INSERT INTO Pet (nome, especie, raca, data_nasc) VALUES (?, ?, ?, ?)";
        String sqlPossui = "INSERT INTO Possui (id_dono, id_pet) VALUES (?, ?)";
        String sqlAnimalServico = "INSERT INTO Animal_de_Servico (id_pet, numero_registro_oficial, status) VALUES (?, ?, ?)";
        Connection conexao = null;

        try {
            conexao = conexaoBD.getConexao();
            conexao.setAutoCommit(false); // Inicia a transação

            // --- Passo 1: Salvar o Pet ---
            int idPetGerado;
            try (PreparedStatement stmtPet = conexao.prepareStatement(sqlPet, Statement.RETURN_GENERATED_KEYS)) {
                stmtPet.setString(1, pet.getNome());
                stmtPet.setString(2, pet.getEspecie());
                stmtPet.setString(3, pet.getRaca());
                stmtPet.setDate(4, Date.valueOf(pet.getDataNascimento()));
                stmtPet.executeUpdate();

                try (ResultSet generatedKeys = stmtPet.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idPetGerado = generatedKeys.getInt(1);
                        pet.setIdPet(idPetGerado);
                    } else {
                        throw new SQLException("Falha ao obter o ID do pet.");
                    }
                }
            }

            // --- Passo 2: Salvar a ligação na tabela Possui ---
            if (pet.getIdDonoTransporte() > 0) {
                try (PreparedStatement stmtPossui = conexao.prepareStatement(sqlPossui)) {
                    stmtPossui.setInt(1, pet.getIdDonoTransporte());
                    stmtPossui.setInt(2, idPetGerado);
                    stmtPossui.executeUpdate();
                }
            } else {
                throw new SQLException("ID do Dono é inválido, não é possível salvar a relação.");
            }

            // --- Passo 3: Salvar a especialização (se for Animal de Serviço) ---
            if (pet instanceof AnimalDeServico) {
                System.out.println("Salvando dados do Animal de Serviço...");
                AnimalDeServico animal = (AnimalDeServico) pet;
                try (PreparedStatement stmtAnimalServico = conexao.prepareStatement(sqlAnimalServico)) {
                    stmtAnimalServico.setInt(1, idPetGerado);
                    stmtAnimalServico.setString(2, animal.getNumeroRegistroOficial());
                    stmtAnimalServico.setString(3, animal.getStatus());
                    stmtAnimalServico.executeUpdate();
                }
            }

            conexao.commit(); // Efetiva a transação
            System.out.println("Pet e relações salvas com sucesso!");
            return pet;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar pet (transação): " + e.getMessage());
            if (conexao != null) {
                try {
                    conexao.rollback();
                    System.err.println("Transação revertida.");
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter a transação: " + ex.getMessage());
                }
            }
            return null;
        } finally {
            if (conexao != null) {
                try {
                    conexao.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Erro ao restaurar auto-commit: " + e.getMessage());
                }
            }
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean atualizar(Pet pet) {
        String sqlPet = "UPDATE Pet SET nome = ?, especie = ?, raca = ?, data_nasc = ? WHERE id_pet = ?";
        String sqlAnimalServico = "UPDATE Animal_de_Servico SET numero_registro_oficial = ?, status = ? WHERE id_pet = ?";
        Connection conexao = null;
        try {
            conexao = conexaoBD.getConexao();
            conexao.setAutoCommit(false); // Transação

            // Atualiza Pet
            try (PreparedStatement stmtPet = conexao.prepareStatement(sqlPet)) {
                stmtPet.setString(1, pet.getNome());
                stmtPet.setString(2, pet.getEspecie());
                stmtPet.setString(3, pet.getRaca());
                stmtPet.setDate(4, Date.valueOf(pet.getDataNascimento()));
                stmtPet.setInt(5, pet.getIdPet());
                stmtPet.executeUpdate();
            }

            // Atualiza AnimalDeServico (se for)
            if (pet instanceof AnimalDeServico) {
                AnimalDeServico animal = (AnimalDeServico) pet;
                try (PreparedStatement stmtAnimalServico = conexao.prepareStatement(sqlAnimalServico)) {
                    stmtAnimalServico.setString(1, animal.getNumeroRegistroOficial());
                    stmtAnimalServico.setString(2, animal.getStatus());
                    stmtAnimalServico.setInt(3, animal.getIdPet());
                    stmtAnimalServico.executeUpdate();
                }
            }

            // Nota: a relação Possui (id_dono) não está sendo atualizada por simplicidade.

            conexao.commit();
            System.out.println("Pet atualizado com sucesso!");
            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pet: " + e.getMessage());
            if (conexao != null) {
                try {
                    conexao.rollback();
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter transação: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conexao != null) {
                try {
                    conexao.setAutoCommit(true);
                } catch (SQLException e) {
                    System.err.println("Erro ao restaurar auto-commit: " + e.getMessage());
                }
            }
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean deletar(int id) {
        // Graças ao "ON DELETE CASCADE" no banco de dados:
        // 1. Deletar um Pet irá deletar o registro em Animal_de_Servico.
        // 2. Deletar um Pet irá deletar o registro em Possui.
        // 3. Deletar um Pet irá deletar os registros em CertificadoVacina.
        // 4. Deletar um AnimalDeServico (id_pet) irá deletar os registros em Credencial_Servico
        //    (e este irá deletar Credencial_Habilidade).

        // Portanto, SÓ precisamos deletar da tabela Pet.
        String sql = "DELETE FROM Pet WHERE id_pet = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar pet: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public Pet buscarPorId(int id) {
        String sql = SQL_SELECT_PETS + " WHERE p.id_pet = ?";
        Pet pet = null;

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pet = instanciarPet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar pet por ID: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return pet;
    }

    @Override
    public List<Pet> listarPorDono(int idDono) {
        // Precisamos de um JOIN com a tabela Possui
        String sql = SQL_SELECT_PETS + " JOIN Possui pos ON p.id_pet = pos.id_pet WHERE pos.id_dono = ?";
        List<Pet> pets = new ArrayList<>();

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idDono);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pet pet = instanciarPet(rs);
                    pet.setIdDonoTransporte(idDono); // Seta o ID do dono para transporte
                    pets.add(pet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar pets por dono: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return pets;
    }

    @Override
    public List<Pet> listarTodos() {
        String sql = SQL_SELECT_PETS;
        List<Pet> pets = new ArrayList<>();

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                pets.add(instanciarPet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os pets: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return pets;
    }

    /**
     * Método utilitário privado para instanciar o objeto Pet ou AnimalDeServico
     * com base nos dados do ResultSet (que já contém o LEFT JOIN).
     */
    private Pet instanciarPet(ResultSet rs) throws SQLException {
        Pet pet;
        String numeroRegistro = rs.getString("numero_registro_oficial");

        if (numeroRegistro != null) {
            // É um Animal de Serviço
            AnimalDeServico animal = new AnimalDeServico();
            animal.setNumeroRegistroOficial(numeroRegistro);
            animal.setStatus(rs.getString("status"));
            pet = animal;
        } else {
            // É um Pet comum
            pet = new Pet();
        }

        // Popula os dados básicos do Pet
        pet.setIdPet(rs.getInt("id_pet"));
        pet.setNome(rs.getString("nome"));
        pet.setEspecie(rs.getString("especie"));
        pet.setRaca(rs.getString("raca"));
        pet.setDataNascimento(rs.getDate("data_nasc").toLocalDate());

        // Nota: O idDono não está sendo populado aqui pois exigiria outro JOIN
        // Estamos usando o 'idDonoTransporte' para isso.

        return pet;
    }
}

