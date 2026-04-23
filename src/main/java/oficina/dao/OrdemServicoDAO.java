package oficina.dao;

import oficina.model.OrdemServico;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrdemServicoDAO {

    // ── CRUD da OS ────────────────────────────────────────────────────────────

    public void inserir(OrdemServico os) throws SQLException {
        String sql = "INSERT INTO ordem_servico (id_veiculo, id_funcionario, data_entrada, status, diagnostico, valor_total) "
                   + "VALUES (?,?,?,?,?,0)";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"id_os"})) {
            ps.setInt(1, os.getIdVeiculo());
            ps.setInt(2, os.getIdFuncionario());
            ps.setDate(3, Date.valueOf(os.getDataEntrada()));
            ps.setString(4, os.getStatus());
            ps.setString(5, os.getDiagnostico());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) os.setId(rs.getInt(1));
            }
        }
    }

    public OrdemServico buscarPorId(int id) throws SQLException {
        String sql = buildSelectBase() + " WHERE os.id_os = ?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        }
        return null;
    }

    public List<OrdemServico> listarTodos() throws SQLException {
        List<OrdemServico> lista = new ArrayList<>();
        String sql = buildSelectBase() + " ORDER BY os.data_entrada DESC";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    public List<OrdemServico> listarPorStatus(String status) throws SQLException {
        List<OrdemServico> lista = new ArrayList<>();
        String sql = buildSelectBase() + " WHERE os.status = ? ORDER BY os.data_entrada DESC";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public void atualizarStatus(int idOs, String novoStatus) throws SQLException {
        String sql = "UPDATE ordem_servico SET status = ?, data_saida = ? WHERE id_os = ?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, novoStatus);
            if ("CONCLUIDA".equals(novoStatus) || "CANCELADA".equals(novoStatus)) {
                ps.setDate(2, Date.valueOf(LocalDate.now()));
            } else {
                ps.setNull(2, Types.DATE);
            }
            ps.setInt(3, idOs);
            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        try (Connection conn = Conexao.obter()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM os_peca WHERE id_os = ?")) {
                    ps.setInt(1, id); ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM os_servico WHERE id_os = ?")) {
                    ps.setInt(1, id); ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM ordem_servico WHERE id_os = ?")) {
                    ps.setInt(1, id); ps.executeUpdate();
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }
        }
    }

    // ── Serviços na OS ────────────────────────────────────────────────────────

    public void adicionarServico(int idOs, int idServico, int qtd, double preco) throws SQLException {
        String sql = "INSERT INTO os_servico (id_os, id_servico, quantidade, preco_cobrado) VALUES (?,?,?,?) "
                   + "ON CONFLICT (id_os, id_servico) DO UPDATE SET "
                   + "quantidade = os_servico.quantidade + EXCLUDED.quantidade, "
                   + "preco_cobrado = EXCLUDED.preco_cobrado";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOs);
            ps.setInt(2, idServico);
            ps.setInt(3, qtd);
            ps.setDouble(4, preco);
            ps.executeUpdate();
        }
        recalcularTotal(idOs);
    }

    public void removerServico(int idOs, int idServico) throws SQLException {
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM os_servico WHERE id_os=? AND id_servico=?")) {
            ps.setInt(1, idOs);
            ps.setInt(2, idServico);
            ps.executeUpdate();
        }
        recalcularTotal(idOs);
    }

    // ── Peças na OS ───────────────────────────────────────────────────────────

    public void adicionarPeca(int idOs, int idPeca, int qtd, double preco) throws SQLException {
        String sql = "INSERT INTO os_peca (id_os, id_peca, quantidade, preco_cobrado) VALUES (?,?,?,?) "
                   + "ON CONFLICT (id_os, id_peca) DO UPDATE SET "
                   + "quantidade = os_peca.quantidade + EXCLUDED.quantidade, "
                   + "preco_cobrado = EXCLUDED.preco_cobrado";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOs);
            ps.setInt(2, idPeca);
            ps.setInt(3, qtd);
            ps.setDouble(4, preco);
            ps.executeUpdate();
        }
        recalcularTotal(idOs);
    }

    public void removerPeca(int idOs, int idPeca) throws SQLException {
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM os_peca WHERE id_os=? AND id_peca=?")) {
            ps.setInt(1, idOs);
            ps.setInt(2, idPeca);
            ps.executeUpdate();
        }
        recalcularTotal(idOs);
    }

    // ── Detalhes (itens) da OS ────────────────────────────────────────────────

    public void imprimirItens(int idOs) throws SQLException {
        System.out.println("\n  [ SERVIÇOS ]");
        String sqlS = "SELECT s.nome, oss.quantidade, oss.preco_cobrado "
                    + "FROM os_servico oss JOIN servico s ON s.id_servico = oss.id_servico "
                    + "WHERE oss.id_os = ?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sqlS)) {
            ps.setInt(1, idOs);
            try (ResultSet rs = ps.executeQuery()) {
                boolean algum = false;
                while (rs.next()) {
                    algum = true;
                    System.out.printf("  %-30s  qtd: %d  R$ %.2f%n",
                            rs.getString("nome"), rs.getInt("quantidade"), rs.getDouble("preco_cobrado"));
                }
                if (!algum) System.out.println("  (nenhum serviço)");
            }
        }
        System.out.println("\n  [ PEÇAS ]");
        String sqlP = "SELECT p.nome, osp.quantidade, osp.preco_cobrado "
                    + "FROM os_peca osp JOIN peca p ON p.id_peca = osp.id_peca "
                    + "WHERE osp.id_os = ?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sqlP)) {
            ps.setInt(1, idOs);
            try (ResultSet rs = ps.executeQuery()) {
                boolean algum = false;
                while (rs.next()) {
                    algum = true;
                    System.out.printf("  %-30s  qtd: %d  R$ %.2f%n",
                            rs.getString("nome"), rs.getInt("quantidade"), rs.getDouble("preco_cobrado"));
                }
                if (!algum) System.out.println("  (nenhuma peça)");
            }
        }
    }

    // ── Relatórios ────────────────────────────────────────────────────────────

    /** Relatório 1: OS por período */
    public void relatorioOsPorPeriodo(LocalDate inicio, LocalDate fim) throws SQLException {
        String sql =
            "SELECT os.id_os, c.nome AS cliente, v.placa, v.marca, v.modelo, " +
            "       f.nome AS mecanico, os.data_entrada, os.data_saida, os.status, os.valor_total " +
            "FROM ordem_servico os " +
            "JOIN veiculo v   ON v.id_veiculo     = os.id_veiculo " +
            "JOIN cliente c   ON c.id_cliente     = v.id_cliente " +
            "JOIN funcionario f ON f.id_funcionario = os.id_funcionario " +
            "WHERE os.data_entrada BETWEEN ? AND ? " +
            "ORDER BY os.data_entrada";
        System.out.printf("%n%-4s %-20s %-10s %-20s %-20s %-12s %-12s %-14s %10s%n",
                "OS","Cliente","Placa","Veículo","Mecânico","Entrada","Saída","Status","Total (R$)");
        separador(130);
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fim));
            double totalGeral = 0;
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double tot = rs.getDouble("valor_total");
                    totalGeral += tot;
                    System.out.printf("%-4d %-20s %-10s %-20s %-20s %-12s %-12s %-14s %10.2f%n",
                            rs.getInt("id_os"),
                            rs.getString("cliente"),
                            rs.getString("placa"),
                            rs.getString("marca") + " " + rs.getString("modelo"),
                            rs.getString("mecanico"),
                            rs.getDate("data_entrada"),
                            rs.getDate("data_saida") != null ? rs.getDate("data_saida").toString() : "-",
                            rs.getString("status"),
                            tot);
                }
            }
            separador(130);
            System.out.printf("%100s %10.2f%n", "TOTAL DO PERÍODO:", totalGeral);
        }
    }

    /** Relatório 2: Peças mais utilizadas */
    public void relatorioPecasMaisUsadas(LocalDate inicio, LocalDate fim) throws SQLException {
        String sql =
            "SELECT p.nome, p.codigo, SUM(osp.quantidade) AS total_qtd, " +
            "       SUM(osp.quantidade * osp.preco_cobrado) AS receita " +
            "FROM os_peca osp " +
            "JOIN peca p ON p.id_peca = osp.id_peca " +
            "JOIN ordem_servico os ON os.id_os = osp.id_os " +
            "WHERE os.data_entrada BETWEEN ? AND ? " +
            "GROUP BY p.id_peca, p.nome, p.codigo " +
            "ORDER BY total_qtd DESC";
        System.out.printf("%n%-35s %-12s %10s %15s%n",
                "Peça","Código","Qtd Usada","Receita (R$)");
        separador(75);
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fim));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("%-35s %-12s %10d %15.2f%n",
                            rs.getString("nome"),
                            rs.getString("codigo"),
                            rs.getInt("total_qtd"),
                            rs.getDouble("receita"));
                }
            }
        }
    }

    /** Relatório 3: Histórico de um veículo por placa */
    public void relatorioHistoricoVeiculo(String placa) throws SQLException {
        String sql =
            "SELECT os.id_os, f.nome AS mecanico, os.data_entrada, os.data_saida, " +
            "       os.status, os.diagnostico, os.valor_total " +
            "FROM ordem_servico os " +
            "JOIN veiculo v      ON v.id_veiculo      = os.id_veiculo " +
            "JOIN funcionario f  ON f.id_funcionario  = os.id_funcionario " +
            "WHERE v.placa = ? " +
            "ORDER BY os.data_entrada DESC";
        System.out.printf("%n  Histórico do veículo: %s%n", placa.toUpperCase());
        separador(100);
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                boolean algum = false;
                while (rs.next()) {
                    algum = true;
                    System.out.printf("  OS #%-4d | Mecânico: %-20s | Entrada: %s | Saída: %-10s | Status: %-12s | Total: R$ %.2f%n",
                            rs.getInt("id_os"),
                            rs.getString("mecanico"),
                            rs.getDate("data_entrada"),
                            rs.getDate("data_saida") != null ? rs.getDate("data_saida").toString() : "-",
                            rs.getString("status"),
                            rs.getDouble("valor_total"));
                    System.out.printf("           Diagnóstico: %s%n%n", rs.getString("diagnostico"));
                }
                if (!algum) System.out.println("  Nenhuma OS encontrada para a placa " + placa);
            }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void recalcularTotal(int idOs) throws SQLException {
        String sql =
            "UPDATE ordem_servico SET valor_total = (" +
            "  SELECT COALESCE(SUM(oss.quantidade * oss.preco_cobrado), 0) FROM os_servico oss WHERE oss.id_os = ?" +
            ") + (" +
            "  SELECT COALESCE(SUM(osp.quantidade * osp.preco_cobrado), 0) FROM os_peca    osp WHERE osp.id_os = ?" +
            ") WHERE id_os = ?";
        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOs);
            ps.setInt(2, idOs);
            ps.setInt(3, idOs);
            ps.executeUpdate();
        }
    }

    private String buildSelectBase() {
        return "SELECT os.*, v.placa, v.marca, v.modelo, f.nome AS nome_funcionario, c.nome AS nome_cliente " +
               "FROM ordem_servico os " +
               "JOIN veiculo v      ON v.id_veiculo      = os.id_veiculo " +
               "JOIN cliente c      ON c.id_cliente      = v.id_cliente " +
               "JOIN funcionario f  ON f.id_funcionario  = os.id_funcionario";
    }

    private OrdemServico mapear(ResultSet rs) throws SQLException {
        OrdemServico os = new OrdemServico();
        os.setId(rs.getInt("id_os"));
        os.setIdVeiculo(rs.getInt("id_veiculo"));
        os.setIdFuncionario(rs.getInt("id_funcionario"));
        os.setPlacaVeiculo(rs.getString("placa"));
        os.setNomeFuncionario(rs.getString("nome_funcionario"));
        os.setNomeCliente(rs.getString("nome_cliente"));
        Date entrada = rs.getDate("data_entrada");
        if (entrada != null) os.setDataEntrada(entrada.toLocalDate());
        Date saida = rs.getDate("data_saida");
        if (saida != null) os.setDataSaida(saida.toLocalDate());
        os.setStatus(rs.getString("status"));
        os.setValorTotal(rs.getDouble("valor_total"));
        os.setDiagnostico(rs.getString("diagnostico"));
        return os;
    }

    private static void separador(int n) {
        System.out.println("-".repeat(n));
    }
}
