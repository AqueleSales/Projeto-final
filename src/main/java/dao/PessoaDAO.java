package dao;

import model.Dono;
import model.Pessoa;
import model.Veterinario;
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
 * Implementação do DAO para Pessoa, agora usando Spring Boot e JdbcTemplate.
 * ATUALIZADO: Salva e busca o campo 'senha'.
 */
@Repository
public class PessoaDAO implements IPessoaDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PessoaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Salva a pessoa.
     * A SENHA já deve vir CRIPTOGRAFADA do Controller (DonoController).
     */
    @Override
    @Transactional
    public Pessoa salvar(Pessoa pessoa) {
        // --- ATUALIZADO ---
        String sqlPessoa = "INSERT INTO Pessoa (nome, cpf, email, senha) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        // 1. Salvar Pessoa
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getCpf());
            stmt.setString(3, pessoa.getEmail());
            // --- NOVO CAMPO DE SENHA ---
            stmt.setString(4, pessoa.getSenha());
            return stmt;
        }, keyHolder);

        int idPessoa = keyHolder.getKey().intValue();
        pessoa.setIdPessoa(idPessoa);

        // 2. Salvar Telefones
        String sqlTelefone = "INSERT INTO Pessoa_Telefone (id_pessoa, telefone) VALUES (?, ?)";
        if (pessoa.getTelefones() != null && !pessoa.getTelefones().isEmpty()) {
            for (String telefone : pessoa.getTelefones()) {
                jdbcTemplate.update(sqlTelefone, idPessoa, telefone);
            }
        }

        // 3. Salvar Especialização (Dono ou Veterinario)
        if (pessoa instanceof Dono) {
            String sqlDono = "INSERT INTO Dono (id_pessoa) VALUES (?)";
            jdbcTemplate.update(sqlDono, idPessoa);
            System.out.println("Registro de Dono salvo!");

        } else if (pessoa instanceof Veterinario) {
            String sqlVeterinario = "INSERT INTO Veterinario (id_pessoa, CRMV) VALUES (?, ?)";
            jdbcTemplate.update(sqlVeterinario, idPessoa, ((Veterinario) pessoa).getCrmv());
            System.out.println("Registro de Veterinario salvo!");
        }

        System.out.println("Pessoa e especialização salvos com sucesso!");
        return pessoa;
    }

    @Override
    public boolean atualizar(Pessoa pessoa) {
        // (Ignorando atualização de senha por simplicidade)
        String sql = "UPDATE Pessoa SET nome = ?, cpf = ?, email = ? WHERE id_pessoa = ?";

        int affectedRows = jdbcTemplate.update(sql,
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getEmail(),
                pessoa.getIdPessoa());

        return affectedRows > 0;
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM Pessoa WHERE id_pessoa = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    @Override
    public Pessoa buscarPorId(int id) {
        String sql = "SELECT * FROM Pessoa WHERE id_pessoa = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Pessoa p = new Pessoa(
                        rs.getInt("id_pessoa"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email")
                );
                p.setSenha(rs.getString("senha")); // Puxa a senha do banco
                return p;
            });
        } catch (Exception e) {
            System.err.println("Pessoa não encontrada: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Pessoa> listarTodos() {
        String sql = "SELECT * FROM Pessoa";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Pessoa p = new Pessoa(
                    rs.getInt("id_pessoa"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("email")
            );
            p.setSenha(rs.getString("senha")); // Puxa a senha do banco
            return p;
        });
    }

    // --- MÉTODO DE LOGIN ATUALIZADO ---
    @Override
    public Pessoa buscarPorEmail(String email) {
        String sql = "SELECT * FROM Pessoa WHERE email = ?";
        try {
            // RowMapper completo que também busca a senha
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) -> {
                Pessoa p = new Pessoa(
                        rs.getInt("id_pessoa"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email")
                );
                // --- IMPORTANTE ---
                p.setSenha(rs.getString("senha")); // Puxa a senha (criptografada) do banco
                return p;
            });
        } catch (Exception e) {
            System.err.println("Pessoa não encontrada com o e-mail: " + email);
            return null;
        }
    }
}