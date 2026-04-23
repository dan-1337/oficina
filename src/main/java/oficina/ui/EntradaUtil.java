package oficina.ui;

import java.util.Scanner;

public class EntradaUtil {
    private static final Scanner sc = new Scanner(System.in);

    public static String ler(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    public static int lerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = sc.nextLine().trim();
            try { return Integer.parseInt(linha); }
            catch (NumberFormatException e) { System.out.println("  Informe um número inteiro válido."); }
        }
    }

    public static double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linha = sc.nextLine().trim().replace(",", ".");
            try { return Double.parseDouble(linha); }
            catch (NumberFormatException e) { System.out.println("  Informe um valor numérico válido."); }
        }
    }

    public static boolean confirmar(String prompt) {
        System.out.print(prompt + " (s/n): ");
        return sc.nextLine().trim().equalsIgnoreCase("s");
    }
}
