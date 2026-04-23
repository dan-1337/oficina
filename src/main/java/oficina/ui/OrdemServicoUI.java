package oficina.ui;

import oficina.dao.*;
import oficina.model.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class OrdemServicoUI {

    private final OrdemServicoDAO osDAO   = new OrdemServicoDAO();
    private final ServicoDAO      servDAO = new ServicoDAO();
    private final PecaDAO         pecaDAO = new PecaDAO();
    private final VeiculoDAO      velDAO  = new VeiculoDAO();
    private final FuncionarioDAO  funcDAO = new FuncionarioDAO();

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ═══════════════════════════════════════════════════════════
    //  MENU PRINCIPAL DE OS
    // ═══════════════════════════════════════════════════════════

    public void menu() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║       ORDENS DE SERVIÇO (OS)       ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║  1. Abrir nova OS                  ║");
            System.out.println("║  2. Listar todas as OS             ║");
            System.out.println("║  3. Listar por status              ║");
            System.out.println("║  4. Consultar OS por ID            ║");
            System.out.println("║  5. Adicionar serviço à OS         ║");
            System.out.println("║  6. Adicionar peça à OS            ║");
            System.out.println("║  7. Remover serviço da OS          ║");
            System.out.println("║  8. Remover peça da OS             ║");
            System.out.println("║  9. Alterar status da OS           ║");
            System.out.println("║ 10. Excluir OS                     ║");
            System.out.println("║  0. Voltar                         ║");
            System.out.println("╚════════════════════════════════════╝");
            int op = EntradaUtil.lerInt("  Opção: ");
            try {
                switch (op) {
                    case 1  -> abrirOS();
                    case 2  -> listarTodas();
                    case 3  -> listarPorStatus();
                    case 4  -> consultarOS();
                    case 5  -> adicionarServico();
                    case 6  -> adicionarPeca();
                    case 7  -> removerServico();
                    case 8  -> removerPeca();
                    case 9  -> alterarStatus();
                    case 10 -> excluirOS();
                    case 0  -> sair = true;
                    default -> System.out.println("  Opção inválida.");
                }
            } catch (SQLException e) {
                System.out.println("  ERRO BD: " + e.getMessage());
            }
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  OPERAÇÕES
    // ═══════════════════════════════════════════════════════════

    private void abrirOS() throws SQLException {
        System.out.println("\n  -- Abrir Nova Ordem de Serviço --");

        // Exibir veículos disponíveis
        System.out.println("\n  Veículos cadastrados:");
        velDAO.listarTodos().forEach(v -> System.out.println("  " + v));
        int idVeiculo = EntradaUtil.lerInt("\n  ID do veículo      : ");

        // Exibir funcionários
        System.out.println("\n  Funcionários disponíveis:");
        funcDAO.listarTodos().forEach(f -> System.out.println("  " + f));
        int idFuncionario = EntradaUtil.lerInt("\n  ID do mecânico     : ");

        String diagnostico = EntradaUtil.ler("  Diagnóstico inicial: ");

        OrdemServico os = new OrdemServico();
        os.setIdVeiculo(idVeiculo);
        os.setIdFuncionario(idFuncionario);
        os.setDataEntrada(LocalDate.now());
        os.setStatus("ABERTA");
        os.setDiagnostico(diagnostico);
        osDAO.inserir(os);
        System.out.println("\n  OS #" + os.getId() + " aberta com sucesso! Data: " + LocalDate.now());
    }

    private void listarTodas() throws SQLException {
        List<OrdemServico> lista = osDAO.listarTodos();
        if (lista.isEmpty()) { System.out.println("  Nenhuma OS encontrada."); return; }
        System.out.println("\n  -- Todas as Ordens de Serviço --");
        lista.forEach(os -> System.out.println("  " + os));
    }

    private void listarPorStatus() throws SQLException {
        System.out.println("  Status: 1=ABERTA  2=EM_ANDAMENTO  3=CONCLUIDA  4=CANCELADA");
        int op = EntradaUtil.lerInt("  Escolha: ");
        String[] statuses = {"ABERTA", "EM_ANDAMENTO", "CONCLUIDA", "CANCELADA"};
        if (op < 1 || op > 4) { System.out.println("  Opção inválida."); return; }
        String status = statuses[op - 1];
        List<OrdemServico> lista = osDAO.listarPorStatus(status);
        if (lista.isEmpty()) { System.out.println("  Nenhuma OS com status " + status); return; }
        lista.forEach(os -> System.out.println("  " + os));
    }

    private void consultarOS() throws SQLException {
        int id = EntradaUtil.lerInt("  ID da OS: ");
        OrdemServico os = osDAO.buscarPorId(id);
        if (os == null) { System.out.println("  OS não encontrada."); return; }
        System.out.println("\n  " + os);
        System.out.println("  Diagnóstico: " + os.getDiagnostico());
        osDAO.imprimirItens(id);
        System.out.printf("\n  TOTAL: R$ %.2f%n", os.getValorTotal());
    }

    private void adicionarServico() throws SQLException {
        int idOs = EntradaUtil.lerInt("  ID da OS: ");
        OrdemServico os = osDAO.buscarPorId(idOs);
        if (os == null) { System.out.println("  OS não encontrada."); return; }

        System.out.println("\n  Serviços disponíveis:");
        servDAO.listarTodos().forEach(s -> System.out.println("  " + s));

        int idServico = EntradaUtil.lerInt("\n  ID do serviço: ");
        Servico servico = servDAO.buscarPorId(idServico);
        if (servico == null) { System.out.println("  Serviço não encontrado."); return; }

        int qtd = EntradaUtil.lerInt("  Quantidade: ");
        System.out.printf("  Preço base: R$ %.2f%n", servico.getPrecoBase());
        double preco = EntradaUtil.lerDouble("  Preço cobrado (Enter para usar o base): ");
        if (preco <= 0) preco = servico.getPrecoBase();

        osDAO.adicionarServico(idOs, idServico, qtd, preco);
        System.out.println("  Serviço adicionado! OS atualizada.");
        OrdemServico atualizada = osDAO.buscarPorId(idOs);
        System.out.printf("  Novo total: R$ %.2f%n", atualizada.getValorTotal());
    }

    private void adicionarPeca() throws SQLException {
        int idOs = EntradaUtil.lerInt("  ID da OS: ");
        OrdemServico os = osDAO.buscarPorId(idOs);
        if (os == null) { System.out.println("  OS não encontrada."); return; }

        System.out.println("\n  Peças em estoque:");
        pecaDAO.listarTodos().forEach(p -> System.out.println("  " + p));

        int idPeca = EntradaUtil.lerInt("\n  ID da peça: ");
        Peca peca = pecaDAO.buscarPorId(idPeca);
        if (peca == null) { System.out.println("  Peça não encontrada."); return; }
        if (peca.getEstoque() == 0) { System.out.println("  Peça sem estoque!"); return; }

        int qtd = EntradaUtil.lerInt("  Quantidade: ");
        if (qtd > peca.getEstoque()) {
            System.out.println("  Estoque insuficiente (disponível: " + peca.getEstoque() + ")");
            return;
        }

        double preco = peca.getPrecoUnitario();
        System.out.printf("  Preço unitário: R$ %.2f  ->  Total peças: R$ %.2f%n", preco, preco * qtd);

        osDAO.adicionarPeca(idOs, idPeca, qtd, preco);
        pecaDAO.atualizarEstoque(idPeca, qtd);
        System.out.println("  Peça adicionada e estoque atualizado!");
        OrdemServico atualizada = osDAO.buscarPorId(idOs);
        System.out.printf("  Novo total: R$ %.2f%n", atualizada.getValorTotal());
    }

    private void removerServico() throws SQLException {
        int idOs      = EntradaUtil.lerInt("  ID da OS: ");
        int idServico = EntradaUtil.lerInt("  ID do serviço a remover: ");
        osDAO.removerServico(idOs, idServico);
        System.out.println("  Serviço removido da OS.");
    }

    private void removerPeca() throws SQLException {
        int idOs   = EntradaUtil.lerInt("  ID da OS: ");
        int idPeca = EntradaUtil.lerInt("  ID da peça a remover: ");
        osDAO.removerPeca(idOs, idPeca);
        System.out.println("  Peça removida da OS.");
    }

    private void alterarStatus() throws SQLException {
        int idOs = EntradaUtil.lerInt("  ID da OS: ");
        OrdemServico os = osDAO.buscarPorId(idOs);
        if (os == null) { System.out.println("  OS não encontrada."); return; }
        System.out.println("  Status atual: " + os.getStatus());
        System.out.println("  1=ABERTA  2=EM_ANDAMENTO  3=CONCLUIDA  4=CANCELADA");
        int op = EntradaUtil.lerInt("  Novo status: ");
        String[] statuses = {"ABERTA", "EM_ANDAMENTO", "CONCLUIDA", "CANCELADA"};
        if (op < 1 || op > 4) { System.out.println("  Opção inválida."); return; }
        osDAO.atualizarStatus(idOs, statuses[op - 1]);
        System.out.println("  Status alterado para " + statuses[op - 1]);
    }

    private void excluirOS() throws SQLException {
        int idOs = EntradaUtil.lerInt("  ID da OS a excluir: ");
        OrdemServico os = osDAO.buscarPorId(idOs);
        if (os == null) { System.out.println("  OS não encontrada."); return; }
        System.out.println("  " + os);
        if (EntradaUtil.confirmar("  Excluir esta OS e todos os seus itens?")) {
            osDAO.excluir(idOs);
            System.out.println("  OS excluída.");
        }
    }

    // ═══════════════════════════════════════════════════════════
    //  RELATÓRIOS
    // ═══════════════════════════════════════════════════════════

    public void menuRelatorios() {
        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔════════════════════════════════════╗");
            System.out.println("║          RELATÓRIOS                ║");
            System.out.println("╠════════════════════════════════════╣");
            System.out.println("║  1. OS por período                 ║");
            System.out.println("║  2. Peças mais utilizadas          ║");
            System.out.println("║  3. Histórico de veículo           ║");
            System.out.println("║  0. Voltar                         ║");
            System.out.println("╚════════════════════════════════════╝");
            int op = EntradaUtil.lerInt("  Opção: ");
            try {
                switch (op) {
                    case 1 -> relatorioOsPorPeriodo();
                    case 2 -> relatorioPecasMaisUsadas();
                    case 3 -> relatorioHistoricoVeiculo();
                    case 0 -> sair = true;
                    default -> System.out.println("  Opção inválida.");
                }
            } catch (Exception e) {
                System.out.println("  ERRO: " + e.getMessage());
            }
        }
    }

    private void relatorioOsPorPeriodo() throws SQLException {
        System.out.println("\n  -- Relatório: OS por Período --");
        LocalDate inicio = lerData("  Data inicial (dd/mm/aaaa): ");
        LocalDate fim    = lerData("  Data final   (dd/mm/aaaa): ");
        osDAO.relatorioOsPorPeriodo(inicio, fim);
    }

    private void relatorioPecasMaisUsadas() throws SQLException {
        System.out.println("\n  -- Relatório: Peças Mais Utilizadas --");
        LocalDate inicio = lerData("  Data inicial (dd/mm/aaaa): ");
        LocalDate fim    = lerData("  Data final   (dd/mm/aaaa): ");
        osDAO.relatorioPecasMaisUsadas(inicio, fim);
    }

    private void relatorioHistoricoVeiculo() throws SQLException {
        System.out.println("\n  -- Relatório: Histórico de Veículo --");
        String placa = EntradaUtil.ler("  Placa do veículo: ");
        osDAO.relatorioHistoricoVeiculo(placa);
    }

    // ─── helpers ────────────────────────────────────────────────

    private LocalDate lerData(String prompt) {
        while (true) {
            String s = EntradaUtil.ler(prompt);
            try {
                return LocalDate.parse(s, FMT);
            } catch (DateTimeParseException e) {
                System.out.println("  Formato inválido. Use dd/mm/aaaa.");
            }
        }
    }
}
