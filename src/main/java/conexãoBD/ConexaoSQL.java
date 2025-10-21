package conexãoBD;
import java.sql.*;
/**
 * Implementação da interface IConexao para o banco de dados MySQL.
 * Esta classe gerencia os detalhes específicos da conexão JDBC para o MySQL.
 */
public class ConexaoSQL implements IConexao {

    // Constantes para as informações de conexão.
    // Lembre-se de NUNCA colocar senhas diretamente no código em um projeto real.
    // O ideal é usar variáveis de ambiente ou um arquivo de configuração seguro.
    private static final String URL = "jdbc:mysql://localhost:3306/MeuAssistentePet";
    private static final String USUARIO = "root"; // Altere para o seu usuário do MySQL
    private static final String SENHA = "S@les1507";   // Altere para a sua senha do MySQL

    private Connection conexao = null;

    /**
     * Tenta estabelecer uma conexão com o banco de dados MySQL.
     * Se a conexão já existir, a reutiliza.
     *
     * @return um objeto Connection.
     * @throws SQLException se houver falha ao conectar.
     */
    @Override
    public Connection getConexao() throws SQLException {
        try {
            // Verifica se a conexão não existe ou está fechada
            if (conexao == null || conexao.isClosed()) {
                // Carrega o driver JDBC do MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Estabelece a conexão
                conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC do MySQL não encontrado!");
            // Lança uma SQLException para que a camada superior possa tratar o erro
            throw new SQLException("Driver não encontrado", e);
        } catch (SQLException e) {
            System.err.println("Falha ao conectar ao banco de dados: " + e.getMessage());
            // Relança a exceção para que o chamador saiba que a conexão falhou
            throw e;
        }
        return conexao;
    }

    /**
     * Fecha a conexão com o banco de dados se ela estiver aberta.
     * É fundamental chamar este metodo após concluir as operações no banco de dados
     * para liberar recursos.
     */
    @Override
    public void closeConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("Conexão com o banco de dados fechada com sucesso.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar a conexão com o banco de dados: " + e.getMessage());
        } finally {
            conexao = null;
        }
    }
}

