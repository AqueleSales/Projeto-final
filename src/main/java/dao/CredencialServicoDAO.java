package dao;

import conexãoBD.ConexaoSQL;
import conexãoBD.IConexao;
import model.CredencialServico;
import model.Habilidade;

import java.sql.*;
import java.util.ArrayList;

/**
 * Implementação da interface ICredencialServicoDAO.
 * Gerencia a persistência da CredencialServico e sua relação M:N
 * com a tabela Habilidade através da tabela Credencial_Habilidade.
 */
public class CredencialServicoDAO implements ICredencialServicoDAO {

    private final IConexao conexaoBD;

    public CredencialServicoDAO() {
        this.conexaoBD = new ConexaoSQL();
    }

    @Override
    public CredencialServico salvar(CredencialServico credencial) {
        String sqlCredencial = "INSERT INTO Credencial_Servico (data_emissao, data_validade, id_animal_servico, id_treinador) VALUES (?, ?, ?, ?)";
        String sqlJunction = "INSERT INTO Credencial_Habilidade (id_credencial, id_habilidade) VALUES (?, ?)";
        Connection conexao = null;

        try {
            conexao = conexaoBD.getConexao();
            conexao.setAutoCommit(false); // Inicia a transação

            // --- Passo 1: Salvar a Credencial_Servico ---
            int idCredencialGerada;
            try (PreparedStatement stmtCredencial = conexao.prepareStatement(sqlCredencial, Statement.RETURN_GENERATED_KEYS)) {
                stmtCredencial.setDate(1, Date.valueOf(credencial.getDataEmissao()));
                stmtCredencial.setDate(2, Date.valueOf(credencial.getDataValidade()));
                stmtCredencial.setInt(3, credencial.getIdAnimalServico());
                stmtCredencial.setInt(4, credencial.getIdTreinador());
                stmtCredencial.executeUpdate();

                try (ResultSet generatedKeys = stmtCredencial.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idCredencialGerada = generatedKeys.getInt(1);
                        credencial.setIdCredencial(idCredencialGerada);
                    } else {
                        throw new SQLException("Falha ao obter o ID da credencial.");
                    }
                }
            }

            // --- Passo 2: Salvar a relação M:N na tabela Credencial_Habilidade ---
            if (credencial.getHabilidades() != null && !credencial.getHabilidades().isEmpty()) {
                try (PreparedStatement stmtJunction = conexao.prepareStatement(sqlJunction)) {
                    for (Habilidade habilidade : credencial.getHabilidades()) {
                        stmtJunction.setInt(1, idCredencialGerada);
                        stmtJunction.setInt(2, habilidade.getIdHabilidade());
                        stmtJunction.addBatch();
                    }
                    stmtJunction.executeBatch();
                }
            }

            conexao.commit(); // Efetiva a transação
            System.out.println("Credencial e Habilidades associadas salvas com sucesso!");
            return credencial;

        } catch (SQLException e) {
            System.err.println("Erro ao salvar credencial (transação): " + e.getMessage());
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
    public CredencialServico buscarPorIdAnimal(int idAnimalServico) {
        // Query complexa que busca a credencial e junta todas as suas habilidades (M:N)
        String sql = "SELECT cs.*, h.id_habilidade, h.descricao_habilidade " +
                "FROM Credencial_Servico cs " +
                "LEFT JOIN Credencial_Habilidade ch ON cs.id_credencial = ch.id_credencial " +
                "LEFT JOIN Habilidade h ON ch.id_habilidade = h.id_habilidade " +
                "WHERE cs.id_animal_servico = ?";

        CredencialServico credencial = null;

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idAnimalServico);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Se a credencial ainda não foi criada, cria o objeto principal
                    if (credencial == null) {
                        credencial = new CredencialServico();
                        credencial.setIdCredencial(rs.getInt("id_credencial"));
                        credencial.setDataEmissao(rs.getDate("data_emissao").toLocalDate());
                        credencial.setDataValidade(rs.getDate("data_validade").toLocalDate());
                        credencial.setIdAnimalServico(rs.getInt("id_animal_servico"));
                        credencial.setIdTreinador(rs.getInt("id_treinador"));
                        credencial.setHabilidades(new ArrayList<>());
                    }

                    // Adiciona a habilidade (se existir)
                    int idHabilidade = rs.getInt("id_habilidade");
                    if (idHabilidade > 0) {
                        Habilidade habilidade = new Habilidade();
                        habilidade.setIdHabilidade(idHabilidade);
                        habilidade.setDescricaoHabilidade(rs.getString("descricao_habilidade"));
                        // Evitar duplicatas se a credencial não tiver habilidades
                        if (!credencial.getHabilidades().stream().anyMatch(h -> h.getIdHabilidade() == idHabilidade)) {
                            credencial.addHabilidade(habilidade);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar credencial por ID do animal: " + e.getMessage());
        } finally {
            conexaoBD.closeConexao();
        }
        return credencial;
    }

    @Override
    public boolean deletar(int idCredencial) {
        // Graças ao "ON DELETE CASCADE" na tabela Credencial_Habilidade,
        // só precisamos deletar o registro da Credencial_Servico.
        String sql = "DELETE FROM Credencial_Servico WHERE id_credencial = ?";

        try (Connection conexao = conexaoBD.getConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setInt(1, idCredencial);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao deletar credencial: " + e.getMessage());
            return false;
        } finally {
            conexaoBD.closeConexao();
        }
    }
}

