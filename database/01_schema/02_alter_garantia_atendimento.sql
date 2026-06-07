-- Etapa 45 - Atendimento de garantia
-- Script incremental para bancos já criados antes da inclusão dos campos de acionamento e encerramento.

ALTER TABLE garantia_peca ADD COLUMN IF NOT EXISTS data_acionamento DATE;
ALTER TABLE garantia_peca ADD COLUMN IF NOT EXISTS motivo_acionamento VARCHAR(255);
ALTER TABLE garantia_peca ADD COLUMN IF NOT EXISTS descricao_defeito TEXT;
ALTER TABLE garantia_peca ADD COLUMN IF NOT EXISTS responsavel_analise VARCHAR(150);
ALTER TABLE garantia_peca ADD COLUMN IF NOT EXISTS data_encerramento DATE;
ALTER TABLE garantia_peca ADD COLUMN IF NOT EXISTS solucao_aplicada TEXT;
ALTER TABLE garantia_peca ADD COLUMN IF NOT EXISTS custo_assumido_por VARCHAR(80);
ALTER TABLE garantia_peca ADD COLUMN IF NOT EXISTS atendimento_realizado BOOLEAN;

ALTER TABLE garantia_servico ADD COLUMN IF NOT EXISTS data_acionamento DATE;
ALTER TABLE garantia_servico ADD COLUMN IF NOT EXISTS motivo_acionamento VARCHAR(255);
ALTER TABLE garantia_servico ADD COLUMN IF NOT EXISTS descricao_defeito TEXT;
ALTER TABLE garantia_servico ADD COLUMN IF NOT EXISTS responsavel_analise VARCHAR(150);
ALTER TABLE garantia_servico ADD COLUMN IF NOT EXISTS data_encerramento DATE;
ALTER TABLE garantia_servico ADD COLUMN IF NOT EXISTS solucao_aplicada TEXT;
ALTER TABLE garantia_servico ADD COLUMN IF NOT EXISTS custo_assumido_por VARCHAR(80);
ALTER TABLE garantia_servico ADD COLUMN IF NOT EXISTS atendimento_realizado BOOLEAN;
