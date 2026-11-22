/*
 * Projeto 2 · Estrutura de Dados
 * Integrantes: Pedro Henrique Silva Guimarães | Lucas Natal
 *
 */
public class ABB {
    private Node root;
    private int comparacoesUltimaBusca;
    private int totalNos;

    public ABB() {
        root = null;
        comparacoesUltimaBusca = 0;
        totalNos = 0;
    }

    public Node getRoot() { return root; }
    public boolean isEmpty() { return root == null; }
    public int getComparacoesUltimaBusca() { return comparacoesUltimaBusca; }
    public int getTotalNos() { return totalNos; }

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
        atualizarAlturasIterativo(pai);
        return true;
    }

    private void atualizarAlturasIterativo(Node atual) {
        while (atual != null) {
            int altEsq = (atual.getEsquerdo() == null) ? 0 : atual.getEsquerdo().getAltura();
            int altDir = (atual.getDireito() == null) ? 0 : atual.getDireito().getAltura();
            int novaAlt = 1 + Math.max(altEsq, altDir);
            if (atual.getAltura() == novaAlt) break;
            atual.setAltura(novaAlt);
            atual = atual.getPai();
        }
    }

    public Node minimo(Node x) {
        while (x.getEsquerdo() != null) x = x.getEsquerdo();
        return x;
    }

    public Node maximo(Node x) {
        while (x.getDireito() != null) x = x.getDireito();
        return x;
    }

    public Node sucessor(Node x) {
        if (x.getDireito() != null) return minimo(x.getDireito());
        Node y = x.getPai();
        while (y != null && x == y.getDireito()) {
            x = y;
            y = y.getPai();
        }
        return y;
    }

    public boolean remove(int k) {
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
        recalcularAlturasCaminho(p);
        return true;
    }

    private void recalcularAlturasCaminho(Node atual) {
        while (atual != null) {
            int altEsq = (atual.getEsquerdo() == null) ? 0 : atual.getEsquerdo().getAltura();
            int altDir = (atual.getDireito() == null) ? 0 : atual.getDireito().getAltura();
            atual.setAltura(1 + Math.max(altEsq, altDir));
            atual = atual.getPai();
        }
    }

    public int altura(Node x) {
        if (x == null) return 0;
        return x.getAltura();
    }

    public int contaNos(Node x) {
        if (x == root) return totalNos;
        if (x == null) return 0;
        return 1 + contaNos(x.getEsquerdo()) + contaNos(x.getDireito());
    }
}