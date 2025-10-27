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
 * A anotação @Repository informa ao Spring que esta é uma classe de acesso a dados.
 */
@Repository
public class PessoaDAO implements IPessoaDAO {

    private final JdbcTemplate jdbcTemplate;

    /**
     * O Spring Boot vai "injetar" (fornecer) o JdbcTemplate automaticamente.
     * Ele já vem configurado com a conexão do arquivo application.properties.
     */
    @Autowired
    public PessoaDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * A anotação @Transactional cuida de toda a lógica de transação
     * (commit e rollback) para nós.
     */
    @Override
    @Transactional
    public Pessoa salvar(Pessoa pessoa) {
        String sqlPessoa = "INSERT INTO Pessoa (nome, cpf, email) VALUES (?, ?, ?)";

        // KeyHolder é usado para pegar o ID auto-gerado pelo banco
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // 1. Salvar Pessoa
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlPessoa, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, pessoa.getNome());
            stmt.setString(2, pessoa.getCpf());
            stmt.setString(3, pessoa.getEmail());
            return stmt;
        }, keyHolder);

        // Pega o ID gerado e define na pessoa
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
        String sql = "UPDATE Pessoa SET nome = ?, cpf = ?, email = ? WHERE id_pessoa = ?";

        int affectedRows = jdbcTemplate.update(sql,
                pessoa.getNome(),
                pessoa.getCpf(),
                pessoa.getEmail(),
                pessoa.getIdPessoa());

        // Lógica de atualização de telefones (deletar antigos, inserir novos)
        // seria implementada aqui. Por enquanto, atualizamos só a pessoa.

        return affectedRows > 0;
    }

    /**
     * Graças ao 'ON DELETE CASCADE' no nosso SQL, só precisamos deletar
     * da tabela 'Pessoa'. O banco cuida do resto.
     */
    @Override
    public boolean deletar(int id) {
        String sql = "DELETE FROM Pessoa WHERE id_pessoa = ?";
        int affectedRows = jdbcTemplate.update(sql, id);
        return affectedRows > 0;
    }

    @Override
    public Pessoa buscarPorId(int id) {
        // Esta é uma implementação simplificada que não carrega especializações
        // ou telefones. Vamos criar um Controller para lidar com isso.
        String sql = "SELECT * FROM Pessoa WHERE id_pessoa = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                    new Pessoa(
                            rs.getInt("id_pessoa"),
                            rs.getString("nome"),
                            rs.getString("cpf"),
                            rs.getString("email")
                    )
            );
        } catch (Exception e) {
            System.err.println("Pessoa não encontrada: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Pessoa> listarTodos() {
        String sql = "SELECT * FROM Pessoa";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Pessoa(
                        rs.getInt("id_pessoa"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("email")
                )
        );
    }
}

