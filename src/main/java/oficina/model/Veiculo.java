package oficina.model;

public class Veiculo {
    private int    id;
    private int    idCliente;
    private String nomeCliente;
    private String placa;
    private String marca;
    private String modelo;
    private int    ano;
    private String cor;
    private String chassi;

    public Veiculo() {}

    public int    getId()          { return id; }
    public int    getIdCliente()   { return idCliente; }
    public String getNomeCliente() { return nomeCliente; }
    public String getPlaca()       { return placa; }
    public String getMarca()       { return marca; }
    public String getModelo()      { return modelo; }
    public int    getAno()         { return ano; }
    public String getCor()         { return cor; }
    public String getChassi()      { return chassi; }

    public void setId(int id)                  { this.id = id; }
    public void setIdCliente(int idCliente)    { this.idCliente = idCliente; }
    public void setNomeCliente(String n)       { this.nomeCliente = n; }
    public void setPlaca(String placa)         { this.placa = placa; }
    public void setMarca(String marca)         { this.marca = marca; }
    public void setModelo(String modelo)       { this.modelo = modelo; }
    public void setAno(int ano)                { this.ano = ano; }
    public void setCor(String cor)             { this.cor = cor; }
    public void setChassi(String chassi)       { this.chassi = chassi; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s (%d) | Placa: %s | Cor: %s | Cliente: %s",
                id, marca, modelo, ano, placa, cor, nomeCliente != null ? nomeCliente : "ID " + idCliente);
    }
}
