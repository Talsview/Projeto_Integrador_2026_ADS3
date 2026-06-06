-- =========================================================
-- Etapa 16 — Verificação de dados para testes do frontend
-- Objetivo: auxiliar a validação visual das tabelas após ações no Angular.
-- =========================================================

-- Verificar clientes cadastrados após salvar pelo frontend.
SELECT c.id_cliente, p.nome, p.telefone, p.email, c.ativo
FROM cliente c
INNER JOIN pessoa p ON p.id_pessoa = c.id_pessoa
ORDER BY c.id_cliente DESC;

-- Verificar funções cadastradas após salvar pelo frontend.
SELECT id_funcao, nome_funcao, descricao, ativo
FROM funcao
ORDER BY id_funcao DESC;

-- Verificar veículos cadastrados após salvar pelo frontend.
SELECT v.id_veiculo, v.placa, v.ano_veiculo, v.ano_modelo, v.quilometragem_atual, v.ativo
FROM veiculo v
ORDER BY v.id_veiculo DESC;

-- Verificar ordens de serviço e seus valores após ações no frontend.
SELECT id_ordem_servico, numero_os, valor_total, prioridade, ativo
FROM ordem_servico
ORDER BY id_ordem_servico DESC;
