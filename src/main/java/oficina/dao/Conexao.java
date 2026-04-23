package oficina.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {

    private static final String URL     = "jdbc:postgresql://localhost:5432/oficina_mecanica";
    private static final String USUARIO = "postgres";
    private static final String SENHA   = "32568879";

    public static Connection obter() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver PostgreSQL não encontrado. Adicione postgresql-jdbc ao classpath.", e);
        }
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public static void fechar(Connection conn) {
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}
