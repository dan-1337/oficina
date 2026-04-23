package oficina.ui;

import oficina.dao.FuncionarioDAO;
import oficina.model.Funcionario;
import java.sql.SQLException;
import java.util.List;

public class FuncionarioUI {
    private final FuncionarioDAO dao = new FuncionarioDAO();

    public void menu() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║     GERENCIAR FUNCIONÁRIOS   ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1. Cadastrar funcionário    ║");
            System.out.println("║  2. Listar todos             ║");
            System.out.println("║  3. Buscar por ID            ║");
            System.out.println("║  4. Atualizar funcionário    ║");
            System.out.println("║  5. Excluir funcionário      ║");
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
        System.out.println("\n  -- Novo Funcionário --");
        Funcionario f = new Funcionario();
        f.setNome(EntradaUtil.ler("  Nome          : "));
        f.setCpf(EntradaUtil.ler("  CPF           : "));
        f.setCargo(EntradaUtil.ler("  Cargo         : "));
        f.setEspecialidade(EntradaUtil.ler("  Especialidade : "));
        f.setSalario(EntradaUtil.lerDouble("  Salário (R$)  : "));
        dao.inserir(f);
        System.out.println("  Funcionário cadastrado com ID " + f.getId());
    }

    private void listar() throws SQLException {
        List<Funcionario> lista = dao.listarTodos();
        if (lista.isEmpty()) { System.out.println("  Nenhum funcionário."); return; }
        lista.forEach(f -> System.out.println("  " + f));
    }

    private void buscarPorId() throws SQLException {
        int id = EntradaUtil.lerInt("  ID do funcionário: ");
        Funcionario f = dao.buscarPorId(id);
        System.out.println(f != null ? "  " + f : "  Funcionário não encontrado.");
    }

    private void atualizar() throws SQLException {
        int id = EntradaUtil.lerInt("  ID do funcionário a atualizar: ");
        Funcionario f = dao.buscarPorId(id);
        if (f == null) { System.out.println("  Funcionário não encontrado."); return; }
        String val;
        val = EntradaUtil.ler("  Nome [" + f.getNome() + "]: ");             if (!val.isEmpty()) f.setNome(val);
        val = EntradaUtil.ler("  CPF [" + f.getCpf() + "]: ");               if (!val.isEmpty()) f.setCpf(val);
        val = EntradaUtil.ler("  Cargo [" + f.getCargo() + "]: ");           if (!val.isEmpty()) f.setCargo(val);
        val = EntradaUtil.ler("  Especialidade [" + f.getEspecialidade() + "]: "); if (!val.isEmpty()) f.setEspecialidade(val);
        val = EntradaUtil.ler("  Salário [" + f.getSalario() + "]: ");
        if (!val.isEmpty()) f.setSalario(Double.parseDouble(val.replace(",",".")));
        dao.atualizar(f);
        System.out.println("  Funcionário atualizado.");
    }

    private void excluir() throws SQLException {
        int id = EntradaUtil.lerInt("  ID do funcionário a excluir: ");
        Funcionario f = dao.buscarPorId(id);
        if (f == null) { System.out.println("  Funcionário não encontrado."); return; }
        if (EntradaUtil.confirmar("  Excluir " + f.getNome() + "?")) {
            boolean ok = dao.excluir(id);
            System.out.println(ok ? "  Funcionário excluído." : "  Não foi possível excluir.");
        }
    }
}
