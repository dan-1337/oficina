package oficina.dao;

import oficina.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void inserir(Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente (nome, cpf, telefone, email, endereco) VALUES (?,?,?,?,?)";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_cliente"})) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getTelefone());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getEndereco());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getInt(1));
            }
        }
    }

    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY nome";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Cliente> buscarPorNome(String nome) throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nome LIKE ? ORDER BY nome";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void atualizar(Cliente c) throws SQLException {
        String sql = "UPDATE cliente SET nome=?, cpf=?, telefone=?, email=?, endereco=? WHERE id_cliente=?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getTelefone());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getEndereco());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id_cliente = ?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id_cliente"));
        c.setNome(rs.getString("nome"));
        c.setCpf(rs.getString("cpf"));
        c.setTelefone(rs.getString("telefone"));
        c.setEmail(rs.getString("email"));
        c.setEndereco(rs.getString("endereco"));
        return c;
    }
}
