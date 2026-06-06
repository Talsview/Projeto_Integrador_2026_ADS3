-- Verificação auxiliar da Etapa 17.
-- Objetivo: confirmar que os registros salvos pelo frontend estão chegando ao banco.
-- A atualização visual é responsabilidade do Angular, mas esta consulta permite validar a persistência.

SELECT
    c.id_cliente,
    p.nome,
    p.telefone,
    p.email,
    c.ativo,
    c.data_hora_criacao
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
ORDER BY c.id_cliente DESC;
