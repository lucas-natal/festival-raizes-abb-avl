/*
 * Projeto 2 · Estrutura de Dados
 * Integrantes: Pedro Henrique Silva Guimarães | Lucas Natal 
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static ABB abb = new ABB();
    private static AVL avl = new AVL();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        exibirMenu();

        while (true) {
            System.out.print("\nOpção: ");
            String entradaOp = sc.nextLine().trim();
            if (entradaOp.isEmpty()) continue;

            int opcao;
            try {
                opcao = Integer.parseInt(entradaOp);
            } catch (NumberFormatException e) {
                System.out.println("  Opção inválida.");
                continue;
            }

            if (opcao == 7) {
                System.out.println("  Até o próximo festival!");
                break;
            }

            if (opcao >= 2 && opcao <= 6 && avl.isEmpty()) {
                System.out.println("  Nenhum ingresso cadastrado. Carregue um arquivo primeiro (opção 1).");
                continue;
            }

            switch (opcao) {
                case 1:
                    carregarArquivo(sc);
                    break;
                case 2:
                    realizarCheckin(sc);
                    break;
                case 3:
                    venderNaPortaria(sc);
                    break;
                case 4:
                    cancelarIngresso(sc);
                    break;
                case 5:
                    consultarPorFaixa(sc);
                    break;
                case 6:
                    exibirPainel();
                    break;
                case 8:
                    if (avl.isEmpty()) {
                        System.out.println("  Nenhum ingresso cadastrado. Carregue um arquivo primeiro (opção 1).");
                    } else {
                        exibirRelatorioPublico();
                    }
                    break;
                default:
                    System.out.println("  Opção inválida.");
            }
        }
        sc.close();
    }

    private static void exibirMenu() {
        System.out.println("========== FESTIVAL RAÍZES · CHECK-IN ==========");
        System.out.println("1) Carregar arquivo      2) Check-in");
        System.out.println("3) Venda na portaria     4) Cancelamento");
        System.out.println("5) Consulta por faixa    6) Painel");
        System.out.println("7) Sair                  8) Relatório de público (Extensão C)");
    }

    private static void carregarArquivo(Scanner sc) {
        System.out.print("Arquivo: ");
        String nomeArquivo = sc.nextLine().trim();

        ABB novaAbb = new ABB();
        AVL novaAvl = new AVL();
        int carregados = 0;
        int ignoradas = 0;
        int numLinha = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                numLinha++;
                String linhaTrim = linha.trim();

                if (linhaTrim.isEmpty()) {
                    System.out.println("  Aviso: linha " + numLinha + " ignorada (linha vazia)");
                    ignoradas++;
                    continue;
                }

                String[] partes = linhaTrim.split(";", -1);
                if (partes.length != 4) {
                    System.out.println("  Aviso: linha " + numLinha + " ignorada (" + partes.length + " campos; esperado 4)");
                    ignoradas++;
                    continue;
                }

                String codStr = partes[0].trim();
                int codigo;
                try {
                    codigo = Integer.parseInt(codStr);
                } catch (NumberFormatException e) {
                    System.out.println("  Aviso: linha " + numLinha + " ignorada (código não numérico: \"" + codStr + "\")");
                    ignoradas++;
                    continue;
                }

                String nome = partes[1].trim();
                String setor = partes[2].trim();
                String dia = partes[3].trim();

                if (novaAvl.busca(codigo) != null) {
                    System.out.println("  Aviso: linha " + numLinha + " ignorada (código " + codigo + " repetido)");
                    ignoradas++;
                    continue;
                }

                Ingresso ingAvl = new Ingresso(codigo, nome, setor, dia);
                Ingresso ingAbb = new Ingresso(codigo, nome, setor, dia);

                novaAvl.insere(ingAvl);
                novaAbb.insere(ingAbb);
                carregados++;
            }

            abb = novaAbb;
            avl = novaAvl;
            System.out.println("  Carga concluída: " + carregados + " ingressos carregados, " + ignoradas + " linhas ignoradas.");

        } catch (IOException e) {
            System.out.println("  Erro ao abrir o arquivo: " + nomeArquivo);
        }
    }

    private static void realizarCheckin(Scanner sc) {
        System.out.print("Código: ");
        int codigo;
        try {
            codigo = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  Código inválido.");
            return;
        }

        Node noAvl = avl.busca(codigo);
        Node noAbb = abb.busca(codigo);

        int compAvl = avl.getComparacoesUltimaBusca();
        int compAbb = abb.getComparacoesUltimaBusca();

        if (noAvl == null) {
            System.out.println("  INVÁLIDO: código " + codigo + " inexistente");
        } else {
            Ingresso ing = noAvl.getIngresso();
            if (ing.isEntrou()) {
                System.out.println("  ALERTA: ingresso " + codigo + " JÁ UTILIZADO (" + ing.getNome() + ")");
            } else {
                ing.setEntrou(true);
                if (noAbb != null) noAbb.getIngresso().setEntrou(true);
                System.out.println("  VÁLIDO: " + ing.getNome() + " | " + ing.getSetor() + " | " + ing.getDia());
            }
        }
        System.out.println("  Comparações: AVL = " + compAvl + " | ABB = " + compAbb);
    }

    private static void venderNaPortaria(Scanner sc) {
        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        System.out.print("Setor: ");
        String setor = sc.nextLine().trim();
        System.out.print("Dia: ");
        String dia = sc.nextLine().trim();

        int novoCodigo = avl.maximo(avl.getRoot()).getElemento() + 1;

        Ingresso ingAvl = new Ingresso(novoCodigo, nome, setor, dia);
        Ingresso ingAbb = new Ingresso(novoCodigo, nome, setor, dia);

        avl.insere(ingAvl);
        abb.insere(ingAbb);

        System.out.println("  Ingresso " + novoCodigo + " emitido para " + nome + " | " + setor + " | " + dia);
        System.out.println("  Rotações na AVL: " + avl.getLogRotacoesUltimaOp());
    }

    private static void cancelarIngresso(Scanner sc) {
        System.out.print("Código: ");
        int codigo;
        try {
            codigo = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  Código inválido.");
            return;
        }

        Node noAvl = avl.busca(codigo);
        if (noAvl == null) {
            System.out.println("  Ingresso " + codigo + " inexistente.");
            return;
        }

        Ingresso ing = noAvl.getIngresso();
        if (ing.isEntrou()) {
            System.out.println("  Ingresso " + codigo + " já foi usado na entrada; não pode ser cancelado.");
            return;
        }

        String nomeTitular = ing.getNome();
        avl.remove(codigo);
        abb.remove(codigo);

        System.out.println("  Ingresso " + codigo + " (" + nomeTitular + ") cancelado.");
        System.out.println("  Rotações na AVL: " + avl.getLogRotacoesUltimaOp());
    }

    private static void consultarPorFaixa(Scanner sc) {
        System.out.print("Início da faixa: ");
        int inicio = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Fim da faixa: ");
        int fim = Integer.parseInt(sc.nextLine().trim());

        int encontrados = avl.consultaFaixa(avl.getRoot(), inicio, fim);
        System.out.println("  " + encontrados + " ingresso(s) entre " + inicio + " e " + fim + ".");
    }

    private static void exibirPainel() {
        int total = avl.getTotalNos();
        int altAbb = abb.altura(abb.getRoot());
        int altAvl = avl.altura(avl.getRoot());
        int codRaiz = avl.getRoot().getElemento();
        int fbRaiz = avl.fb(avl.getRoot());
        int realizados = avl.contarCheckinsRealizados(avl.getRoot());
        int restantes = total - realizados;

        System.out.println("  Ingressos cadastrados: " + total);
        System.out.println("  Altura ABB: " + altAbb + " | Altura AVL: " + altAvl);
        System.out.println("  Raiz da AVL: " + codRaiz + " (fb = " + fbRaiz + ")");
        System.out.println("  Rotações: RSE = " + avl.getContRSE()
                + " | RSD = " + avl.getContRSD()
                + " | RDE = " + avl.getContRDE()
                + " | RDD = " + avl.getContRDD());
        System.out.println("  Check-ins: " + realizados + " realizados | " + restantes + " restantes");
    }

    private static void exibirRelatorioPublico() {
        String[] nomesSetores = {"Pista", "Pista Premium", "Arquibancada", "Camarote"};
        String[] nomesDias = {"Sexta", "Sábado", "Domingo"};
        int[] totalSetor = new int[4];
        int[] entrouSetor = new int[4];
        int[] totalDia = new int[3];
        int[] entrouDia = new int[3];

        avl.acumularRelatorioPublico(avl.getRoot(), totalSetor, entrouSetor, totalDia, entrouDia);

        System.out.println("  --- RELATÓRIO DE PÚBLICO POR SETOR ---");
        for (int i = 0; i < 4; i++) {
            System.out.println("  " + nomesSetores[i] + ": " + totalSetor[i] + " vendidos | "
                    + entrouSetor[i] + " já entraram | " + (totalSetor[i] - entrouSetor[i]) + " restantes");
        }
        System.out.println("  --- RELATÓRIO DE PÚBLICO POR DIA ---");
        for (int i = 0; i < 3; i++) {
            System.out.println("  " + nomesDias[i] + ": " + totalDia[i] + " vendidos | "
                    + entrouDia[i] + " já entraram | " + (totalDia[i] - entrouDia[i]) + " restantes");
        }
    }
}