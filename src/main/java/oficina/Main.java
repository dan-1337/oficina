package oficina;

import oficina.dao.Conexao;
import oficina.ui.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        // Verificar conexão com o banco antes de iniciar
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE CONTROLE DE OFICINA MECÂNICA      ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.print("  Conectando ao banco de dados...");
        try (Connection conn = Conexao.obter()) {
            System.out.println(" OK!");
        } catch (SQLException e) {
            System.out.println(" FALHA!");
            System.out.println("  Erro: " + e.getMessage());
            System.out.println("  Verifique as configurações em Conexao.java e tente novamente.");
            return;
        }

        ClienteUI      clienteUI  = new ClienteUI();
        VeiculoUI      veiculoUI  = new VeiculoUI();
        FuncionarioUI  funcUI     = new FuncionarioUI();
        ServicoUI      servicoUI  = new ServicoUI();
        PecaUI         pecaUI     = new PecaUI();
        OrdemServicoUI osUI       = new OrdemServicoUI();

        boolean sair = false;
        while (!sair) {
            System.out.println("\n╔══════════════════════════════════════════════════╗");
            System.out.println("║               MENU PRINCIPAL                     ║");
            System.out.println("╠══════════════════════════════════════════════════╣");
            System.out.println("║  CADASTROS (CRUD)                                ║");
            System.out.println("║    1. Clientes                                   ║");
            System.out.println("║    2. Veículos                                   ║");
            System.out.println("║    3. Funcionários                               ║");
            System.out.println("║    4. Serviços                                   ║");
            System.out.println("║    5. Peças                                      ║");
            System.out.println("╠══════════════════════════════════════════════════╣");
            System.out.println("║  PROCESSOS DE NEGÓCIO                            ║");
            System.out.println("║    6. Ordens de Serviço                          ║");
            System.out.println("╠══════════════════════════════════════════════════╣");
            System.out.println("║  RELATÓRIOS                                      ║");
            System.out.println("║    7. Relatórios do sistema                      ║");
            System.out.println("╠══════════════════════════════════════════════════╣");
            System.out.println("║    0. Sair                                       ║");
            System.out.println("╚══════════════════════════════════════════════════╝");

            int op = EntradaUtil.lerInt("  Opção: ");
            switch (op) {
                case 1 -> clienteUI.menu();
                case 2 -> veiculoUI.menu();
                case 3 -> funcUI.menu();
                case 4 -> servicoUI.menu();
                case 5 -> pecaUI.menu();
                case 6 -> osUI.menu();
                case 7 -> osUI.menuRelatorios();
                case 0 -> sair = true;
                default -> System.out.println("  Opção inválida.");
            }
        }
        System.out.println("\n  Sistema encerrado. Até logo!");
    }
}
