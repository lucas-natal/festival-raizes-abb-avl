/*
 * Projeto 2 · Estrutura de Dados
  * Integrantes: Pedro Henrique Silva Guimarães | Lucas Natal 
 */
public class Ingresso {
    private int codigo;
    private String nome;
    private String setor;
    private String dia;
    private boolean entrou;

    public Ingresso(int codigo, String nome, String setor, String dia) {
        this.codigo = codigo;
        this.nome = nome;
        this.setor = setor;
        this.dia = dia;
        this.entrou = false;
    }

    public int getCodigo() { return codigo; }
    public void setCodigo(int codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSetor() { return setor; }
    public void setSetor(String setor) { this.setor = setor; }

    public String getDia() { return dia; }
    public void setDia(String dia) { this.dia = dia; }

    public boolean isEntrou() { return entrou; }
    public void setEntrou(boolean entrou) { this.entrou = entrou; }
}