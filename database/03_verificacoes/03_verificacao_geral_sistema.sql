-- =========================================================
-- Sistema de Gestão da Oficina Mecânica AV CAR AUTO CENTER
-- Script unificado de verificações - PostgreSQL
-- =========================================================
-- Objetivo:
-- Substituir os vários scripts antigos de verificação por um único
-- arquivo organizado, permitindo conferir rapidamente se o banco,
-- as seeds, o frontend, o fluxo de OS, pagamentos, garantias e PDF
-- possuem dados consistentes para demonstração.
--
-- Este script NÃO altera estrutura e NÃO insere dados.
-- Execute depois de:
--   1. database/01_schema/01_create_schema.sql
--   2. database/02_seed/02_seed_inicial.sql
-- =========================================================

-- =========================================================
-- 1. Diagnóstico básico da conexão
-- =========================================================
SELECT
    NOW() AS data_hora_teste,
    CURRENT_DATABASE() AS banco_atual,
    CURRENT_USER AS usuario_atual,
    VERSION() AS versao_postgresql;

-- =========================================================
-- 2. Quantidade geral de registros por tabela principal
-- =========================================================
SELECT 'pessoa' AS tabela, COUNT(*) AS total, COUNT(*) FILTER (WHERE ativo = TRUE) AS ativos FROM pessoa
UNION ALL SELECT 'cliente', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM cliente
UNION ALL SELECT 'pessoa_fisica', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM pessoa_fisica
UNION ALL SELECT 'pessoa_juridica', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM pessoa_juridica
UNION ALL SELECT 'colaborador', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM colaborador
UNION ALL SELECT 'funcao', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM funcao
UNION ALL SELECT 'colaborador_funcao', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM colaborador_funcao
UNION ALL SELECT 'marca', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM marca
UNION ALL SELECT 'modelo', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM modelo
UNION ALL SELECT 'veiculo', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM veiculo
UNION ALL SELECT 'historico_proprietario', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM historico_proprietario
UNION ALL SELECT 'status_ordem_servico', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM status_ordem_servico
UNION ALL SELECT 'ordem_servico', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM ordem_servico
UNION ALL SELECT 'historico_status_ordem', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM historico_status_ordem
UNION ALL SELECT 'servico', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM servico
UNION ALL SELECT 'servico_interno', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM servico_interno
UNION ALL SELECT 'servico_terceirizado', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM servico_terceirizado
UNION ALL SELECT 'empresa_terceirizada', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM empresa_terceirizada
UNION ALL SELECT 'item_servico', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM item_servico
UNION ALL SELECT 'execucao_servico_terceirizado', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM execucao_servico_terceirizado
UNION ALL SELECT 'fornecedor', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM fornecedor
UNION ALL SELECT 'peca', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM peca
UNION ALL SELECT 'item_peca', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM item_peca
UNION ALL SELECT 'garantia_peca', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM garantia_peca
UNION ALL SELECT 'garantia_servico', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM garantia_servico
UNION ALL SELECT 'pagamento', COUNT(*), COUNT(*) FILTER (WHERE ativo = TRUE) FROM pagamento
ORDER BY tabela;

-- =========================================================
-- 3. Verificação dos dados necessários para o frontend Angular
-- =========================================================
SELECT 'funcoes_para_colaboradores' AS verificacao, COUNT(*) AS total FROM funcao WHERE ativo = TRUE
UNION ALL SELECT 'marcas_para_veiculos', COUNT(*) FROM marca WHERE ativo = TRUE
UNION ALL SELECT 'modelos_para_veiculos', COUNT(*) FROM modelo WHERE ativo = TRUE
UNION ALL SELECT 'clientes_para_os', COUNT(*) FROM cliente WHERE ativo = TRUE
UNION ALL SELECT 'veiculos_para_os', COUNT(*) FROM veiculo WHERE ativo = TRUE
UNION ALL SELECT 'colaboradores_para_item_servico', COUNT(*) FROM colaborador WHERE ativo = TRUE
UNION ALL SELECT 'servicos_para_os', COUNT(*) FROM servico WHERE ativo = TRUE
UNION ALL SELECT 'pecas_para_os', COUNT(*) FROM peca WHERE ativo = TRUE
UNION ALL SELECT 'fornecedores_para_item_peca', COUNT(*) FROM fornecedor WHERE ativo = TRUE
UNION ALL SELECT 'empresas_terceirizadas', COUNT(*) FROM empresa_terceirizada WHERE ativo = TRUE
UNION ALL SELECT 'ordens_servico_operacionais', COUNT(*) FROM ordem_servico WHERE ativo = TRUE;

-- =========================================================
-- 4. Clientes com tipo PF/PJ
-- =========================================================
SELECT
    c.id_cliente,
    p.nome,
    CASE
        WHEN pf.id_cliente IS NOT NULL THEN 'PESSOA_FISICA'
        WHEN pj.id_cliente IS NOT NULL THEN 'PESSOA_JURIDICA'
        ELSE 'NAO_CLASSIFICADO'
    END AS tipo_cliente,
    COALESCE(pf.cpf, pj.cnpj) AS documento,
    p.telefone,
    p.email,
    c.ativo
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
LEFT JOIN pessoa_fisica pf ON pf.id_cliente = c.id_cliente
LEFT JOIN pessoa_juridica pj ON pj.id_cliente = c.id_cliente
ORDER BY c.id_cliente;

-- =========================================================
-- 5. Colaboradores com funções vinculadas
-- =========================================================
SELECT
    col.id_colaborador,
    p.nome AS colaborador,
    col.status_colaborador,
    p.telefone,
    p.email,
    STRING_AGG(f.nome_funcao, ', ' ORDER BY f.nome_funcao) AS funcoes
FROM colaborador col
JOIN pessoa p ON p.id_pessoa = col.id_pessoa
LEFT JOIN colaborador_funcao cf ON cf.id_colaborador = col.id_colaborador AND cf.ativo = TRUE
LEFT JOIN funcao f ON f.id_funcao = cf.id_funcao
GROUP BY col.id_colaborador, p.nome, col.status_colaborador, p.telefone, p.email
ORDER BY col.id_colaborador;

-- =========================================================
-- 6. Veículos com proprietário atual, marca e modelo
-- =========================================================
SELECT
    v.id_veiculo,
    v.placa,
    ma.nome_marca,
    mo.nome_modelo,
    v.ano_veiculo,
    v.ano_modelo,
    v.quilometragem_atual,
    pc.nome AS proprietario_atual,
    hp.data_inicio_posse
FROM veiculo v
JOIN modelo mo ON mo.id_modelo = v.id_modelo
JOIN marca ma ON ma.id_marca = mo.id_marca
LEFT JOIN historico_proprietario hp
    ON hp.id_veiculo = v.id_veiculo
   AND hp.proprietario_atual = TRUE
   AND hp.ativo = TRUE
LEFT JOIN cliente c ON c.id_cliente = hp.id_cliente
LEFT JOIN pessoa pc ON pc.id_pessoa = c.id_pessoa
ORDER BY v.id_veiculo;


-- =========================================================
-- 6.1. Histórico de proprietários por cliente
-- A consulta abaixo deve mostrar cada cliente uma vez, com a quantidade de veículos
-- que possui atualmente e a quantidade de registros históricos preservados.
-- =========================================================
SELECT
    pc.nome AS cliente,
    COUNT(*) AS registros_de_posse,
    COUNT(DISTINCT hp.id_veiculo) AS veiculos_no_historico,
    COUNT(*) FILTER (WHERE hp.proprietario_atual = TRUE) AS posses_atuais,
    STRING_AGG(DISTINCT v.placa, ', ' ORDER BY v.placa) AS placas_relacionadas
FROM historico_proprietario hp
JOIN cliente c ON c.id_cliente = hp.id_cliente
JOIN pessoa pc ON pc.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.id_veiculo = hp.id_veiculo
WHERE hp.ativo = TRUE
GROUP BY pc.nome
ORDER BY pc.nome;

-- Detalhe das posses, incluindo início, fim e status atual/anterior.
SELECT
    pc.nome AS cliente,
    v.placa,
    ma.nome_marca,
    mo.nome_modelo,
    hp.data_inicio_posse,
    hp.data_fim_posse,
    CASE WHEN hp.proprietario_atual THEN 'ATUAL' ELSE 'ANTERIOR' END AS status_posse,
    hp.observacao
FROM historico_proprietario hp
JOIN cliente c ON c.id_cliente = hp.id_cliente
JOIN pessoa pc ON pc.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.id_veiculo = hp.id_veiculo
JOIN modelo mo ON mo.id_modelo = v.id_modelo
JOIN marca ma ON ma.id_marca = mo.id_marca
WHERE hp.ativo = TRUE
ORDER BY pc.nome, v.placa, hp.data_inicio_posse DESC;

-- =========================================================
-- 7. Ordens de Serviço com status atual
-- =========================================================
WITH ultimo_status AS (
    SELECT DISTINCT ON (h.id_ordem_servico)
        h.id_ordem_servico,
        s.nome_status,
        h.data_status
    FROM historico_status_ordem h
    JOIN status_ordem_servico s ON s.id_status_ordem_servico = h.id_status_ordem_servico
    WHERE h.ativo = TRUE
    ORDER BY h.id_ordem_servico, h.data_status DESC, h.id_historico_status_ordem DESC
)
SELECT
    os.id_ordem_servico,
    os.numero_os,
    p.nome AS cliente,
    v.placa,
    ma.nome_marca,
    mo.nome_modelo,
    os.prioridade,
    us.nome_status AS status_atual,
    os.valor_total,
    os.data_abertura,
    os.data_finalizacao,
    os.ativo
FROM ordem_servico os
JOIN cliente c ON c.id_cliente = os.id_cliente
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.id_veiculo = os.id_veiculo
JOIN modelo mo ON mo.id_modelo = v.id_modelo
JOIN marca ma ON ma.id_marca = mo.id_marca
LEFT JOIN ultimo_status us ON us.id_ordem_servico = os.id_ordem_servico
ORDER BY os.id_ordem_servico;

-- =========================================================
-- 8. Resumo financeiro das OS
-- =========================================================
SELECT
    os.numero_os,
    os.valor_total,
    COALESCE(SUM(pg.valor_pago) FILTER (WHERE pg.ativo = TRUE AND pg.status_pagamento = 'PAGO'), 0) AS valor_pago,
    os.valor_total - COALESCE(SUM(pg.valor_pago) FILTER (WHERE pg.ativo = TRUE AND pg.status_pagamento = 'PAGO'), 0) AS valor_pendente
FROM ordem_servico os
LEFT JOIN pagamento pg ON pg.id_ordem_servico = os.id_ordem_servico
GROUP BY os.id_ordem_servico, os.numero_os, os.valor_total
ORDER BY os.id_ordem_servico;

-- =========================================================
-- 9. Serviços por OS com colaborador responsável
-- =========================================================
SELECT
    os.numero_os,
    s.nome_servico,
    pc.nome AS colaborador_responsavel,
    its.quantidade,
    its.valor_unitario,
    its.valor_total,
    its.data_inicio,
    its.data_fim
FROM item_servico its
JOIN ordem_servico os ON os.id_ordem_servico = its.id_ordem_servico
JOIN servico s ON s.id_servico = its.id_servico
JOIN colaborador col ON col.id_colaborador = its.id_colaborador
JOIN pessoa pc ON pc.id_pessoa = col.id_pessoa
WHERE its.ativo = TRUE
ORDER BY os.id_ordem_servico, its.id_item_servico;

-- =========================================================
-- 10. Peças por OS com fornecedor identificado
-- =========================================================
SELECT
    os.numero_os,
    pe.nome_peca,
    pe.codigo_nacional,
    f.nome_fornecedor,
    ip.quantidade,
    ip.valor_unitario,
    ip.valor_total
FROM item_peca ip
JOIN ordem_servico os ON os.id_ordem_servico = ip.id_ordem_servico
JOIN peca pe ON pe.id_peca = ip.id_peca
JOIN fornecedor f ON f.id_fornecedor = ip.id_fornecedor
WHERE ip.ativo = TRUE
ORDER BY os.id_ordem_servico, ip.id_item_peca;

-- =========================================================
-- 11. Garantias de peças e serviços
-- =========================================================
SELECT
    'PECA' AS tipo_garantia,
    os.numero_os,
    pe.nome_peca AS item,
    gp.responsabilidade,
    gp.status_garantia,
    gp.data_inicio,
    gp.data_fim,
    gp.data_acionamento,
    gp.motivo_acionamento,
    gp.responsavel_analise,
    gp.data_encerramento,
    gp.solucao_aplicada
FROM garantia_peca gp
JOIN item_peca ip ON ip.id_item_peca = gp.id_item_peca
JOIN peca pe ON pe.id_peca = ip.id_peca
JOIN ordem_servico os ON os.id_ordem_servico = ip.id_ordem_servico
UNION ALL
SELECT
    'SERVICO' AS tipo_garantia,
    os.numero_os,
    s.nome_servico AS item,
    'OFICINA' AS responsabilidade,
    gs.status_garantia,
    gs.data_inicio,
    gs.data_fim,
    gs.data_acionamento,
    gs.motivo_acionamento,
    gs.responsavel_analise,
    gs.data_encerramento,
    gs.solucao_aplicada
FROM garantia_servico gs
JOIN item_servico its ON its.id_item_servico = gs.id_item_servico
JOIN servico s ON s.id_servico = its.id_servico
JOIN ordem_servico os ON os.id_ordem_servico = its.id_ordem_servico
ORDER BY numero_os, tipo_garantia, item;

-- =========================================================
-- 12. Conferência de dados para emissão de PDF/recibo interno
-- Recomendado: OS finalizada e quitada.
-- =========================================================
WITH ultimo_status AS (
    SELECT DISTINCT ON (h.id_ordem_servico)
        h.id_ordem_servico,
        s.nome_status
    FROM historico_status_ordem h
    JOIN status_ordem_servico s ON s.id_status_ordem_servico = h.id_status_ordem_servico
    WHERE h.ativo = TRUE
    ORDER BY h.id_ordem_servico, h.data_status DESC, h.id_historico_status_ordem DESC
), resumo_pagamento AS (
    SELECT
        os.id_ordem_servico,
        COALESCE(SUM(pg.valor_pago) FILTER (WHERE pg.ativo = TRUE AND pg.status_pagamento = 'PAGO'), 0) AS pago
    FROM ordem_servico os
    LEFT JOIN pagamento pg ON pg.id_ordem_servico = os.id_ordem_servico
    GROUP BY os.id_ordem_servico
)
SELECT
    os.numero_os,
    us.nome_status AS status_atual,
    os.valor_total,
    rp.pago,
    CASE
        WHEN us.nome_status = 'FINALIZADO' AND rp.pago >= os.valor_total THEN 'APTA_PARA_PDF_FINAL'
        ELSE 'PENDENTE_REGRA_PDF_FINAL'
    END AS situacao_pdf
FROM ordem_servico os
LEFT JOIN ultimo_status us ON us.id_ordem_servico = os.id_ordem_servico
LEFT JOIN resumo_pagamento rp ON rp.id_ordem_servico = os.id_ordem_servico
ORDER BY os.id_ordem_servico;

-- =========================================================
-- 13. Alertas de inconsistência de rastreabilidade
-- Estes SELECTs devem retornar zero linhas em uma base consistente.
-- =========================================================

-- Veículos sem proprietário atual.
SELECT v.id_veiculo, v.placa
FROM veiculo v
WHERE NOT EXISTS (
    SELECT 1 FROM historico_proprietario hp
    WHERE hp.id_veiculo = v.id_veiculo
      AND hp.proprietario_atual = TRUE
      AND hp.ativo = TRUE
);

-- OS sem item de serviço.
SELECT os.id_ordem_servico, os.numero_os
FROM ordem_servico os
WHERE NOT EXISTS (
    SELECT 1 FROM item_servico its
    WHERE its.id_ordem_servico = os.id_ordem_servico
      AND its.ativo = TRUE
);

-- Item de serviço sem garantia.
SELECT its.id_item_servico, os.numero_os
FROM item_servico its
JOIN ordem_servico os ON os.id_ordem_servico = its.id_ordem_servico
WHERE NOT EXISTS (
    SELECT 1 FROM garantia_servico gs
    WHERE gs.id_item_servico = its.id_item_servico
      AND gs.ativo = TRUE
);

-- Item de peça sem garantia.
SELECT ip.id_item_peca, os.numero_os
FROM item_peca ip
JOIN ordem_servico os ON os.id_ordem_servico = ip.id_ordem_servico
WHERE NOT EXISTS (
    SELECT 1 FROM garantia_peca gp
    WHERE gp.id_item_peca = ip.id_item_peca
      AND gp.ativo = TRUE
);

-- Peça aplicada sem fornecedor identificado. A consulta deve retornar zero linhas.
SELECT ip.id_item_peca, os.numero_os
FROM item_peca ip
JOIN ordem_servico os ON os.id_ordem_servico = ip.id_ordem_servico
WHERE ip.id_fornecedor IS NULL;

-- Verificação Etapa 57: auditoria persistente das notificações do Decorator.
SELECT 'notificacao_auditoria' AS tabela, COUNT(*) AS total_registros FROM notificacao_auditoria;
