package oficina.dao;

import oficina.model.Veiculo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoDAO {

    public void inserir(Veiculo v) throws SQLException {
        String sql = "INSERT INTO veiculo (id_cliente, placa, marca, modelo, ano, cor, chassi) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_veiculo"})) {
            ps.setInt(1, v.getIdCliente());
            ps.setString(2, v.getPlaca());
            ps.setString(3, v.getMarca());
            ps.setString(4, v.getModelo());
            ps.setInt(5, v.getAno());
            ps.setString(6, v.getCor());
            ps.setString(7, v.getChassi());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) v.setId(rs.getInt(1));
            }
        }
    }

    public Veiculo buscarPorId(int id) throws SQLException {
        String sql = "SELECT v.*, c.nome AS nome_cliente FROM veiculo v " +
                     "JOIN cliente c ON c.id_cliente = v.id_cliente WHERE v.id_veiculo = ?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public Veiculo buscarPorPlaca(String placa) throws SQLException {
        String sql = "SELECT v.*, c.nome AS nome_cliente FROM veiculo v " +
                     "JOIN cliente c ON c.id_cliente = v.id_cliente WHERE v.placa = ?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public List<Veiculo> listarTodos() throws SQLException {
        List<Veiculo> lista = new ArrayList<>();
        String sql = "SELECT v.*, c.nome AS nome_cliente FROM veiculo v " +
                     "JOIN cliente c ON c.id_cliente = v.id_cliente ORDER BY v.placa";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<Veiculo> listarPorCliente(int idCliente) throws SQLException {
        List<Veiculo> lista = new ArrayList<>();
        String sql = "SELECT v.*, c.nome AS nome_cliente FROM veiculo v " +
                     "JOIN cliente c ON c.id_cliente = v.id_cliente WHERE v.id_cliente = ?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void atualizar(Veiculo v) throws SQLException {
        String sql = "UPDATE veiculo SET id_cliente=?, placa=?, marca=?, modelo=?, ano=?, cor=?, chassi=? WHERE id_veiculo=?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, v.getIdCliente());
            ps.setString(2, v.getPlaca());
            ps.setString(3, v.getMarca());
            ps.setString(4, v.getModelo());
            ps.setInt(5, v.getAno());
            ps.setString(6, v.getCor());
            ps.setString(7, v.getChassi());
            ps.setInt(8, v.getId());
            ps.executeUpdate();
        }
    }

    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM veiculo WHERE id_veiculo = ?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Veiculo mapear(ResultSet rs) throws SQLException {
        Veiculo v = new Veiculo();
        v.setId(rs.getInt("id_veiculo"));
        v.setIdCliente(rs.getInt("id_cliente"));
        v.setNomeCliente(rs.getString("nome_cliente"));
        v.setPlaca(rs.getString("placa"));
        v.setMarca(rs.getString("marca"));
        v.setModelo(rs.getString("modelo"));
        v.setAno(rs.getInt("ano"));
        v.setCor(rs.getString("cor"));
        v.setChassi(rs.getString("chassi"));
        return v;
    }
}
