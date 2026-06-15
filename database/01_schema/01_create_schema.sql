-- =========================================================
-- Sistema de Gestão da Oficina Mecânica AV CAR AUTO CENTER
-- Modelo físico inicial - PostgreSQL
-- =========================================================
-- Observação acadêmica:
-- Este script deve ser executado manualmente no pgAdmin ou psql.
-- A aplicação Spring Boot utiliza spring.jpa.hibernate.ddl-auto=validate,
-- portanto o JPA apenas valida o banco físico e não cria as tabelas.
-- =========================================================

CREATE TABLE IF NOT EXISTS pessoa (
    id_pessoa BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    telefone VARCHAR(30),
    email VARCHAR(150),
    endereco VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cliente (
    id_cliente BIGSERIAL PRIMARY KEY,
    id_pessoa BIGINT NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_cliente_pessoa FOREIGN KEY (id_pessoa) REFERENCES pessoa(id_pessoa)
);

CREATE TABLE IF NOT EXISTS pessoa_fisica (
    id_cliente BIGINT PRIMARY KEY,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    rg VARCHAR(30),
    data_nascimento DATE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_pessoa_fisica_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

CREATE TABLE IF NOT EXISTS pessoa_juridica (
    id_cliente BIGINT PRIMARY KEY,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    razao_social VARCHAR(180) NOT NULL,
    nome_fantasia VARCHAR(180),
    inscricao_estadual VARCHAR(40),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_pessoa_juridica_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

CREATE TABLE IF NOT EXISTS colaborador (
    id_colaborador BIGSERIAL PRIMARY KEY,
    id_pessoa BIGINT NOT NULL UNIQUE,
    data_admissao DATE,
    status_colaborador VARCHAR(30) NOT NULL DEFAULT 'ATIVO',
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_colaborador_pessoa FOREIGN KEY (id_pessoa) REFERENCES pessoa(id_pessoa),
    CONSTRAINT ck_status_colaborador CHECK (status_colaborador IN ('ATIVO', 'AFASTADO', 'DESLIGADO'))
);

CREATE TABLE IF NOT EXISTS funcao (
    id_funcao BIGSERIAL PRIMARY KEY,
    nome_funcao VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP
);

CREATE TABLE IF NOT EXISTS colaborador_funcao (
    id_colaborador_funcao BIGSERIAL PRIMARY KEY,
    id_colaborador BIGINT,
    id_funcao BIGINT NOT NULL,
    data_inicio DATE NOT NULL DEFAULT CURRENT_DATE,
    data_fim DATE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_colaborador_funcao_colaborador FOREIGN KEY (id_colaborador) REFERENCES colaborador(id_colaborador),
    CONSTRAINT fk_colaborador_funcao_funcao FOREIGN KEY (id_funcao) REFERENCES funcao(id_funcao),
    CONSTRAINT ck_colaborador_funcao_periodo CHECK (data_fim IS NULL OR data_fim >= data_inicio)
);

CREATE TABLE IF NOT EXISTS marca (
    id_marca BIGSERIAL PRIMARY KEY,
    nome_marca VARCHAR(100) NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP
);

CREATE TABLE IF NOT EXISTS modelo (
    id_modelo BIGSERIAL PRIMARY KEY,
    id_marca BIGINT NOT NULL,
    nome_modelo VARCHAR(100) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_modelo_marca FOREIGN KEY (id_marca) REFERENCES marca(id_marca),
    CONSTRAINT uk_modelo_marca_nome UNIQUE (id_marca, nome_modelo)
);

CREATE TABLE IF NOT EXISTS veiculo (
    id_veiculo BIGSERIAL PRIMARY KEY,
    id_modelo BIGINT NOT NULL,
    placa VARCHAR(10) NOT NULL,
    chassi VARCHAR(30),
    cor VARCHAR(50),
    ano_veiculo INTEGER NOT NULL,
    ano_modelo INTEGER NOT NULL,
    quilometragem_atual INTEGER DEFAULT 0,
    observacao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_veiculo_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id_modelo),
    CONSTRAINT ck_veiculo_ano CHECK (ano_veiculo >= 1900 AND ano_modelo >= 1900),
    CONSTRAINT ck_veiculo_quilometragem CHECK (quilometragem_atual IS NULL OR quilometragem_atual >= 0)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_veiculo_placa_ativo ON veiculo(placa) WHERE ativo = TRUE;
CREATE UNIQUE INDEX IF NOT EXISTS uk_veiculo_chassi_ativo ON veiculo(chassi) WHERE ativo = TRUE AND chassi IS NOT NULL;

CREATE TABLE IF NOT EXISTS historico_proprietario (
    id_historico_proprietario BIGSERIAL PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    id_veiculo BIGINT NOT NULL,
    data_inicio_posse DATE NOT NULL,
    data_fim_posse DATE,
    proprietario_atual BOOLEAN NOT NULL DEFAULT TRUE,
    observacao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_historico_proprietario_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    CONSTRAINT fk_historico_proprietario_veiculo FOREIGN KEY (id_veiculo) REFERENCES veiculo(id_veiculo),
    CONSTRAINT ck_historico_proprietario_periodo CHECK (data_fim_posse IS NULL OR data_fim_posse >= data_inicio_posse)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_historico_proprietario_atual
    ON historico_proprietario(id_veiculo)
    WHERE proprietario_atual = TRUE AND ativo = TRUE;

CREATE TABLE IF NOT EXISTS status_ordem_servico (
    id_status_ordem_servico BIGSERIAL PRIMARY KEY,
    nome_status VARCHAR(40) NOT NULL UNIQUE,
    ordem_fluxo INTEGER NOT NULL UNIQUE,
    descricao VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP
);

CREATE TABLE IF NOT EXISTS ordem_servico (
    id_ordem_servico BIGSERIAL PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    id_veiculo BIGINT NOT NULL,
    numero_os VARCHAR(30) NOT NULL UNIQUE,
    data_abertura TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_aprovacao TIMESTAMP,
    data_finalizacao TIMESTAMP,
    prioridade VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    valor_total NUMERIC(12,2) NOT NULL DEFAULT 0,
    observacao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_ordem_servico_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
    CONSTRAINT fk_ordem_servico_veiculo FOREIGN KEY (id_veiculo) REFERENCES veiculo(id_veiculo),
    CONSTRAINT ck_ordem_servico_prioridade CHECK (prioridade IN ('BAIXA', 'NORMAL', 'ALTA', 'URGENTE')),
    CONSTRAINT ck_ordem_servico_valor CHECK (valor_total >= 0)
);

CREATE TABLE IF NOT EXISTS historico_status_ordem (
    id_historico_status_ordem BIGSERIAL PRIMARY KEY,
    id_ordem_servico BIGINT NOT NULL,
    id_status_ordem_servico BIGINT NOT NULL,
    data_status TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_historico_status_ordem_os FOREIGN KEY (id_ordem_servico) REFERENCES ordem_servico(id_ordem_servico),
    CONSTRAINT fk_historico_status_ordem_status FOREIGN KEY (id_status_ordem_servico) REFERENCES status_ordem_servico(id_status_ordem_servico)
);

CREATE TABLE IF NOT EXISTS servico (
    id_servico BIGSERIAL PRIMARY KEY,
    nome_servico VARCHAR(150) NOT NULL UNIQUE,
    descricao TEXT,
    prazo_garantia_dias INTEGER NOT NULL DEFAULT 90,
    valor_base NUMERIC(12,2) NOT NULL DEFAULT 0,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT ck_servico_garantia CHECK (prazo_garantia_dias >= 0),
    CONSTRAINT ck_servico_valor CHECK (valor_base >= 0)
);

CREATE TABLE IF NOT EXISTS servico_interno (
    id_servico BIGINT PRIMARY KEY,
    observacao_interna TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_servico_interno_servico FOREIGN KEY (id_servico) REFERENCES servico(id_servico)
);

CREATE TABLE IF NOT EXISTS empresa_terceirizada (
    id_empresa_terceirizada BIGSERIAL PRIMARY KEY,
    nome_empresa VARCHAR(180) NOT NULL,
    cnpj VARCHAR(18),
    telefone VARCHAR(30),
    email VARCHAR(150),
    endereco VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP
);

CREATE TABLE IF NOT EXISTS servico_terceirizado (
    id_servico BIGINT PRIMARY KEY,
    id_empresa_terceirizada_padrao BIGINT NOT NULL,
    observacao_terceirizacao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_servico_terceirizado_servico FOREIGN KEY (id_servico) REFERENCES servico(id_servico),
    CONSTRAINT fk_servico_terceirizado_empresa_padrao FOREIGN KEY (id_empresa_terceirizada_padrao) REFERENCES empresa_terceirizada(id_empresa_terceirizada)
);

CREATE TABLE IF NOT EXISTS item_servico (
    id_item_servico BIGSERIAL PRIMARY KEY,
    id_ordem_servico BIGINT NOT NULL,
    id_servico BIGINT NOT NULL,
    id_colaborador BIGINT,
    descricao_execucao TEXT,
    quantidade NUMERIC(10,2) NOT NULL DEFAULT 1,
    valor_unitario NUMERIC(12,2) NOT NULL DEFAULT 0,
    valor_total NUMERIC(12,2) NOT NULL DEFAULT 0,
    data_inicio TIMESTAMP,
    data_fim TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_item_servico_os FOREIGN KEY (id_ordem_servico) REFERENCES ordem_servico(id_ordem_servico),
    CONSTRAINT fk_item_servico_servico FOREIGN KEY (id_servico) REFERENCES servico(id_servico),
    CONSTRAINT fk_item_servico_colaborador FOREIGN KEY (id_colaborador) REFERENCES colaborador(id_colaborador),
    CONSTRAINT ck_item_servico_quantidade CHECK (quantidade > 0),
    CONSTRAINT ck_item_servico_valor_unitario CHECK (valor_unitario >= 0),
    CONSTRAINT ck_item_servico_valor_total CHECK (valor_total >= 0)
);

CREATE TABLE IF NOT EXISTS execucao_servico_terceirizado (
    id_execucao_servico_terceirizado BIGSERIAL PRIMARY KEY,
    id_item_servico BIGINT NOT NULL UNIQUE,
    id_empresa_terceirizada BIGINT NOT NULL,
    data_envio TIMESTAMP,
    data_retorno TIMESTAMP,
    valor_cobrado NUMERIC(12,2) NOT NULL DEFAULT 0,
    observacao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_execucao_terceirizada_item_servico FOREIGN KEY (id_item_servico) REFERENCES item_servico(id_item_servico),
    CONSTRAINT fk_execucao_terceirizada_empresa FOREIGN KEY (id_empresa_terceirizada) REFERENCES empresa_terceirizada(id_empresa_terceirizada),
    CONSTRAINT ck_execucao_terceirizada_valor CHECK (valor_cobrado >= 0)
);

CREATE TABLE IF NOT EXISTS fornecedor (
    id_fornecedor BIGSERIAL PRIMARY KEY,
    nome_fornecedor VARCHAR(180) NOT NULL,
    cnpj VARCHAR(18),
    telefone VARCHAR(30),
    email VARCHAR(150),
    endereco VARCHAR(255),
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_fornecedor_cnpj_ativo
    ON fornecedor(cnpj)
    WHERE ativo = TRUE AND cnpj IS NOT NULL;

CREATE TABLE IF NOT EXISTS peca (
    id_peca BIGSERIAL PRIMARY KEY,
    nome_peca VARCHAR(180) NOT NULL,
    codigo_nacional VARCHAR(60),
    marca_peca VARCHAR(100),
    modelo_aplicavel VARCHAR(100),
    ano_veiculo INTEGER,
    ano_modelo INTEGER,
    id_fornecedor_padrao BIGINT NOT NULL,
    valor_unitario_padrao NUMERIC(12,2) NOT NULL DEFAULT 0,
    prazo_garantia_dias INTEGER NOT NULL DEFAULT 90,
    descricao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_peca_fornecedor_padrao FOREIGN KEY (id_fornecedor_padrao) REFERENCES fornecedor(id_fornecedor),
    CONSTRAINT ck_peca_anos CHECK ((ano_veiculo IS NULL OR ano_veiculo >= 1900) AND (ano_modelo IS NULL OR ano_modelo >= 1900)),
    CONSTRAINT ck_peca_valor_padrao CHECK (valor_unitario_padrao >= 0),
    CONSTRAINT ck_peca_garantia CHECK (prazo_garantia_dias >= 0)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_peca_codigo_nacional_ativo
    ON peca(codigo_nacional)
    WHERE ativo = TRUE AND codigo_nacional IS NOT NULL;

CREATE TABLE IF NOT EXISTS item_peca (
    id_item_peca BIGSERIAL PRIMARY KEY,
    id_ordem_servico BIGINT NOT NULL,
    id_peca BIGINT NOT NULL,
    id_fornecedor BIGINT NOT NULL,
    quantidade NUMERIC(10,2) NOT NULL DEFAULT 1,
    valor_unitario NUMERIC(12,2) NOT NULL DEFAULT 0,
    valor_total NUMERIC(12,2) NOT NULL DEFAULT 0,
    observacao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_item_peca_os FOREIGN KEY (id_ordem_servico) REFERENCES ordem_servico(id_ordem_servico),
    CONSTRAINT fk_item_peca_peca FOREIGN KEY (id_peca) REFERENCES peca(id_peca),
    CONSTRAINT fk_item_peca_fornecedor FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id_fornecedor),
    CONSTRAINT ck_item_peca_quantidade CHECK (quantidade > 0),
    CONSTRAINT ck_item_peca_valor_unitario CHECK (valor_unitario >= 0),
    CONSTRAINT ck_item_peca_valor_total CHECK (valor_total >= 0)
);

CREATE TABLE IF NOT EXISTS garantia_peca (
    id_garantia_peca BIGSERIAL PRIMARY KEY,
    id_item_peca BIGINT NOT NULL UNIQUE,
    prazo_dias INTEGER NOT NULL,
    data_inicio DATE,
    data_fim DATE,
    responsabilidade VARCHAR(30) NOT NULL DEFAULT 'FORNECEDOR',
    status_garantia VARCHAR(30) NOT NULL DEFAULT 'AGUARDANDO_FINALIZACAO_OS',
    data_acionamento DATE,
    motivo_acionamento VARCHAR(255),
    descricao_defeito TEXT,
    responsavel_analise VARCHAR(150),
    data_encerramento DATE,
    solucao_aplicada TEXT,
    custo_assumido_por VARCHAR(80),
    atendimento_realizado BOOLEAN,
    observacao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_garantia_peca_item_peca FOREIGN KEY (id_item_peca) REFERENCES item_peca(id_item_peca),
    CONSTRAINT ck_garantia_peca_prazo CHECK (prazo_dias >= 0),
    CONSTRAINT ck_garantia_peca_responsabilidade CHECK (responsabilidade IN ('OFICINA', 'FORNECEDOR', 'AMBOS')),
    CONSTRAINT ck_garantia_peca_status CHECK (status_garantia IN ('AGUARDANDO_FINALIZACAO_OS', 'VIGENTE', 'EXPIRADA', 'ACIONADA', 'ENCERRADA'))
);

CREATE TABLE IF NOT EXISTS garantia_servico (
    id_garantia_servico BIGSERIAL PRIMARY KEY,
    id_item_servico BIGINT NOT NULL UNIQUE,
    prazo_dias INTEGER NOT NULL,
    data_inicio DATE,
    data_fim DATE,
    status_garantia VARCHAR(30) NOT NULL DEFAULT 'AGUARDANDO_FINALIZACAO_OS',
    data_acionamento DATE,
    motivo_acionamento VARCHAR(255),
    descricao_defeito TEXT,
    responsavel_analise VARCHAR(150),
    data_encerramento DATE,
    solucao_aplicada TEXT,
    custo_assumido_por VARCHAR(80),
    atendimento_realizado BOOLEAN,
    observacao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_garantia_servico_item_servico FOREIGN KEY (id_item_servico) REFERENCES item_servico(id_item_servico),
    CONSTRAINT ck_garantia_servico_prazo CHECK (prazo_dias >= 0),
    CONSTRAINT ck_garantia_servico_status CHECK (status_garantia IN ('AGUARDANDO_FINALIZACAO_OS', 'VIGENTE', 'EXPIRADA', 'ACIONADA', 'ENCERRADA'))
);

CREATE TABLE IF NOT EXISTS pagamento (
    id_pagamento BIGSERIAL PRIMARY KEY,
    id_ordem_servico BIGINT NOT NULL,
    forma_pagamento VARCHAR(40) NOT NULL,
    valor_pago NUMERIC(12,2) NOT NULL,
    data_pagamento TIMESTAMP,
    status_pagamento VARCHAR(30) NOT NULL DEFAULT 'PENDENTE',
    observacao TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP,
    CONSTRAINT fk_pagamento_ordem_servico FOREIGN KEY (id_ordem_servico) REFERENCES ordem_servico(id_ordem_servico),
    CONSTRAINT ck_pagamento_valor CHECK (valor_pago > 0),
    CONSTRAINT ck_pagamento_forma CHECK (forma_pagamento IN ('DINHEIRO', 'PIX', 'CARTAO_DEBITO', 'CARTAO_CREDITO', 'TRANSFERENCIA', 'BOLETO', 'OUTRO')),
    CONSTRAINT ck_pagamento_status CHECK (status_pagamento IN ('PENDENTE', 'PAGO', 'CANCELADO', 'ESTORNADO'))
);

CREATE INDEX IF NOT EXISTS idx_pagamento_ordem_servico
    ON pagamento(id_ordem_servico)
    WHERE ativo = TRUE;

CREATE TABLE IF NOT EXISTS notificacao_auditoria (
    id_notificacao_auditoria BIGSERIAL PRIMARY KEY,
    modulo VARCHAR(80) NOT NULL,
    referencia VARCHAR(80) NOT NULL,
    canal VARCHAR(60) NOT NULL,
    mensagem_original TEXT,
    mensagem_processada TEXT NOT NULL,
    entregue BOOLEAN NOT NULL DEFAULT TRUE,
    auditoria_registrada BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_envio TIMESTAMP NOT NULL,
    data_hora_auditoria TIMESTAMP NOT NULL,
    observacao_auditoria TEXT,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_hora_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_hora_atualizacao TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_notificacao_auditoria_referencia
    ON notificacao_auditoria(referencia)
    WHERE ativo = TRUE;

CREATE INDEX IF NOT EXISTS idx_notificacao_auditoria_modulo_data
    ON notificacao_auditoria(modulo, data_hora_auditoria DESC)
    WHERE ativo = TRUE;
