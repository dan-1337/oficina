package oficina.ui;

import oficina.dao.VeiculoDAO;
import oficina.model.Veiculo;
import java.sql.SQLException;
import java.util.List;

public class VeiculoUI {
    private final VeiculoDAO dao = new VeiculoDAO();

    public void menu() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔══════════════════════════════╗");
            System.out.println("║       GERENCIAR VEÍCULOS     ║");
            System.out.println("╠══════════════════════════════╣");
            System.out.println("║  1. Cadastrar veículo        ║");
            System.out.println("║  2. Listar todos             ║");
            System.out.println("║  3. Buscar por ID            ║");
            System.out.println("║  4. Buscar por placa         ║");
            System.out.println("║  5. Listar por cliente       ║");
            System.out.println("║  6. Atualizar veículo        ║");
            System.out.println("║  7. Excluir veículo          ║");
            System.out.println("║  0. Voltar                   ║");
            System.out.println("╚══════════════════════════════╝");
            int op = EntradaUtil.lerInt("  Opção: ");
            try {
                switch (op) {
                    case 1 -> cadastrar();
                    case 2 -> listar();
                    case 3 -> buscarPorId();
                    case 4 -> buscarPorPlaca();
                    case 5 -> listarPorCliente();
                    case 6 -> atualizar();
                    case 7 -> excluir();
                    case 0 -> sair = true;
                    default -> System.out.println("  Opção inválida.");
                }
            } catch (SQLException e) {
                System.out.println("  ERRO BD: " + e.getMessage());
            }
        }
    }

    private void cadastrar() throws SQLException {
        System.out.println("\n  -- Novo Veículo --");
        Veiculo v = new Veiculo();
        v.setIdCliente(EntradaUtil.lerInt("  ID do cliente  : "));
        v.setPlaca(EntradaUtil.ler("  Placa          : "));
        v.setMarca(EntradaUtil.ler("  Marca          : "));
        v.setModelo(EntradaUtil.ler("  Modelo         : "));
        v.setAno(EntradaUtil.lerInt("  Ano            : "));
        v.setCor(EntradaUtil.ler("  Cor            : "));
        v.setChassi(EntradaUtil.ler("  Chassi         : "));
        dao.inserir(v);
        System.out.println("  Veículo cadastrado com ID " + v.getId());
    }

    private void listar() throws SQLException {
        List<Veiculo> lista = dao.listarTodos();
        if (lista.isEmpty()) { System.out.println("  Nenhum veículo."); return; }
        lista.forEach(v -> System.out.println("  " + v));
    }

    private void buscarPorId() throws SQLException {
        int id = EntradaUtil.lerInt("  ID do veículo: ");
        Veiculo v = dao.buscarPorId(id);
        System.out.println(v != null ? "  " + v : "  Veículo não encontrado.");
    }

    private void buscarPorPlaca() throws SQLException {
        String placa = EntradaUtil.ler("  Placa: ");
        Veiculo v = dao.buscarPorPlaca(placa);
        System.out.println(v != null ? "  " + v : "  Veículo não encontrado.");
    }

    private void listarPorCliente() throws SQLException {
        int idCliente = EntradaUtil.lerInt("  ID do cliente: ");
        List<Veiculo> lista = dao.listarPorCliente(idCliente);
        if (lista.isEmpty()) { System.out.println("  Nenhum veículo para este cliente."); return; }
        lista.forEach(v -> System.out.println("  " + v));
    }

    private void atualizar() throws SQLException {
        int id = EntradaUtil.lerInt("  ID do veículo a atualizar: ");
        Veiculo v = dao.buscarPorId(id);
        if (v == null) { System.out.println("  Veículo não encontrado."); return; }
        System.out.println("  Atual: " + v);
        String val;
        val = EntradaUtil.ler("  Placa [" + v.getPlaca() + "]: ");      if (!val.isEmpty()) v.setPlaca(val);
        val = EntradaUtil.ler("  Marca [" + v.getMarca() + "]: ");      if (!val.isEmpty()) v.setMarca(val);
        val = EntradaUtil.ler("  Modelo [" + v.getModelo() + "]: ");    if (!val.isEmpty()) v.setModelo(val);
        val = EntradaUtil.ler("  Ano [" + v.getAno() + "]: ");
        if (!val.isEmpty()) v.setAno(Integer.parseInt(val));
        val = EntradaUtil.ler("  Cor [" + v.getCor() + "]: ");         if (!val.isEmpty()) v.setCor(val);
        val = EntradaUtil.ler("  Chassi [" + v.getChassi() + "]: ");   if (!val.isEmpty()) v.setChassi(val);
        dao.atualizar(v);
        System.out.println("  Veículo atualizado.");
    }

    private void excluir() throws SQLException {
        int id = EntradaUtil.lerInt("  ID do veículo a excluir: ");
        Veiculo v = dao.buscarPorId(id);
        if (v == null) { System.out.println("  Veículo não encontrado."); return; }
        if (EntradaUtil.confirmar("  Excluir " + v.getPlaca() + "?")) {
            boolean ok = dao.excluir(id);
            System.out.println(ok ? "  Veículo excluído." : "  Não foi possível excluir.");
        }
    }
}
