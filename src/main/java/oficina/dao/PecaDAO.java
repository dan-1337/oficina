package oficina.dao;

import oficina.model.Peca;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PecaDAO {

    public void inserir(Peca p) throws SQLException {
        String sql = "INSERT INTO peca (nome, codigo, fornecedor, preco_unitario, estoque) VALUES (?,?,?,?,?)";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_peca"})) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCodigo());
            ps.setString(3, p.getFornecedor());
            ps.setDouble(4, p.getPrecoUnitario());
            ps.setInt(5, p.getEstoque());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        }
    }

    public Peca buscarPorId(int id) throws SQLException {
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM peca WHERE id_peca = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public List<Peca> listarTodos() throws SQLException {
        List<Peca> lista = new ArrayList<>();
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM peca ORDER BY nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public void atualizar(Peca p) throws SQLException {
        String sql = "UPDATE peca SET nome=?, codigo=?, fornecedor=?, preco_unitario=?, estoque=? WHERE id_peca=?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNome());
            ps.setString(2, p.getCodigo());
            ps.setString(3, p.getFornecedor());
            ps.setDouble(4, p.getPrecoUnitario());
            ps.setInt(5, p.getEstoque());
            ps.setInt(6, p.getId());
            ps.executeUpdate();
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM peca WHERE id_peca = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public void atualizarEstoque(int id, int quantidade) throws SQLException {
        String sql = "UPDATE peca SET estoque = estoque - ? WHERE id_peca = ?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantidade);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    private Peca mapear(ResultSet rs) throws SQLException {
        Peca p = new Peca();
        p.setId(rs.getInt("id_peca"));
        p.setNome(rs.getString("nome"));
        p.setCodigo(rs.getString("codigo"));
        p.setFornecedor(rs.getString("fornecedor"));
        p.setPrecoUnitario(rs.getDouble("preco_unitario"));
        p.setEstoque(rs.getInt("estoque"));
        return p;
    }
}
