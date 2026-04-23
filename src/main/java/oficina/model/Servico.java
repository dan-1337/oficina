package oficina.model;

public class Servico {
    private int    id;
    private String nome;
    private String descricao;
    private double precoBase;
    private int    tempoEstimado;

    public Servico() {}

    public int    getId()            { return id; }
    public String getNome()          { return nome; }
    public String getDescricao()     { return descricao; }
    public double getPrecoBase()     { return precoBase; }
    public int    getTempoEstimado() { return tempoEstimado; }

    public void setId(int id)                  { this.id = id; }
    public void setNome(String nome)           { this.nome = nome; }
    public void setDescricao(String d)         { this.descricao = d; }
    public void setPrecoBase(double p)         { this.precoBase = p; }
    public void setTempoEstimado(int t)        { this.tempoEstimado = t; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Preço: R$ %.2f | Tempo: %d min | %s",
                id, nome, precoBase, tempoEstimado, descricao);
    }
}
