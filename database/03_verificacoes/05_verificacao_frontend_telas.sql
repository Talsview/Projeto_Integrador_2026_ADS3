-- Etapa 15 — Verificação rápida para uso das telas Angular
-- Execute no banco car_repair para conferir se existem dados básicos.

SELECT 'funcoes' AS tabela, COUNT(*) AS total FROM funcao
UNION ALL
SELECT 'clientes', COUNT(*) FROM cliente
UNION ALL
SELECT 'colaboradores', COUNT(*) FROM colaborador
UNION ALL
SELECT 'marcas', COUNT(*) FROM marca
UNION ALL
SELECT 'modelos', COUNT(*) FROM modelo
UNION ALL
SELECT 'veiculos', COUNT(*) FROM veiculo
UNION ALL
SELECT 'servicos', COUNT(*) FROM servico
UNION ALL
SELECT 'fornecedores', COUNT(*) FROM fornecedor
UNION ALL
SELECT 'pecas', COUNT(*) FROM peca
UNION ALL
SELECT 'ordens_servico', COUNT(*) FROM ordem_servico;
