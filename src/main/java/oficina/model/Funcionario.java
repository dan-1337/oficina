package oficina.model;

public class Funcionario {
    private int    id;
    private String nome;
    private String cpf;
    private String cargo;
    private String especialidade;
    private double salario;

    public Funcionario() {}

    public int    getId()           { return id; }
    public String getNome()         { return nome; }
    public String getCpf()          { return cpf; }
    public String getCargo()        { return cargo; }
    public String getEspecialidade(){ return especialidade; }
    public double getSalario()      { return salario; }

    public void setId(int id)                    { this.id = id; }
    public void setNome(String nome)             { this.nome = nome; }
    public void setCpf(String cpf)               { this.cpf = cpf; }
    public void setCargo(String cargo)           { this.cargo = cargo; }
    public void setEspecialidade(String e)       { this.especialidade = e; }
    public void setSalario(double salario)       { this.salario = salario; }

    @Override
    public String toString() {
        return String.format("[%d] %s | Cargo: %s | Especialidade: %s | Salário: R$ %.2f",
                id, nome, cargo, especialidade, salario);
    }
}
