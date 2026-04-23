package oficina.model;

public class Peca {
    private int    id;
    private String nome;
    private String codigo;
    private String fornecedor;
    private double precoUnitario;
    private int    estoque;

    public Peca() {}

    public int    getId()            { return id; }
    public String getNome()          { return nome; }
    public String getCodigo()        { return codigo; }
    public String getFornecedor()    { return fornecedor; }
    public double getPrecoUnitario() { return precoUnitario; }
    public int    getEstoque()       { return estoque; }

    public void setId(int id)                  { this.id = id; }
    public void setNome(String nome)           { this.nome = nome; }
    public void setCodigo(String codigo)       { this.codigo = codigo; }
    public void setFornecedor(String f)        { this.fornecedor = f; }
    public void setPrecoUnitario(double p)     { this.precoUnitario = p; }
    public void setEstoque(int estoque)        { this.estoque = estoque; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Cód: %s | Preço: R$ %.2f | Estoque: %d | Fornecedor: %s",
                id, nome, codigo, precoUnitario, estoque, fornecedor);
    }
}
