package oficina.dao;

import oficina.model.Funcionario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {

    public void inserir(Funcionario f) throws SQLException {
        String sql = "INSERT INTO funcionario (nome, cpf, cargo, especialidade, salario) VALUES (?,?,?,?,?)";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_funcionario"})) {
            ps.setString(1, f.getNome());
            ps.setString(2, f.getCpf());
            ps.setString(3, f.getCargo());
            ps.setString(4, f.getEspecialidade());
            ps.setDouble(5, f.getSalario());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) f.setId(rs.getInt(1));
            }
        }
    }

    public Funcionario buscarPorId(int id) throws SQLException {
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM funcionario WHERE id_funcionario = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public List<Funcionario> listarTodos() throws SQLException {
        List<Funcionario> lista = new ArrayList<>();
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM funcionario ORDER BY nome");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public void atualizar(Funcionario f) throws SQLException {
        String sql = "UPDATE funcionario SET nome=?, cpf=?, cargo=?, especialidade=?, salario=? WHERE id_funcionario=?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, f.getNome());
            ps.setString(2, f.getCpf());
            ps.setString(3, f.getCargo());
            ps.setString(4, f.getEspecialidade());
            ps.setDouble(5, f.getSalario());
            ps.setInt(6, f.getId());
            ps.executeUpdate();
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM funcionario WHERE id_funcionario = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Funcionario mapear(ResultSet rs) throws SQLException {
        Funcionario f = new Funcionario();
        f.setId(rs.getInt("id_funcionario"));
        f.setNome(rs.getString("nome"));
        f.setCpf(rs.getString("cpf"));
        f.setCargo(rs.getString("cargo"));
        f.setEspecialidade(rs.getString("especialidade"));
        f.setSalario(rs.getDouble("salario"));
        return f;
    }
}
