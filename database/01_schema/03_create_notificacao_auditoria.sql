-- Etapa 57: tabela de auditoria persistente das notificações operacionais.
-- Esta tabela fortalece o padrão Decorator, registrando em banco as notificações
-- geradas por eventos importantes, como mudança de status de Ordem de Serviço.

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
