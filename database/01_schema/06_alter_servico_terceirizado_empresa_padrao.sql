-- Etapa 70 — Empresa padrão no cadastro de serviço terceirizado.
--
-- Regra aplicada:
-- - O serviço terceirizado deve possuir uma empresa terceirizada padrão cadastrada.
-- - Ao selecionar esse serviço na Ordem de Serviço, a empresa executora é preenchida automaticamente.
-- - A empresa não deve ser escolhida manualmente no item da OS, pois ela pertence ao cadastro do serviço.

ALTER TABLE servico_terceirizado
    ADD COLUMN IF NOT EXISTS id_empresa_terceirizada_padrao BIGINT;

-- Garante pelo menos uma empresa ativa para preencher bases antigas.
INSERT INTO empresa_terceirizada (nome_empresa, cnpj, telefone, email, endereco)
SELECT 'Parceiro de Funilaria e Pintura', '11.222.333/0001-81', '(62) 3300-1100', 'funilaria.parceira@exemplo.com', 'Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM empresa_terceirizada WHERE cnpj = '11.222.333/0001-81');

INSERT INTO empresa_terceirizada (nome_empresa, cnpj, telefone, email, endereco)
SELECT 'Retífica Técnica Centro Oeste', '45.997.418/0001-53', '(62) 3300-1101', 'retifica.centro@exemplo.com', 'Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM empresa_terceirizada WHERE cnpj = '45.997.418/0001-53');

INSERT INTO empresa_terceirizada (nome_empresa, cnpj, telefone, email, endereco)
SELECT 'Ar Gelado Auto Service', '19.354.200/0001-70', '(62) 3300-1102', 'argelado@exemplo.com', 'Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM empresa_terceirizada WHERE cnpj = '19.354.200/0001-70');

UPDATE servico_terceirizado st
   SET id_empresa_terceirizada_padrao = CASE
       WHEN s.nome_servico ILIKE '%retífica%' THEN (SELECT id_empresa_terceirizada FROM empresa_terceirizada WHERE cnpj = '45.997.418/0001-53')
       WHEN s.nome_servico ILIKE '%ar-condicionado%' THEN (SELECT id_empresa_terceirizada FROM empresa_terceirizada WHERE cnpj = '19.354.200/0001-70')
       ELSE (SELECT id_empresa_terceirizada FROM empresa_terceirizada WHERE cnpj = '11.222.333/0001-81')
   END
  FROM servico s
 WHERE s.id_servico = st.id_servico
   AND st.id_empresa_terceirizada_padrao IS NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_servico_terceirizado_empresa_padrao'
    ) THEN
        ALTER TABLE servico_terceirizado
            ADD CONSTRAINT fk_servico_terceirizado_empresa_padrao
            FOREIGN KEY (id_empresa_terceirizada_padrao) REFERENCES empresa_terceirizada(id_empresa_terceirizada);
    END IF;
END $$;

ALTER TABLE servico_terceirizado
    ALTER COLUMN id_empresa_terceirizada_padrao SET NOT NULL;


-- Garante compatibilidade caso a base ainda não tenha recebido a Etapa 58.
ALTER TABLE peca ADD COLUMN IF NOT EXISTS id_fornecedor_padrao BIGINT;
ALTER TABLE peca ADD COLUMN IF NOT EXISTS valor_unitario_padrao NUMERIC(12,2) NOT NULL DEFAULT 0;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'fk_peca_fornecedor_padrao'
    ) THEN
        ALTER TABLE peca
            ADD CONSTRAINT fk_peca_fornecedor_padrao
            FOREIGN KEY (id_fornecedor_padrao) REFERENCES fornecedor(id_fornecedor);
    END IF;
END $$;

-- Reforço da regra de peças: toda peça cadastrada deve possuir fornecedor padrão.
INSERT INTO fornecedor (nome_fornecedor, cnpj, telefone, email, endereco)
SELECT 'Fornecedor Auto Peças Centro', '11.222.333/0001-81', '(62) 3300-1200', 'autopecas.centro@exemplo.com', 'Avenida Anhanguera, Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM fornecedor WHERE cnpj = '11.222.333/0001-81');

UPDATE peca p
   SET id_fornecedor_padrao = COALESCE(
       p.id_fornecedor_padrao,
       (SELECT f.id_fornecedor FROM fornecedor f WHERE f.ativo = TRUE ORDER BY f.id_fornecedor LIMIT 1)
   )
 WHERE p.id_fornecedor_padrao IS NULL
   AND EXISTS (SELECT 1 FROM fornecedor f WHERE f.ativo = TRUE);

ALTER TABLE peca
    ALTER COLUMN id_fornecedor_padrao SET NOT NULL;

-- Reforço da regra das peças: item de peça deve usar o fornecedor padrão da peça cadastrada.
UPDATE item_peca ip
   SET id_fornecedor = p.id_fornecedor_padrao
  FROM peca p
 WHERE p.id_peca = ip.id_peca
   AND p.id_fornecedor_padrao IS NOT NULL
   AND ip.id_fornecedor IS DISTINCT FROM p.id_fornecedor_padrao;
