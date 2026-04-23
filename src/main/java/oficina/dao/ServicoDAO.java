package oficina.dao;

import oficina.model.Servico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {

    public void inserir(Servico s) throws SQLException {
        String sql = "INSERT INTO servico (nome, descricao, preco_base, tempo_estimado) VALUES (?,?,?,?)";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_servico"})) {
            ps.setString(1, s.getNome());
            ps.setString(2, s.getDescricao());
            ps.setDouble(3, s.getPrecoBase());
            ps.setInt(4, s.getTempoEstimado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) s.setId(rs.getInt(1));
            }
        }
    }

    public Servico buscarPorId(int id) throws SQLException {
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM servico WHERE id_servico = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public List<Servico> listarTodos() throws SQLException {
        List<Servico> lista = new ArrayList<>();
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM servico ORDER BY nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public void atualizar(Servico s) throws SQLException {
        String sql = "UPDATE servico SET nome=?, descricao=?, preco_base=?, tempo_estimado=? WHERE id_servico=?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getNome());
            ps.setString(2, s.getDescricao());
            ps.setDouble(3, s.getPrecoBase());
            ps.setInt(4, s.getTempoEstimado());
            ps.setInt(5, s.getId());
            ps.executeUpdate();
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM servico WHERE id_servico = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Servico mapear(ResultSet rs) throws SQLException {
        Servico s = new Servico();
        s.setId(rs.getInt("id_servico"));
        s.setNome(rs.getString("nome"));
        s.setDescricao(rs.getString("descricao"));
        s.setPrecoBase(rs.getDouble("preco_base"));
        s.setTempoEstimado(rs.getInt("tempo_estimado"));
        return s;
    }
}
