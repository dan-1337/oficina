-- ============================================================
-- SISTEMA DE CONTROLE DE OFICINA MECÂNICA
-- Script de criação e população do banco de dados (PostgreSQL)
-- ============================================================

-- Execute este script conectado ao banco oficina_mecanica
-- Crie o banco antes: CREATE DATABASE oficina_mecanica;

-- ------------------------------------------------------------
-- ENTIDADES PRINCIPAIS
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS cliente (
    id_cliente   SERIAL PRIMARY KEY,
    nome         VARCHAR(100) NOT NULL,
    cpf          VARCHAR(14)  NOT NULL UNIQUE,
    telefone     VARCHAR(20),
    email        VARCHAR(100),
    endereco     VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS veiculo (
    id_veiculo   SERIAL PRIMARY KEY,
    id_cliente   INT          NOT NULL,
    placa        VARCHAR(10)  NOT NULL UNIQUE,
    marca        VARCHAR(50)  NOT NULL,
    modelo       VARCHAR(50)  NOT NULL,
    ano          INT,
    cor          VARCHAR(30),
    chassi       VARCHAR(50),
    CONSTRAINT fk_veiculo_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

CREATE TABLE IF NOT EXISTS funcionario (
    id_funcionario SERIAL PRIMARY KEY,
    nome           VARCHAR(100) NOT NULL,
    cpf            VARCHAR(14)  NOT NULL UNIQUE,
    cargo          VARCHAR(50),
    especialidade  VARCHAR(100),
    salario        DECIMAL(10,2)
);

CREATE TABLE IF NOT EXISTS servico (
    id_servico     SERIAL PRIMARY KEY,
    nome           VARCHAR(100) NOT NULL,
    descricao      VARCHAR(255),
    preco_base     DECIMAL(10,2) NOT NULL,
    tempo_estimado INT  -- em minutos
);

CREATE TABLE IF NOT EXISTS peca (
    id_peca        SERIAL PRIMARY KEY,
    nome           VARCHAR(100) NOT NULL,
    codigo         VARCHAR(50)  NOT NULL UNIQUE,
    fornecedor     VARCHAR(100),
    preco_unitario DECIMAL(10,2) NOT NULL,
    estoque        INT DEFAULT 0
);

-- ------------------------------------------------------------
-- TABELAS ASSOCIATIVAS
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS ordem_servico (
    id_os          SERIAL PRIMARY KEY,
    id_veiculo     INT          NOT NULL,
    id_funcionario INT          NOT NULL,
    data_entrada   DATE         NOT NULL,
    data_saida     DATE,
    status         VARCHAR(20)  NOT NULL DEFAULT 'ABERTA'
                       CHECK (status IN ('ABERTA','EM_ANDAMENTO','CONCLUIDA','CANCELADA')),
    valor_total    DECIMAL(10,2) DEFAULT 0.00,
    diagnostico    TEXT,
    CONSTRAINT fk_os_veiculo     FOREIGN KEY (id_veiculo)     REFERENCES veiculo(id_veiculo),
    CONSTRAINT fk_os_funcionario FOREIGN KEY (id_funcionario) REFERENCES funcionario(id_funcionario)
);

CREATE TABLE IF NOT EXISTS os_servico (
    id_os          INT NOT NULL,
    id_servico     INT NOT NULL,
    quantidade     INT          NOT NULL DEFAULT 1,
    preco_cobrado  DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_os, id_servico),
    CONSTRAINT fk_osserv_os      FOREIGN KEY (id_os)      REFERENCES ordem_servico(id_os),
    CONSTRAINT fk_osserv_servico FOREIGN KEY (id_servico) REFERENCES servico(id_servico)
);

CREATE TABLE IF NOT EXISTS os_peca (
    id_os          INT NOT NULL,
    id_peca        INT NOT NULL,
    quantidade     INT          NOT NULL DEFAULT 1,
    preco_cobrado  DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id_os, id_peca),
    CONSTRAINT fk_ospeca_os   FOREIGN KEY (id_os)   REFERENCES ordem_servico(id_os),
    CONSTRAINT fk_ospeca_peca FOREIGN KEY (id_peca) REFERENCES peca(id_peca)
);

-- ------------------------------------------------------------
-- DADOS INICIAIS
-- ------------------------------------------------------------

INSERT INTO cliente (nome, cpf, telefone, email, endereco) VALUES
('Carlos Silva',     '111.111.111-11', '(47) 99901-1111', 'carlos@email.com',  'Rua das Flores, 10 - Joinville/SC'),
('Ana Souza',        '222.222.222-22', '(47) 99902-2222', 'ana@email.com',     'Av. Brasil, 200 - Joinville/SC'),
('Pedro Oliveira',   '333.333.333-33', '(47) 99903-3333', 'pedro@email.com',   'Rua XV, 55 - Joinville/SC'),
('Mariana Lima',     '444.444.444-44', '(47) 99904-4444', 'mariana@email.com', 'Rua Blumenau, 80 - Joinville/SC'),
('Roberto Costa',    '555.555.555-55', '(47) 99905-5555', 'roberto@email.com', 'Rua Iririú, 33 - Joinville/SC');

INSERT INTO veiculo (id_cliente, placa, marca, modelo, ano, cor, chassi) VALUES
(1, 'ABC-1234', 'Volkswagen', 'Gol',     2018, 'Branco',  'VW1ABC1234XXXXX01'),
(1, 'DEF-5678', 'Fiat',       'Uno',     2015, 'Prata',   'FT2DEF5678XXXXX02'),
(2, 'GHI-9012', 'Chevrolet',  'Onix',    2020, 'Preto',   'CH3GHI9012XXXXX03'),
(3, 'JKL-3456', 'Ford',       'Ka',      2017, 'Vermelho','FO4JKL3456XXXXX04'),
(4, 'MNO-7890', 'Toyota',     'Corolla', 2022, 'Azul',    'TO5MNO7890XXXXX05'),
(5, 'PQR-1122', 'Honda',      'Civic',   2019, 'Cinza',   'HO6PQR1122XXXXX06');

INSERT INTO funcionario (nome, cpf, cargo, especialidade, salario) VALUES
('João Mecânico',   '666.666.666-66', 'Mecânico Sênior', 'Motor e Câmbio',      4500.00),
('Lucas Elétrico',  '777.777.777-77', 'Eletricista',     'Sistemas Elétricos',  3800.00),
('Fernanda Freios', '888.888.888-88', 'Mecânica',        'Freios e Suspensão',  3600.00),
('Rafael Funileiro','999.999.999-99', 'Funileiro',       'Lataria e Pintura',   3200.00),
('Adriana Gestora', '000.000.000-00', 'Gerente',         'Administração',       5500.00);

INSERT INTO servico (nome, descricao, preco_base, tempo_estimado) VALUES
('Troca de Óleo',        'Troca de óleo do motor + filtro',         80.00,  60),
('Alinhamento',          'Alinhamento de direção',                  70.00,  45),
('Balanceamento',        'Balanceamento de rodas',                  60.00,  45),
('Revisão Geral',        'Revisão completa do veículo',            350.00, 240),
('Troca de Pastilhas',   'Substituição de pastilhas de freio',     120.00,  90),
('Diagnóstico Elétrico', 'Diagnóstico do sistema elétrico',       100.00,  60),
('Troca de Correia',     'Substituição da correia dentada',        200.00, 120),
('Higienização A/C',     'Limpeza do sistema de ar-condicionado',  90.00,  60);

INSERT INTO peca (nome, codigo, fornecedor, preco_unitario, estoque) VALUES
('Filtro de Óleo',       'FO-001', 'Mann Filter',   25.00, 50),
('Filtro de Ar',         'FA-002', 'Mann Filter',   30.00, 40),
('Pastilha de Freio Dianteira', 'PF-003', 'Bosch',  85.00, 30),
('Vela de Ignição',      'VI-004', 'NGK',           18.00, 60),
('Correia Dentada',      'CD-005', 'Gates',        120.00, 20),
('Amortecedor Dianteiro','AD-006', 'Monroe',       280.00, 15),
('Óleo Motor 5W30 1L',   'OM-007', 'Mobil',        38.00, 80),
('Fluido de Freio',      'FF-008', 'Bosch',         22.00, 35);

INSERT INTO ordem_servico (id_veiculo, id_funcionario, data_entrada, data_saida, status, valor_total, diagnostico) VALUES
(1, 1, '2024-03-01', '2024-03-01', 'CONCLUIDA',    193.00, 'Troca de óleo e filtros de rotina'),
(2, 3, '2024-03-05', '2024-03-05', 'CONCLUIDA',    205.00, 'Pastilhas gastas, substituídas'),
(3, 1, '2024-03-10', '2024-03-12', 'CONCLUIDA',    520.00, 'Revisão completa + correia dentada'),
(4, 2, '2024-04-01', '2024-04-01', 'CONCLUIDA',    170.00, 'Falha no alternador diagnosticada e corrigida'),
(5, 1, '2024-04-15', NULL,         'EM_ANDAMENTO',   0.00, 'Barulho no motor em análise'),
(6, 3, '2024-04-20', NULL,         'ABERTA',          0.00, 'Aguardando peças para freio');

INSERT INTO os_servico (id_os, id_servico, quantidade, preco_cobrado) VALUES
(1, 1, 1,  80.00),
(2, 5, 1, 120.00),
(3, 4, 1, 350.00),
(3, 7, 1, 200.00),
(4, 6, 1, 100.00);

INSERT INTO os_peca (id_os, id_peca, quantidade, preco_cobrado) VALUES
(1, 1, 1,  25.00),
(1, 7, 1,  38.00),
(1, 2, 1,  30.00),
(2, 3, 1,  85.00),
(3, 5, 1, 120.00),
(4, 4, 2,  36.00);
