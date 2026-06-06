-- Script auxiliar da Etapa 14
-- Objetivo: verificar rapidamente se o banco está respondendo antes de testar o Angular.
-- Este script não altera estrutura e não altera dados.

SELECT
    NOW() AS data_hora_teste,
    CURRENT_DATABASE() AS banco_atual,
    CURRENT_USER AS usuario_atual;

SELECT COUNT(*) AS total_status_ordem_servico
FROM status_ordem_servico;

SELECT COUNT(*) AS total_funcoes
FROM funcao;
