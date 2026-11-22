/*
 * Projeto 2 · Estrutura de Dados
  * Integrantes: Pedro Henrique Silva Guimarães | Lucas Natal 
 */
public class Node {
    private Ingresso ingresso;
    private Node esquerdo;
    private Node direito;
    private Node pai;
    private int altura;

    public Node(Ingresso ingresso) {
        this.ingresso = ingresso;
        this.esquerdo = null;
        this.direito = null;
        this.pai = null;
        this.altura = 1;
    }

    public Ingresso getIngresso() { return ingresso; }
    public void setIngresso(Ingresso ingresso) { this.ingresso = ingresso; }

    public int getElemento() { return ingresso.getCodigo(); }

    public Node getEsquerdo() { return esquerdo; }
    public void setEsquerdo(Node esquerdo) { this.esquerdo = esquerdo; }

    public Node getDireito() { return direito; }
    public void setDireito(Node direito) { this.direito = direito; }

    public Node getPai() { return pai; }
    public void setPai(Node pai) { this.pai = pai; }

    public int getAltura() { return altura; }
    public void setAltura(int altura) { this.altura = altura; }
}