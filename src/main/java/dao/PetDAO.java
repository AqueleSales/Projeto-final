package dao;

// Corrigido para usar o pacote 'conexãoBD' e a classe 'ConexaoSQL' do seu projeto
import conexãoBD.ConexaoSQL;
import conexãoBD.IConexao;
import model.Pet;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do DAO para Pet (Versão Corrigida).
 * Esta versão lida corretamente com a tabela de junção 'Possui' (M:N)
 * e usa transações para garantir a integridade dos dados.
 */
public class PetDAO implements IPetDAO {

    private final IConexao conexaoBD;

    public PetDAO() {
        // Corrigido para instanciar a sua classe ConexaoSQL
        this.conexaoBD = new ConexaoSQL();
    }

    @Override
    public Pet salvar(Pet pet) {
        // Usamos duas instruções SQL: uma para Pet, uma para Possui
        // A tabela Pet NÃO tem 'id_dono'
        String sqlPet = "INSERT INTO Pet (nome, especie, raca, data_nasc) VALUES (?, ?, ?, ?)";
        String sqlPossui = "INSERT INTO Possui (id_dono, id_pet) VALUES (?, ?)";
        Connection conexao = null;

        try {
            conexao = conexaoBD.getConexao();
            // Iniciar transação
            conexao.setAutoCommit(false);

            // --- Passo 1: Salvar o Pet ---
            try (PreparedStatement stmtPet = conexao.prepareStatement(sqlPet, Statement.RETURN_GENERATED_KEYS)) {
                stmtPet.setString(1, pet.getNome());
                stmtPet.setString(2, pet.getEspecie());
                stmtPet.setString(3, pet.getRaca());
                stmtPet.setDate(4, Date.valueOf(pet.getDataNascimento()));

                stmtPet.executeUpdate();

                // Obter o ID gerado para o Pet
                try (ResultSet generatedKeys = stmtPet.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pet.setIdPet(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Falha ao obter o ID do pet.");
                    }
                }
            }

            // --- Passo 2: Salvar a ligação na tabela Possui ---
            // O pet.getIdDono() foi definido no main.java
            if (pet.getIdDono() > 0) {
                try (PreparedStatement stmtPossui = conexao.prepareStatement(sqlPossui)) {
                    stmtPossui.setInt(1, pet.getIdDono());
                    stmtPossui.setInt(2, pet.getIdPet());
                    stmtPossui.executeUpdate();
                }
            } else {
                throw new SQLException("ID do Dono é inválido, não é possível salvar a relação.");
            }

            // --- Passo 3: Efetivar a transação ---
            conexao.commit();
            System.out.println("Pet e relação Possui salvos com sucesso!");
            return pet;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar pet (transação): " + e.getMessage());
            // Reverter a transação em caso de erro
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
            // Restaurar auto-commit e fechar conexão
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
        // A atualização agora só mexe na tabela Pet.
        // Mudar o dono seria uma operação mais complexa (deletar/inserir em 'Possui')
        String sql = "UPDATE Pet SET nome = ?, especie = ?, raca = ?, data_nasc = ? WHERE id_pet = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getEspecie());
            stmt.setString(3, pet.getRaca());
            stmt.setDate(4, Date.valueOf(pet.getDataNascimento()));
            stmt.setInt(5, pet.getIdPet());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar pet: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }

    @Override
    public boolean deletar(int id) {
        // Precisamos deletar as relações primeiro (tabela Possui e outras)
        String sqlPossui = "DELETE FROM Possui WHERE id_pet = ?";
        // TODO: Adicionar DELETE para AnimalDeServico, CertificadoVacina, etc.
        String sqlPet = "DELETE FROM Pet WHERE id_pet = ?";
        Connection conexao = null;

        try {
            conexao = conexaoBD.getConexao();
            // Iniciar transação
            conexao.setAutoCommit(false);

            // --- Passo 1: Deletar da tabela Possui ---
            // (Idealmente, deletar de AnimalDeServico e CertificadoVacina primeiro)
            try (PreparedStatement stmtPossui = conexao.prepareStatement(sqlPossui)) {
                stmtPossui.setInt(1, id);
                stmtPossui.executeUpdate();
            }

            // --- Passo 2: Deletar da tabela Pet ---
            try (PreparedStatement stmtPet = conexao.prepareStatement(sqlPet)) {
                stmtPet.setInt(1, id);
                int affectedRows = stmtPet.executeUpdate();
                if (affectedRows == 0) {
                    System.out.println("Nenhum pet encontrado para deletar com ID: " + id);
                }
            }

            // --- Passo 3: Efetivar a transação ---
            conexao.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar pet (transação): " + e.getMessage());
            // Reverter a transação em caso de erro
            if (conexao != null) {
                try {
                    conexao.rollback();
                    System.err.println("Transação revertida.");
                } catch (SQLException ex) {
                    System.err.println("Erro ao reverter a transação: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            // Restaurar auto-commit e fechar conexão
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
    public Pet buscarPorId(int id) {
        // Busca o Pet e o ID do seu dono na tabela Possui
        String sql = "SELECT p.*, po.id_dono " +
                "FROM Pet p " +
                "LEFT JOIN Possui po ON p.id_pet = po.id_pet " +
                "WHERE p.id_pet = ?";
        Pet pet = null;

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    pet = new Pet();
                    pet.setIdPet(rs.getInt("id_pet"));
                    pet.setNome(rs.getString("nome"));
                    pet.setEspecie(rs.getString("especie"));
                    pet.setRaca(rs.getString("raca"));
                    pet.setDataNascimento(rs.getDate("data_nasc").toLocalDate());
                    pet.setIdDono(rs.getInt("id_dono")); // Pega o ID da tabela Possui
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
        // Busca todos os pets de um dono específico
        String sql = "SELECT p.* " +
                "FROM Pet p " +
                "JOIN Possui po ON p.id_pet = po.id_pet " +
                "WHERE po.id_dono = ?";
        List<Pet> pets = new ArrayList<>();

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idDono);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pet pet = new Pet();
                    pet.setIdPet(rs.getInt("id_pet"));
                    pet.setNome(rs.getString("nome"));
                    pet.setEspecie(rs.getString("especie"));
                    pet.setRaca(rs.getString("raca"));
                    pet.setDataNascimento(rs.getDate("data_nasc").toLocalDate());
                    pet.setIdDono(idDono); // Nós já sabemos o ID do dono
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
        // Lista todos os pets, mas sem os donos
        String sql = "SELECT * FROM Pet";
        List<Pet> pets = new ArrayList<>();

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pet pet = new Pet();
                pet.setIdPet(rs.getInt("id_pet"));
                pet.setNome(rs.getString("nome"));
                pet.setEspecie(rs.getString("especie"));
                pet.setRaca(rs.getString("raca"));
                pet.setDataNascimento(rs.getDate("data_nasc").toLocalDate());
                // pet.setIdDono(0); // Não sabemos o dono aqui
                pets.add(pet);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os pets: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return pets;
    }
}

