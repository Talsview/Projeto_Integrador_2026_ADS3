-- Verificação da Etapa 25: comunicação global do frontend Angular com o backend.
-- Execute estas consultas no pgAdmin para confirmar se existem dados a serem exibidos nas telas.

SELECT 'funcoes' AS tabela, COUNT(*) AS total FROM funcao WHERE ativo = true
UNION ALL
SELECT 'marcas', COUNT(*) FROM marca WHERE ativo = true
UNION ALL
SELECT 'modelos', COUNT(*) FROM modelo WHERE ativo = true
UNION ALL
SELECT 'clientes', COUNT(*) FROM cliente WHERE ativo = true
UNION ALL
SELECT 'colaboradores', COUNT(*) FROM colaborador WHERE ativo = true
UNION ALL
SELECT 'veiculos', COUNT(*) FROM veiculo WHERE ativo = true
UNION ALL
SELECT 'servicos', COUNT(*) FROM servico WHERE ativo = true
UNION ALL
SELECT 'fornecedores', COUNT(*) FROM fornecedor WHERE ativo = true
UNION ALL
SELECT 'pecas', COUNT(*) FROM peca WHERE ativo = true
UNION ALL
SELECT 'ordens_servico', COUNT(*) FROM ordem_servico WHERE ativo = true;
