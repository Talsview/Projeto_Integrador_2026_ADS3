-- Etapa 58 - Peça vinculada a fornecedor padrão e valor unitário padrão
-- Execute este script em bancos já existentes antes de subir o backend atualizado.

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

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint WHERE conname = 'ck_peca_valor_padrao'
    ) THEN
        ALTER TABLE peca
            ADD CONSTRAINT ck_peca_valor_padrao
            CHECK (valor_unitario_padrao >= 0);
    END IF;
END $$;

-- Preenche peças antigas com um fornecedor padrão quando existir fornecedor ativo cadastrado.
UPDATE peca p
   SET id_fornecedor_padrao = COALESCE(
       p.id_fornecedor_padrao,
       (SELECT f.id_fornecedor FROM fornecedor f WHERE f.ativo = TRUE ORDER BY f.id_fornecedor LIMIT 1)
   )
 WHERE p.ativo = TRUE
   AND p.id_fornecedor_padrao IS NULL
   AND EXISTS (SELECT 1 FROM fornecedor f WHERE f.ativo = TRUE);

-- Valores padrão para peças iniciais já existentes antes desta etapa.
UPDATE peca SET valor_unitario_padrao = 40.00 WHERE codigo_nacional = 'FOL0113' AND valor_unitario_padrao = 0;
UPDATE peca SET valor_unitario_padrao = 58.00 WHERE codigo_nacional = 'ART8826' AND valor_unitario_padrao = 0;
UPDATE peca SET valor_unitario_padrao = 55.00 WHERE codigo_nacional = 'OLEO5W30' AND valor_unitario_padrao = 0;
UPDATE peca SET valor_unitario_padrao = 40.00 WHERE codigo_nacional = 'FCA0125' AND valor_unitario_padrao = 0;
UPDATE peca SET valor_unitario_padrao = 168.00 WHERE codigo_nacional = 'KITDISTRIBUICAO' AND valor_unitario_padrao = 0;
UPDATE peca SET valor_unitario_padrao = 63.00 WHERE codigo_nacional = 'CORREIAMICROV' AND valor_unitario_padrao = 0;
UPDATE peca SET valor_unitario_padrao = 288.00 WHERE codigo_nacional = 'NKF8116' AND valor_unitario_padrao = 0;
UPDATE peca SET valor_unitario_padrao = 1265.00 WHERE codigo_nacional = 'RADIADOR522201' AND valor_unitario_padrao = 0;
UPDATE peca SET valor_unitario_padrao = 40.00 WHERE codigo_nacional = 'ADITIVOORGROSA1L' AND valor_unitario_padrao = 0;
UPDATE peca SET valor_unitario_padrao = 20.00 WHERE codigo_nacional = 'VELANGK99632' AND valor_unitario_padrao = 0;
