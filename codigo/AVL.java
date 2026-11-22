/*
 * Projeto 2 · Estrutura de Dados
 * Integrantes: Pedro Henrique Silva Guimarães | Lucas Natal 
 */
public class AVL {
    private Node root;
    private int comparacoesUltimaBusca;
    private int contRSE;
    private int contRSD;
    private int contRDE;
    private int contRDD;
    private String logRotacoesUltimaOp;
    private int totalNos;

    public AVL() {
        root = null;
        comparacoesUltimaBusca = 0;
        contRSE = 0;
        contRSD = 0;
        contRDE = 0;
        contRDD = 0;
        logRotacoesUltimaOp = "";
        totalNos = 0;
    }

    public Node getRoot() { return root; }
    public boolean isEmpty() { return root == null; }
    public int getComparacoesUltimaBusca() { return comparacoesUltimaBusca; }
    public int getContRSE() { return contRSE; }
    public int getContRSD() { return contRSD; }
    public int getContRDE() { return contRDE; }
    public int getContRDD() { return contRDD; }
    public String getLogRotacoesUltimaOp() {
        return logRotacoesUltimaOp.isEmpty() ? "nenhuma" : logRotacoesUltimaOp;
    }
    public int getTotalNos() { return totalNos; }

    public int altura(Node x) {
        return (x == null) ? 0 : x.getAltura();
    }

    private void atualizarAltura(Node x) {
        if (x != null) {
            x.setAltura(1 + Math.max(altura(x.getEsquerdo()), altura(x.getDireito())));
        }
    }

    public int fb(Node x) {
        if (x == null) return 0;
        return altura(x.getDireito()) - altura(x.getEsquerdo());
    }

    private void registrarLog(String tipo, int codigoPivo) {
        if (!logRotacoesUltimaOp.isEmpty()) {
            logRotacoesUltimaOp += ", ";
        }
        logRotacoesUltimaOp += tipo + " no " + codigoPivo;
    }

    private Node rotacaoEsquerda(Node x) {
        Node y = x.getDireito();
        Node B = y.getEsquerdo();

        y.setEsquerdo(x);
        x.setDireito(B);

        y.setPai(x.getPai());
        x.setPai(y);
        if (B != null) B.setPai(x);

        if (y.getPai() == null) {
            root = y;
        } else if (y.getPai().getEsquerdo() == x) {
            y.getPai().setEsquerdo(y);
        } else {
            y.getPai().setDireito(y);
        }

        atualizarAltura(x);
        atualizarAltura(y);
        return y;
    }

    private Node rotacaoDireita(Node x) {
        Node y = x.getEsquerdo();
        Node B = y.getDireito();

        y.setDireito(x);
        x.setEsquerdo(B);

        y.setPai(x.getPai());
        x.setPai(y);
        if (B != null) B.setPai(x);

        if (y.getPai() == null) {
            root = y;
        } else if (y.getPai().getEsquerdo() == x) {
            y.getPai().setEsquerdo(y);
        } else {
            y.getPai().setDireito(y);
        }

        atualizarAltura(x);
        atualizarAltura(y);
        return y;
    }

    private void rebalancearCaminho(Node atual) {
        while (atual != null) {
            atualizarAltura(atual);
            int fator = fb(atual);
            Node proximoPai = atual.getPai();

            if (fator == 2) {
                if (fb(atual.getDireito()) >= 0) {
                    int cod = atual.getElemento();
                    rotacaoEsquerda(atual);
                    contRSE++;
                    registrarLog("RSE", cod);
                } else {
                    int cod = atual.getElemento();
                    rotacaoDireita(atual.getDireito());
                    rotacaoEsquerda(atual);
                    contRDE++;
                    registrarLog("RDE", cod);
                }
            } else if (fator == -2) {
                if (fb(atual.getEsquerdo()) <= 0) {
                    int cod = atual.getElemento();
                    rotacaoDireita(atual);
                    contRSD++;
                    registrarLog("RSD", cod);
                } else {
                    int cod = atual.getElemento();
                    rotacaoEsquerda(atual.getEsquerdo());
                    rotacaoDireita(atual);
                    contRDD++;
                    registrarLog("RDD", cod);
                }
            }
            atual = proximoPai;
        }
    }

    public Node busca(int k) {
        comparacoesUltimaBusca = 0;
        Node x = root;
        while (x != null) {
            comparacoesUltimaBusca++;
            if (x.getElemento() == k) return x;
            if (k < x.getElemento()) x = x.getEsquerdo();
            else x = x.getDireito();
        }
        return null;
    }

    public boolean insere(Ingresso ing) {
        logRotacoesUltimaOp = "";
        int k = ing.getCodigo();
        Node pai = null;
        Node x = root;
        while (x != null) {
            pai = x;
            if (k == x.getElemento()) return false;
            if (k < x.getElemento()) x = x.getEsquerdo();
            else x = x.getDireito();
        }
        Node novo = new Node(ing);
        novo.setPai(pai);
        if (pai == null) root = novo;
        else if (k < pai.getElemento()) pai.setEsquerdo(novo);
        else pai.setDireito(novo);
        totalNos++;
        rebalancearCaminho(pai);
        return true;
    }

    public Node minimo(Node x) {
        while (x.getEsquerdo() != null) x = x.getEsquerdo();
        return x;
    }

    public Node maximo(Node x) {
        while (x.getDireito() != null) x = x.getDireito();
        return x;
    }

    public boolean remove(int k) {
        logRotacoesUltimaOp = "";
        Node z = busca(k);
        if (z == null) return false;
        if (z.getEsquerdo() != null && z.getDireito() != null) {
            Node s = minimo(z.getDireito());
            z.setIngresso(s.getIngresso());
            z = s;
        }
        Node filho = z.getEsquerdo();
        if (filho == null) filho = z.getDireito();
        Node p = z.getPai();
        if (filho != null) filho.setPai(p);
        if (p == null) root = filho;
        else if (z == p.getEsquerdo()) p.setEsquerdo(filho);
        else p.setDireito(filho);
        totalNos--;
        rebalancearCaminho(p);
        return true;
    }

    public int consultaFaixa(Node x, int inicio, int fim) {
        if (x == null) return 0;
        int cont = 0;
        if (x.getElemento() > inicio) {
            cont += consultaFaixa(x.getEsquerdo(), inicio, fim);
        }
        if (x.getElemento() >= inicio && x.getElemento() <= fim) {
            Ingresso ing = x.getIngresso();
            String sufixo = ing.isEntrou() ? " | já entrou" : "";
            System.out.println("  " + ing.getCodigo() + " | " + ing.getNome() + " | " + ing.getSetor() + " | " + ing.getDia() + sufixo);
            cont++;
        }
        if (x.getElemento() < fim) {
            cont += consultaFaixa(x.getDireito(), inicio, fim);
        }
        return cont;
    }

    public int contarCheckinsRealizados(Node x) {
        if (x == null) return 0;
        int atual = x.getIngresso().isEntrou() ? 1 : 0;
        return atual + contarCheckinsRealizados(x.getEsquerdo()) + contarCheckinsRealizados(x.getDireito());
    }

    public void acumularRelatorioPublico(Node x, int[] totalSetor, int[] entrouSetor, int[] totalDia, int[] entrouDia) {
        if (x == null) return;
        acumularRelatorioPublico(x.getEsquerdo(), totalSetor, entrouSetor, totalDia, entrouDia);

        Ingresso ing = x.getIngresso();
        int idxSetor = -1;
        if (ing.getSetor().equalsIgnoreCase("Pista")) idxSetor = 0;
        else if (ing.getSetor().equalsIgnoreCase("Pista Premium")) idxSetor = 1;
        else if (ing.getSetor().equalsIgnoreCase("Arquibancada")) idxSetor = 2;
        else if (ing.getSetor().equalsIgnoreCase("Camarote")) idxSetor = 3;

        if (idxSetor != -1) {
            totalSetor[idxSetor]++;
            if (ing.isEntrou()) entrouSetor[idxSetor]++;
        }

        int idxDia = -1;
        if (ing.getDia().equalsIgnoreCase("Sexta")) idxDia = 0;
        else if (ing.getDia().equalsIgnoreCase("Sábado") || ing.getDia().equalsIgnoreCase("Sabado")) idxDia = 1;
        else if (ing.getDia().equalsIgnoreCase("Domingo")) idxDia = 2;

        if (idxDia != -1) {
            totalDia[idxDia]++;
            if (ing.isEntrou()) entrouDia[idxDia]++;
        }

        acumularRelatorioPublico(x.getDireito(), totalSetor, entrouSetor, totalDia, entrouDia);
    }
}