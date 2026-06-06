-- Verificação da etapa 24 — atualização global das telas Angular.
-- Execute após realizar cadastros pelo frontend para conferir se os dados foram gravados.

SELECT 'clientes' AS tabela, COUNT(*) AS total FROM cliente
UNION ALL
SELECT 'colaboradores', COUNT(*) FROM colaborador
UNION ALL
SELECT 'funcoes', COUNT(*) FROM funcao
UNION ALL
SELECT 'veiculos', COUNT(*) FROM veiculo
UNION ALL
SELECT 'servicos', COUNT(*) FROM servico
UNION ALL
SELECT 'fornecedores', COUNT(*) FROM fornecedor
UNION ALL
SELECT 'pecas', COUNT(*) FROM peca
UNION ALL
SELECT 'ordens_servico', COUNT(*) FROM ordem_servico
UNION ALL
SELECT 'pagamentos', COUNT(*) FROM pagamento;
