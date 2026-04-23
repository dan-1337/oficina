package oficina.ui;

import oficina.dao.PecaDAO;
import oficina.model.Peca;
import java.sql.SQLException;
import java.util.List;

public class PecaUI {
    private final PecaDAO dao = new PecaDAO();

    public void menu() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║       GERENCIAR PEÇAS        ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1. Cadastrar peça           ║");
            System.out.println("║  2. Listar todas             ║");
            System.out.println("║  3. Buscar por ID            ║");
            System.out.println("║  4. Atualizar peça           ║");
            System.out.println("║  5. Excluir peça             ║");
            System.out.println("║  0. Voltar                   ║");
            System.out.println("╚══════════════════════════════╝");
            int op = EntradaUtil.lerInt("  Opção: ");
            try {
                switch (op) {
                    case 1 -> cadastrar();
                    case 2 -> listar();
                    case 3 -> buscarPorId();
                    case 4 -> atualizar();
                    case 5 -> excluir();
                    case 0 -> sair = true;
                    default -> System.out.println("  Opção inválida.");
                }
            } catch (SQLException e) {
                System.out.println("  ERRO BD: " + e.getMessage());
            }
        }
    }

    private void cadastrar() throws SQLException {
        System.out.println("\n  -- Nova Peça --");
        Peca p = new Peca();
        p.setNome(EntradaUtil.ler("  Nome            : "));
        p.setCodigo(EntradaUtil.ler("  Código          : "));
        p.setFornecedor(EntradaUtil.ler("  Fornecedor      : "));
        p.setPrecoUnitario(EntradaUtil.lerDouble("  Preço unit (R$) : "));
        p.setEstoque(EntradaUtil.lerInt("  Estoque (qtd)   : "));
        dao.inserir(p);
        System.out.println("  Peça cadastrada com ID " + p.getId());
    }

    private void listar() throws SQLException {
        List<Peca> lista = dao.listarTodos();
        if (lista.isEmpty()) { System.out.println("  Nenhuma peça cadastrada."); return; }
        lista.forEach(p -> System.out.println("  " + p));
    }

    private void buscarPorId() throws SQLException {
        int id = EntradaUtil.lerInt("  ID da peça: ");
        Peca p = dao.buscarPorId(id);
        System.out.println(p != null ? "  " + p : "  Peça não encontrada.");
    }

    private void atualizar() throws SQLException {
        int id = EntradaUtil.lerInt("  ID da peça a atualizar: ");
        Peca p = dao.buscarPorId(id);
        if (p == null) { System.out.println("  Peça não encontrada."); return; }
        String val;
        val = EntradaUtil.ler("  Nome [" + p.getNome() + "]: ");            if (!val.isEmpty()) p.setNome(val);
        val = EntradaUtil.ler("  Código [" + p.getCodigo() + "]: ");        if (!val.isEmpty()) p.setCodigo(val);
        val = EntradaUtil.ler("  Fornecedor [" + p.getFornecedor() + "]: "); if (!val.isEmpty()) p.setFornecedor(val);
        val = EntradaUtil.ler("  Preço [" + p.getPrecoUnitario() + "]: ");
        if (!val.isEmpty()) p.setPrecoUnitario(Double.parseDouble(val.replace(",",".")));
        val = EntradaUtil.ler("  Estoque [" + p.getEstoque() + "]: ");
        if (!val.isEmpty()) p.setEstoque(Integer.parseInt(val));
        dao.atualizar(p);
        System.out.println("  Peça atualizada.");
    }

    private void excluir() throws SQLException {
        int id = EntradaUtil.lerInt("  ID da peça a excluir: ");
        Peca p = dao.buscarPorId(id);
        if (p == null) { System.out.println("  Peça não encontrada."); return; }
        if (EntradaUtil.confirmar("  Excluir \"" + p.getNome() + "\"?")) {
            boolean ok = dao.excluir(id);
            System.out.println(ok ? "  Peça excluída." : "  Não foi possível excluir.");
        }
    }
}
