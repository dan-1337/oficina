package oficina.ui;

import oficina.dao.ServicoDAO;
import oficina.model.Servico;
import java.sql.SQLException;
import java.util.List;

public class ServicoUI {
    private final ServicoDAO dao = new ServicoDAO();

    public void menu() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║      GERENCIAR SERVIÇOS      ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1. Cadastrar serviço        ║");
            System.out.println("║  2. Listar todos             ║");
            System.out.println("║  3. Buscar por ID            ║");
            System.out.println("║  4. Atualizar serviço        ║");
            System.out.println("║  5. Excluir serviço          ║");
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
        System.out.println("\n  -- Novo Serviço --");
        Servico s = new Servico();
        s.setNome(EntradaUtil.ler("  Nome             : "));
        s.setDescricao(EntradaUtil.ler("  Descrição        : "));
        s.setPrecoBase(EntradaUtil.lerDouble("  Preço base (R$)  : "));
        s.setTempoEstimado(EntradaUtil.lerInt("  Tempo (min)      : "));
        dao.inserir(s);
        System.out.println("  Serviço cadastrado com ID " + s.getId());
    }

    private void listar() throws SQLException {
        List<Servico> lista = dao.listarTodos();
        if (lista.isEmpty()) { System.out.println("  Nenhum serviço cadastrado."); return; }
        lista.forEach(s -> System.out.println("  " + s));
    }

    private void buscarPorId() throws SQLException {
        int id = EntradaUtil.lerInt("  ID do serviço: ");
        Servico s = dao.buscarPorId(id);
        System.out.println(s != null ? "  " + s : "  Serviço não encontrado.");
    }

    private void atualizar() throws SQLException {
        int id = EntradaUtil.lerInt("  ID do serviço a atualizar: ");
        Servico s = dao.buscarPorId(id);
        if (s == null) { System.out.println("  Serviço não encontrado."); return; }
        String val;
        val = EntradaUtil.ler("  Nome [" + s.getNome() + "]: ");               if (!val.isEmpty()) s.setNome(val);
        val = EntradaUtil.ler("  Descrição [" + s.getDescricao() + "]: ");     if (!val.isEmpty()) s.setDescricao(val);
        val = EntradaUtil.ler("  Preço base [" + s.getPrecoBase() + "]: ");
        if (!val.isEmpty()) s.setPrecoBase(Double.parseDouble(val.replace(",",".")));
        val = EntradaUtil.ler("  Tempo min [" + s.getTempoEstimado() + "]: ");
        if (!val.isEmpty()) s.setTempoEstimado(Integer.parseInt(val));
        dao.atualizar(s);
        System.out.println("  Serviço atualizado.");
    }

    private void excluir() throws SQLException {
        int id = EntradaUtil.lerInt("  ID do serviço a excluir: ");
        Servico s = dao.buscarPorId(id);
        if (s == null) { System.out.println("  Serviço não encontrado."); return; }
        if (EntradaUtil.confirmar("  Excluir \"" + s.getNome() + "\"?")) {
            boolean ok = dao.excluir(id);
            System.out.println(ok ? "  Serviço excluído." : "  Não foi possível excluir.");
        }
    }
}
