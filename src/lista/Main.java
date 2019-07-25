package lista;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ListaMemoriaDisponivel listaMemoriaDisponivel = new ListaMemoriaDisponivel();
        Scanner leia = new Scanner(System.in);
        int op = -1;
        System.out.println("\nMENU\n" +
                "1 - Solicitar\n" +
                "2 - Devolver\n" +
                "3 - Imprimir Disponivel\n" +
                "4 - imprimir Indisponivel\n" +
                "0 - Sair\n" +
                "Digite o numero da opção:");

        int i = 0;
        while (op != 0) {
            try {
                op = leia.nextInt();
            } catch (Exception err) {
                break;
            }
            switch (op) {
                case 1:
                    int length = leia.nextInt();
                    boolean resultutado = listaMemoriaDisponivel.solicitar(length);
                    break;
                case 2:
                    int length2 = leia.nextInt();
                    String endereco = leia.next();
                    listaMemoriaDisponivel.devolver(endereco, length2);
                    break;
                case 3:
                    listaMemoriaDisponivel.imprimirDisponivel();
                    System.out.println();
                    break;
                case 4:
                    listaMemoriaDisponivel.imprimirIndisponivel();
                    System.out.println();
                    break;
                default:
                    break;
            }
        }
    }
}
