-- ==========================================================
-- 03_verificacao_integracao_frontend.sql
-- Sistema de Gestão da Oficina Mecânica AV CAR AUTO CENTER
-- Etapa 13 — Verificação auxiliar para integração Angular + Backend
-- ==========================================================
-- Este script não altera a estrutura do banco de dados.
-- Ele apenas consulta dados mínimos para apoiar o teste do frontend.
-- Deve ser executado depois de:
-- 01_create_schema.sql
-- 02_seed_inicial.sql
-- ==========================================================

-- Verificar status cadastrados para o fluxo da Ordem de Serviço.
SELECT id_status_ordem_servico, nome_status, ordem_fluxo, ativo
FROM status_ordem_servico
ORDER BY ordem_fluxo;

-- Verificar funções iniciais dos colaboradores.
SELECT id_funcao, nome_funcao, ativo
FROM funcao
ORDER BY nome_funcao;

-- Verificar quantidade de clientes cadastrados.
SELECT COUNT(*) AS total_clientes
FROM cliente;

-- Verificar quantidade de veículos cadastrados.
SELECT COUNT(*) AS total_veiculos
FROM veiculo;

-- Verificar quantidade de ordens de serviço cadastradas.
SELECT COUNT(*) AS total_ordens_servico
FROM ordem_servico;
