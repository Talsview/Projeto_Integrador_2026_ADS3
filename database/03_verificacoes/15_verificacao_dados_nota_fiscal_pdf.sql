-- Verificação dos dados usados pela geração da Nota Fiscal / Recibo em PDF.
-- Execute substituindo o valor do ID da OS conforme necessário.

-- 1. Dados principais da OS
SELECT
    os.id_ordem_servico,
    os.numero_os,
    os.data_abertura,
    os.data_finalizacao,
    os.valor_total,
    p.nome AS cliente,
    v.placa,
    m.nome_modelo,
    ma.nome_marca
FROM ordem_servico os
JOIN cliente c ON c.id_cliente = os.id_cliente
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.id_veiculo = os.id_veiculo
JOIN modelo m ON m.id_modelo = v.id_modelo
JOIN marca ma ON ma.id_marca = m.id_marca
WHERE os.ativo = true
ORDER BY os.id_ordem_servico DESC;

-- 2. Serviços por OS
SELECT
    os.numero_os,
    s.nome_servico,
    pc.nome AS colaborador_responsavel,
    its.quantidade,
    its.valor_unitario,
    its.valor_total
FROM item_servico its
JOIN ordem_servico os ON os.id_ordem_servico = its.id_ordem_servico
JOIN servico s ON s.id_servico = its.id_servico
JOIN colaborador col ON col.id_colaborador = its.id_colaborador
JOIN pessoa pc ON pc.id_pessoa = col.id_pessoa
WHERE its.ativo = true
ORDER BY os.id_ordem_servico DESC, its.id_item_servico;

-- 3. Peças por OS com fornecedor identificado
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
WHERE ip.ativo = true
ORDER BY os.id_ordem_servico DESC, ip.id_item_peca;

-- 4. Pagamentos por OS
SELECT
    os.numero_os,
    pg.forma_pagamento,
    pg.status_pagamento,
    pg.valor_pago,
    pg.data_pagamento
FROM pagamento pg
JOIN ordem_servico os ON os.id_ordem_servico = pg.id_ordem_servico
WHERE pg.ativo = true
ORDER BY os.id_ordem_servico DESC, pg.id_pagamento;
