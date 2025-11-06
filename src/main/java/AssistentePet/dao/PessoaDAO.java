package AssistentePet.dao;

import AssistentePet.model.Dono;
import AssistentePet.model.Pessoa;
import AssistentePet.model.Treinador;
import AssistentePet.model.Veterinario;
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
public class PessoaDAO implements IPessoaDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PessoaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Pessoa salvar(Pessoa pessoa) {

        String sqlPessoa = "INSERT INTO Pessoa (nome, cpf, email, senha) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getCpf());
            stmt.setString(3, pessoa.getEmail());
            stmt.setString(4, pessoa.getSenha());
            return stmt;
        }, keyHolder);
        int idPessoa = keyHolder.getKey().intValue();
        pessoa.setIdPessoa(idPessoa);
        String sqlTelefone = "INSERT INTO Pessoa_Telefone (id_pessoa, telefone) VALUES (?, ?)";
        if (pessoa.getTelefones() != null && !pessoa.getTelefones().isEmpty()) {
            for (String telefone : pessoa.getTelefones()) {
                jdbcTemplate.update(sqlTelefone, idPessoa, telefone);
            }
        }

        if (pessoa instanceof Dono) {
            String sqlDono = "INSERT INTO Dono (id_pessoa) VALUES (?)";
            jdbcTemplate.update(sqlDono, idPessoa);
            System.out.println("Registro de Dono salvo!");

        } else if (pessoa instanceof Veterinario) {
            String sqlVeterinario = "INSERT INTO Veterinario (id_pessoa, CRMV) VALUES (?, ?)";
            jdbcTemplate.update(sqlVeterinario, idPessoa, ((Veterinario) pessoa).getCrmv());
            System.out.println("Registro de Veterinario salvo!");

        } else if (pessoa instanceof Treinador) {
            String sqlTreinador = "INSERT INTO Treinador (id_pessoa, numero_certificacao_profissional) VALUES (?, ?)";
            jdbcTemplate.update(sqlTreinador, idPessoa, ((Treinador) pessoa).getNumeroCertificacaoProfissional());
            System.out.println("Registro de Treinador salvo!");
        }
        System.out.println("Pessoa e especialização salvos com sucesso!");
        return pessoa;
    }

    @Override
    @Transactional
    public boolean atualizar(Pessoa pessoa) {

        String sqlPessoa = "UPDATE Pessoa SET nome = ?, email = ? WHERE id_pessoa = ?";
        int affectedRows = jdbcTemplate.update(sqlPessoa,
                pessoa.getNome(),
                pessoa.getEmail(),
                pessoa.getIdPessoa());
        if (affectedRows == 0) {
            return false;
        }
        String sqlDeletePhones = "DELETE FROM Pessoa_Telefone WHERE id_pessoa = ?";
        jdbcTemplate.update(sqlDeletePhones, pessoa.getIdPessoa());
        String sqlInsertPhone = "INSERT INTO Pessoa_Telefone (id_pessoa, telefone) VALUES (?, ?)";
        if (pessoa.getTelefones() != null && !pessoa.getTelefones().isEmpty()) {
            for (String telefone : pessoa.getTelefones()) {
                if(telefone != null && !telefone.trim().isEmpty()) {
                    jdbcTemplate.update(sqlInsertPhone, pessoa.getIdPessoa(), telefone);
                }
            }
        }
        return true;
    }

    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM Pessoa WHERE id_pessoa = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }
    private void carregarTelefones(Pessoa p) {
        if (p == null) return;
        String sql = "SELECT telefone FROM Pessoa_Telefone WHERE id_pessoa = ?";
        List<String> telefones = jdbcTemplate.queryForList(sql, String.class, p.getIdPessoa());
        p.setTelefones(telefones);
    }
    @Override
    public Pessoa buscarPorId(int id) {
        String sql = "SELECT * FROM Pessoa WHERE id_pessoa = ?";
        try {
            Pessoa p = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
                Pessoa pessoa = new Pessoa(
                        rs.getInt("id_pessoa"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email")
                );
                pessoa.setSenha(rs.getString("senha")); // Puxa a senha do banco
                return pessoa;
            });
            carregarTelefones(p);
            return p;
        } catch (Exception e) {
            System.err.println("Pessoa não encontrada: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Pessoa> listarTodos() {
        String sql = "SELECT * FROM Pessoa";

        List<Pessoa> pessoas = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Pessoa p = new Pessoa(
                    rs.getInt("id_pessoa"),
                    rs.getString("nome"),
                    rs.getString("cpf"),
                    rs.getString("email")
            );
            p.setSenha(rs.getString("senha")); // Puxa a senha do banco
            return p;
        });
        pessoas.forEach(this::carregarTelefones); // <-- LINHA ADICIONADA
        return pessoas;
    }

    @Override
    public Pessoa buscarPorEmail(String email) {
        String sql = "SELECT * FROM Pessoa WHERE email = ?";
        try {
            // RowMapper completo que também busca a senha
            Pessoa p = jdbcTemplate.queryForObject(sql, new Object[]{email}, (rs, rowNum) -> {
                Pessoa pessoa = new Pessoa(
                        rs.getInt("id_pessoa"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email")
                );
                // --- IMPORTANTE ---
                pessoa.setSenha(rs.getString("senha")); // Puxa a senha (criptografada)
                return pessoa;
            });
            carregarTelefones(p);
            if (p != null) {
                String sqlDono = "SELECT COUNT(*) FROM Dono WHERE id_pessoa = ?";
                int countDono = jdbcTemplate.queryForObject(sqlDono, Integer.class, p.getIdPessoa());
                if (countDono > 0) {
                    p.setRole("DONO");
                    return p;
                }
                String sqlVet = "SELECT COUNT(*) FROM Veterinario WHERE id_pessoa = ?";
                int countVet = jdbcTemplate.queryForObject(sqlVet, Integer.class, p.getIdPessoa());
                if (countVet > 0) {
                    p.setRole("VETERINARIO");
                    return p;
                }
                String sqlTreinador = "SELECT COUNT(*) FROM Treinador WHERE id_pessoa = ?";
                int countTreinador = jdbcTemplate.queryForObject(sqlTreinador, Integer.class, p.getIdPessoa());
                if (countTreinador > 0) {
                    p.setRole("TREINADOR");
                    return p;
                }
            }
            return p;
        } catch (Exception e) {
            System.err.println("Pessoa não encontrada com o e-mail: " + email);
            return null;
        }
    }
    @Override
    public Pessoa buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM Pessoa WHERE cpf = ?";
        try {
            Pessoa p = jdbcTemplate.queryForObject(sql, new Object[]{cpf}, (rs, rowNum) -> {
                Pessoa pessoa = new Pessoa(
                        rs.getInt("id_pessoa"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email")
                );
                pessoa.setSenha(rs.getString("senha"));
                return pessoa;
            });
            carregarTelefones(p); // <-- LINHA ADICIONADA
            return p;
        } catch (Exception e) {
            System.err.println("Pessoa não encontrada com o CPF: " + cpf);
            return null;
        }
    }
}