-- Verificação da Etapa 23
-- Objetivo: conferir registros usados pelas telas que devem carregar e atualizar automaticamente.

SELECT 'cliente' AS tabela, COUNT(*) AS total_registros FROM cliente
UNION ALL
SELECT 'pessoa', COUNT(*) FROM pessoa
UNION ALL
SELECT 'pessoa_fisica', COUNT(*) FROM pessoa_fisica
UNION ALL
SELECT 'pessoa_juridica', COUNT(*) FROM pessoa_juridica
UNION ALL
SELECT 'funcao', COUNT(*) FROM funcao;

-- Conferência de clientes com dados de pessoa.
SELECT
    c.id_cliente,
    p.nome,
    p.telefone,
    p.email,
    CASE
        WHEN pf.id_cliente IS NOT NULL THEN 'PESSOA_FISICA'
        WHEN pj.id_cliente IS NOT NULL THEN 'PESSOA_JURIDICA'
        ELSE 'NAO_CLASSIFICADO'
    END AS tipo_cliente
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
LEFT JOIN pessoa_fisica pf ON pf.id_cliente = c.id_cliente
LEFT JOIN pessoa_juridica pj ON pj.id_cliente = c.id_cliente
ORDER BY c.id_cliente DESC;

-- Conferência de funções usadas no cadastro de colaboradores.
SELECT id_funcao, nome_funcao, descricao, ativo
FROM funcao
ORDER BY id_funcao;
