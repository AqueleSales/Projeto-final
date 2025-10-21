package conexãoBD;
import java.sql.*;
/**
 * Interface que define o contrato para classes de conexão com o banco de dados.
 * O uso de uma interface permite a fácil substituição da implementação do banco de dados
 * (por exemplo, trocar MySQL por PostgreSQL) sem impactar o resto da aplicação.
 */
public interface IConexao {

    /**
     * Estabelece e retorna uma conexão ativa com o banco de dados.
     *
     * @return um objeto Connection.
     * @throws SQLException se ocorrer um erro de acesso ao banco de dados.
     */
    Connection getConexao() throws SQLException;

    /**
     * Fecha a conexão ativa com o banco de dados.
     * É crucial chamar este metodo para liberar os recursos do banco de dados.
     */
    void closeConexao();
}

