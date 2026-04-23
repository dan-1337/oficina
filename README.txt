====================================================================
 SISTEMA DE CONTROLE DE OFICINA MECÂNICA
 README - Instruções de Compilação e Execução
====================================================================

PRÉ-REQUISITOS
--------------
  - Java JDK 17 ou superior
  - PostgreSQL 13 ou superior
  - Arquivo JAR do driver JDBC: postgresql-42.x.x.jar
    Download: https://jdbc.postgresql.org/download/

ESTRUTURA DO PROJETO
--------------------
  oficina/
  ├── src/main/java/oficina/
  │   ├── Main.java                  <- Classe principal
  │   ├── dao/
  │   │   ├── Conexao.java
  │   │   ├── ClienteDAO.java
  │   │   ├── VeiculoDAO.java
  │   │   ├── FuncionarioDAO.java
  │   │   ├── ServicoDAO.java
  │   │   ├── PecaDAO.java
  │   │   └── OrdemServicoDAO.java
  │   ├── model/
  │   │   ├── Cliente.java
  │   │   ├── Veiculo.java
  │   │   ├── Funcionario.java
  │   │   ├── Servico.java
  │   │   ├── Peca.java
  │   │   └── OrdemServico.java
  │   └── ui/
  │       ├── EntradaUtil.java
  │       ├── ClienteUI.java
  │       ├── VeiculoUI.java
  │       ├── FuncionarioUI.java
  │       ├── ServicoUI.java
  │       ├── PecaUI.java
  │       └── OrdemServicoUI.java
  └── src/main/resources/
      └── banco.sql                  <- Script do banco de dados

====================================================================
 PASSO 1 — CONFIGURAR O BANCO DE DADOS
====================================================================

  1. Abra o pgAdmin (ou o terminal psql) e crie o banco:

       CREATE DATABASE oficina_mecanica;

  2. Com o banco selecionado, execute o script de criação:

     Via terminal:
       psql -U postgres -d oficina_mecanica -f src/main/resources/banco.sql

     Ou abra o arquivo banco.sql no pgAdmin (Query Tool) e execute.

  3. O script cria todas as tabelas e insere dados de exemplo
     automaticamente.

====================================================================
 PASSO 2 — CONFIGURAR A CONEXÃO (se necessário)
====================================================================

  Abra o arquivo:
    src/main/java/oficina/dao/Conexao.java

  Ajuste as linhas conforme seu ambiente:

    private static final String URL     = "jdbc:postgresql://localhost:5432/oficina_mecanica";
    private static final String USUARIO = "postgres";   // <- seu usuário PostgreSQL
    private static final String SENHA   = "postgres";   // <- sua senha PostgreSQL

====================================================================
 PASSO 3 — COMPILAÇÃO
====================================================================

  Coloque o JAR do driver na pasta raiz do projeto, ex:
    oficina/postgresql-42.x.x.jar

  No terminal, na pasta raiz (oficina/), execute:

  Linux/macOS:
    find src -name "*.java" > fontes.txt
    javac -cp postgresql-42.x.x.jar -d out @fontes.txt

  Windows (PowerShell):
    Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName } > fontes.txt
    javac -cp postgresql-42.x.x.jar -d out @fontes.txt

  Isso compila tudo para a pasta "out/".

====================================================================
 PASSO 4 — EXECUÇÃO
====================================================================

  Linux/macOS:
    java -cp out:postgresql-42.x.x.jar oficina.Main

  Windows:
    java -cp "out;postgresql-42.x.x.jar" oficina.Main

====================================================================
 FUNCIONALIDADES DISPONÍVEIS
====================================================================

  CADASTROS (CRUD completo):
    - Clientes
    - Veículos
    - Funcionários
    - Serviços
    - Peças / Estoque

  PROCESSOS DE NEGÓCIO:
    - Abrir Ordem de Serviço
    - Adicionar/remover serviços à OS
    - Adicionar/remover peças à OS (com controle de estoque)
    - Alterar status da OS (Aberta → Em Andamento → Concluída)
    - Cálculo automático do valor total da OS

  RELATÓRIOS:
    1. OS por período: lista todas as OS em um intervalo de datas,
       com cliente, veículo, mecânico, status e valor total.
    2. Peças mais utilizadas: ranking de peças usadas no período,
       com quantidade total e receita gerada.
    3. Histórico de veículo: todas as OS de um veículo pela placa,
       com mecânico, datas, diagnóstico e valores.

====================================================================
 OBSERVAÇÕES
====================================================================

  - O sistema roda em modo texto (terminal/console).
  - Caracteres especiais (acentos) requerem terminal UTF-8.
    No Windows, execute antes: chcp 65001
  - O driver PostgreSQL deve ser compatível com a versão instalada.

====================================================================
