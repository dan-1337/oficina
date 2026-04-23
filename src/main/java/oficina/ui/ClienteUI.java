package oficina.ui;

import oficina.dao.ClienteDAO;
import oficina.model.Cliente;
import java.sql.SQLException;
import java.util.List;

public class ClienteUI {
    private final ClienteDAO dao = new ClienteDAO();

    public void menu() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║       GERENCIAR CLIENTES     ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1. Cadastrar cliente        ║");
            System.out.println("║  2. Listar todos             ║");
            System.out.println("║  3. Buscar por ID            ║");
            System.out.println("║  4. Buscar por nome          ║");
            System.out.println("║  5. Atualizar cliente        ║");
            System.out.println("║  6. Excluir cliente          ║");
            System.out.println("║  0. Voltar                   ║");
            System.out.println("╚══════════════════════════════╝");
            int op = EntradaUtil.lerInt("  Opção: ");
            try {
                switch (op) {
                    case 1 -> cadastrar();
                    case 2 -> listar();
                    case 3 -> buscarPorId();
                    case 4 -> buscarPorNome();
                    case 5 -> atualizar();
                    case 6 -> excluir();
                    case 0 -> sair = true;
                    default -> System.out.println("  Opção inválida.");
                }
            } catch (SQLException e) {
                System.out.println("  ERRO BD: " + e.getMessage());
            }
        }
    }

    private void cadastrar() throws SQLException {
        System.out.println("\n  -- Novo Cliente --");
        Cliente c = new Cliente();
        c.setNome(EntradaUtil.ler("  Nome       : "));
        c.setCpf(EntradaUtil.ler("  CPF        : "));
        c.setTelefone(EntradaUtil.ler("  Telefone   : "));
        c.setEmail(EntradaUtil.ler("  E-mail     : "));
        c.setEndereco(EntradaUtil.ler("  Endereço   : "));
        dao.inserir(c);
        System.out.println("  Cliente cadastrado com ID " + c.getId());
    }

    private void listar() throws SQLException {
        List<Cliente> lista = dao.listarTodos();
        if (lista.isEmpty()) { System.out.println("  Nenhum cliente cadastrado."); return; }
        System.out.println("\n  -- Clientes --");
        lista.forEach(c -> System.out.println("  " + c));
    }

    private void buscarPorId() throws SQLException {
        int id = EntradaUtil.lerInt("  ID do cliente: ");
        Cliente c = dao.buscarPorId(id);
        System.out.println(c != null ? "  " + c : "  Cliente não encontrado.");
    }

    private void buscarPorNome() throws SQLException {
        String nome = EntradaUtil.ler("  Nome (parcial): ");
        List<Cliente> lista = dao.buscarPorNome(nome);
        if (lista.isEmpty()) { System.out.println("  Nenhum resultado."); return; }
        lista.forEach(c -> System.out.println("  " + c));
    }

    private void atualizar() throws SQLException {
        int id = EntradaUtil.lerInt("  ID do cliente a atualizar: ");
        Cliente c = dao.buscarPorId(id);
        if (c == null) { System.out.println("  Cliente não encontrado."); return; }
        System.out.println("  Atual: " + c);
        System.out.println("  (Enter para manter o valor atual)");
        String v;
        v = EntradaUtil.ler("  Nome [" + c.getNome() + "]: ");
        if (!v.isEmpty()) c.setNome(v);
        v = EntradaUtil.ler("  CPF [" + c.getCpf() + "]: ");
        if (!v.isEmpty()) c.setCpf(v);
        v = EntradaUtil.ler("  Telefone [" + c.getTelefone() + "]: ");
        if (!v.isEmpty()) c.setTelefone(v);
        v = EntradaUtil.ler("  E-mail [" + c.getEmail() + "]: ");
        if (!v.isEmpty()) c.setEmail(v);
        v = EntradaUtil.ler("  Endereço [" + c.getEndereco() + "]: ");
        if (!v.isEmpty()) c.setEndereco(v);
        dao.atualizar(c);
        System.out.println("  Cliente atualizado.");
    }

    private void excluir() throws SQLException {
        int id = EntradaUtil.lerInt("  ID do cliente a excluir: ");
        Cliente c = dao.buscarPorId(id);
        if (c == null) { System.out.println("  Cliente não encontrado."); return; }
        System.out.println("  " + c);
        if (EntradaUtil.confirmar("  Confirmar exclusão?")) {
            boolean ok = dao.excluir(id);
            System.out.println(ok ? "  Cliente excluído." : "  Não foi possível excluir (possui veículos vinculados?).");
        }
    }
}
