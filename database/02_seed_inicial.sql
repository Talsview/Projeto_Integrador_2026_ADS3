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
