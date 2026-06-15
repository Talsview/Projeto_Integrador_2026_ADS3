-- Etapa 69 — Ajuste da regra de serviço terceirizado.
--
-- Serviço interno continua exigindo colaborador responsável da oficina.
-- Serviço terceirizado passa a exigir empresa terceirizada executora em
-- execucao_servico_terceirizado, não colaborador interno em item_servico.
-- Por isso, a coluna id_colaborador precisa aceitar NULL para os itens
-- terceirizados.

ALTER TABLE item_servico
    ALTER COLUMN id_colaborador DROP NOT NULL;
