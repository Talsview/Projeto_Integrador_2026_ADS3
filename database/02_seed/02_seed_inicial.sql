-- Dados iniciais necessários ao funcionamento acadêmico do sistema.

INSERT INTO status_ordem_servico (nome_status, ordem_fluxo, descricao)
VALUES
    ('ORCAMENTO', 1, 'Ordem de serviço criada em fase de orçamento.'),
    ('EXECUCAO', 2, 'Ordem de serviço aprovada e em execução.'),
    ('PAGAMENTO', 3, 'Ordem de serviço concluída e aguardando pagamento.'),
    ('FINALIZADO', 4, 'Ordem de serviço finalizada; inicia a garantia de peças e serviços.')
ON CONFLICT (nome_status) DO NOTHING;

INSERT INTO funcao (nome_funcao, descricao)
VALUES
    ('Mecânico', 'Colaborador responsável por executar serviços técnicos nos veículos.'),
    ('Atendente', 'Colaborador responsável pelo atendimento ao cliente e abertura de OS.'),
    ('Secretária', 'Colaborador responsável por rotinas administrativas.'),
    ('Faxineiro', 'Colaborador responsável pela limpeza e organização do ambiente.'),
    ('Estoquista', 'Colaborador responsável pelo controle de peças e materiais.'),
    ('Gerente', 'Colaborador responsável pela gestão operacional da oficina.')
ON CONFLICT (nome_funcao) DO NOTHING;

-- Marcas e modelos iniciais úteis para testes do módulo Veículo.
INSERT INTO marca (nome_marca)
VALUES
    ('GM'),
    ('RENAULT'),
    ('FIAT'),
    ('VOLKSWAGEN'),
    ('TOYOTA'),
    ('HYUNDAI'),
    ('FORD')
ON CONFLICT (nome_marca) DO NOTHING;

INSERT INTO modelo (id_marca, nome_modelo)
SELECT id_marca, 'Cobalt' FROM marca WHERE nome_marca = 'GM'
ON CONFLICT (id_marca, nome_modelo) DO NOTHING;

INSERT INTO modelo (id_marca, nome_modelo)
SELECT id_marca, 'Captiva' FROM marca WHERE nome_marca = 'GM'
ON CONFLICT (id_marca, nome_modelo) DO NOTHING;

-- Serviços iniciais para testes e demonstração do módulo Serviço.
INSERT INTO servico (nome_servico, descricao, prazo_garantia_dias, valor_base)
VALUES
    ('Serviços mecânicos', 'Serviço técnico geral de manutenção mecânica.', 90, 180.00),
    ('Alinhamento', 'Serviço de alinhamento de direção.', 30, 120.00),
    ('Balanceamento', 'Serviço de balanceamento de rodas.', 30, 80.00),
    ('Cambagem', 'Serviço de correção de cambagem.', 30, 120.00),
    ('Serviço de ar-condicionado', 'Diagnóstico, limpeza ou manutenção do sistema de ar-condicionado.', 90, 220.00),
    ('Funilaria terceirizada', 'Serviço de funilaria executado por empresa parceira.', 90, 0.00),
    ('Pintura terceirizada', 'Serviço de pintura executado por empresa parceira.', 90, 0.00)
ON CONFLICT (nome_servico) DO NOTHING;

INSERT INTO servico_interno (id_servico, observacao_interna)
SELECT id_servico, 'Serviço executado diretamente pela equipe da oficina.'
  FROM servico
 WHERE nome_servico IN ('Serviços mecânicos', 'Alinhamento', 'Balanceamento', 'Cambagem', 'Serviço de ar-condicionado')
ON CONFLICT (id_servico) DO NOTHING;

INSERT INTO servico_terceirizado (id_servico, observacao_terceirizacao)
SELECT id_servico, 'Serviço encaminhado a empresa parceira; a oficina mantém responsabilidade perante o cliente.'
  FROM servico
 WHERE nome_servico IN ('Funilaria terceirizada', 'Pintura terceirizada')
ON CONFLICT (id_servico) DO NOTHING;

INSERT INTO empresa_terceirizada (nome_empresa, telefone, email, endereco)
VALUES
    ('Parceiro de Funilaria e Pintura', '(62) 0000-0000', 'parceiro.funilaria@email.com', 'Goiânia-GO'),
    ('Parceiro de Ar-condicionado Automotivo', '(62) 0000-0001', 'parceiro.ar@email.com', 'Goiânia-GO')
ON CONFLICT DO NOTHING;

-- Fornecedores e peças iniciais para testes do módulo Peça.
INSERT INTO fornecedor (nome_fornecedor, cnpj, telefone, email, endereco)
VALUES
    ('Fornecedor Auto Peças Centro', NULL, '(62) 0000-1000', 'autopecas.centro@email.com', 'Goiânia-GO'),
    ('Distribuidora de Peças Goiânia', NULL, '(62) 0000-1001', 'distribuidora.pecas@email.com', 'Goiânia-GO'),
    ('Representante de Lubrificantes', NULL, '(62) 0000-1002', 'lubrificantes@email.com', 'Goiânia-GO')
ON CONFLICT DO NOTHING;

INSERT INTO peca (nome_peca, codigo_nacional, marca_peca, modelo_aplicavel, ano_veiculo, ano_modelo, prazo_garantia_dias, descricao)
VALUES
    ('Filtro de óleo', 'FOL0113', 'AMX', 'Aplicação conforme catálogo', NULL, NULL, 90, 'Peça de manutenção preventiva.'),
    ('Filtro de ar do motor', 'ART8826', 'AMX', 'Aplicação conforme catálogo', NULL, NULL, 90, 'Peça de manutenção preventiva.'),
    ('Óleo do motor 5W30', 'OLEO5W30', 'Selenia', 'Aplicação conforme especificação do veículo', NULL, NULL, 90, 'Lubrificante automotivo.'),
    ('Kit distribuição', 'KITDISTRIBUICAO', 'AMX', 'Aplicação conforme motorização', NULL, NULL, 180, 'Kit de manutenção do sistema de distribuição.'),
    ('Correia micro V', 'CORREIAMICROV', 'Gates', 'Aplicação conforme catálogo', NULL, NULL, 90, 'Correia auxiliar do motor.')
ON CONFLICT DO NOTHING;
