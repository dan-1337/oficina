package oficina.model;

import java.time.LocalDate;

public class OrdemServico {
    private int       id;
    private int       idVeiculo;
    private int       idFuncionario;
    private String    placaVeiculo;
    private String    nomeFuncionario;
    private String    nomeCliente;
    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private String    status;
    private double    valorTotal;
    private String    diagnostico;

    public OrdemServico() {}

    public int       getId()              { return id; }
    public int       getIdVeiculo()       { return idVeiculo; }
    public int       getIdFuncionario()   { return idFuncionario; }
    public String    getPlacaVeiculo()    { return placaVeiculo; }
    public String    getNomeFuncionario() { return nomeFuncionario; }
    public String    getNomeCliente()     { return nomeCliente; }
    public LocalDate getDataEntrada()     { return dataEntrada; }
    public LocalDate getDataSaida()       { return dataSaida; }
    public String    getStatus()          { return status; }
    public double    getValorTotal()      { return valorTotal; }
    public String    getDiagnostico()     { return diagnostico; }

    public void setId(int id)                       { this.id = id; }
    public void setIdVeiculo(int idVeiculo)         { this.idVeiculo = idVeiculo; }
    public void setIdFuncionario(int idFuncionario) { this.idFuncionario = idFuncionario; }
    public void setPlacaVeiculo(String p)           { this.placaVeiculo = p; }
    public void setNomeFuncionario(String n)        { this.nomeFuncionario = n; }
    public void setNomeCliente(String n)            { this.nomeCliente = n; }
    public void setDataEntrada(LocalDate d)         { this.dataEntrada = d; }
    public void setDataSaida(LocalDate d)           { this.dataSaida = d; }
    public void setStatus(String status)            { this.status = status; }
    public void setValorTotal(double v)             { this.valorTotal = v; }
    public void setDiagnostico(String d)            { this.diagnostico = d; }

    @Override
    public String toString() {
        return String.format("OS #%d | Veículo: %s | Mecânico: %s | Entrada: %s | Status: %s | Total: R$ %.2f",
                id,
                placaVeiculo    != null ? placaVeiculo    : "ID " + idVeiculo,
                nomeFuncionario != null ? nomeFuncionario : "ID " + idFuncionario,
                dataEntrada, status, valorTotal);
    }
}
