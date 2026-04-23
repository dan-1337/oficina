package oficina.model;

public class Cliente {
    private int    id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;
    private String endereco;

    public Cliente() {}
    public Cliente(int id, String nome, String cpf, String telefone, String email, String endereco) {
        this.id = id; this.nome = nome; this.cpf = cpf;
        this.telefone = telefone; this.email = email; this.endereco = endereco;
    }

    public int    getId()       { return id; }
    public String getNome()     { return nome; }
    public String getCpf()      { return cpf; }
    public String getTelefone() { return telefone; }
    public String getEmail()    { return email; }
    public String getEndereco() { return endereco; }

    public void setId(int id)             { this.id = id; }
    public void setNome(String nome)      { this.nome = nome; }
    public void setCpf(String cpf)        { this.cpf = cpf; }
    public void setTelefone(String t)     { this.telefone = t; }
    public void setEmail(String e)        { this.email = e; }
    public void setEndereco(String end)   { this.endereco = end; }

    @Override
    public String toString() {
        return String.format("[%d] %s | CPF: %s | Tel: %s | Email: %s",
                id, nome, cpf, telefone, email);
    }
}
