-- Verificação da Etapa 22 - Carregamento inicial automático das telas Angular
-- Objetivo: conferir se existem registros para serem exibidos automaticamente
-- quando cada tela é aberta no frontend.

SELECT 'funcao' AS tabela, COUNT(*) AS total_registros FROM funcao
UNION ALL
SELECT 'cliente', COUNT(*) FROM cliente
UNION ALL
SELECT 'colaborador', COUNT(*) FROM colaborador
UNION ALL
SELECT 'marca', COUNT(*) FROM marca
UNION ALL
SELECT 'modelo', COUNT(*) FROM modelo
UNION ALL
SELECT 'veiculo', COUNT(*) FROM veiculo
UNION ALL
SELECT 'servico', COUNT(*) FROM servico
UNION ALL
SELECT 'empresa_terceirizada', COUNT(*) FROM empresa_terceirizada
UNION ALL
SELECT 'fornecedor', COUNT(*) FROM fornecedor
UNION ALL
SELECT 'peca', COUNT(*) FROM peca
UNION ALL
SELECT 'ordem_servico', COUNT(*) FROM ordem_servico
UNION ALL
SELECT 'pagamento', COUNT(*) FROM pagamento;
