-- =========================================================
-- Sistema de Gestão da Oficina Mecânica AV CAR AUTO CENTER
-- Seed inicial completo - PostgreSQL
-- =========================================================
-- Objetivo:
-- Popular o banco com dados coerentes para demonstração acadêmica,
-- testes do frontend Angular, validações de regras de negócio,
-- emissão de PDF/recibo interno e conferência da rastreabilidade.
--
-- Observações:
-- 1. O script é idempotente sempre que possível.
-- 2. CPFs e CNPJs utilizados são válidos pelo cálculo dos dígitos verificadores.
-- 3. A numeração das OS é preservada e não deve ser reutilizada.
-- 4. O Hibernate deve permanecer com spring.jpa.hibernate.ddl-auto=validate.
-- =========================================================

BEGIN;

-- =========================================================
-- 1. Status do fluxo da Ordem de Serviço
-- =========================================================
INSERT INTO status_ordem_servico (nome_status, ordem_fluxo, descricao)
VALUES
    ('ORCAMENTO', 1, 'Ordem de serviço criada em fase de orçamento.'),
    ('EXECUCAO', 2, 'Ordem de serviço aprovada e em execução.'),
    ('PAGAMENTO', 3, 'Ordem de serviço concluída e aguardando pagamento.'),
    ('FINALIZADO', 4, 'Ordem de serviço finalizada; inicia a garantia de peças e serviços.')
ON CONFLICT (nome_status) DO NOTHING;

-- =========================================================
-- 2. Funções de colaboradores
-- =========================================================
INSERT INTO funcao (nome_funcao, descricao)
VALUES
    ('Mecânico', 'Colaborador responsável por executar serviços técnicos nos veículos.'),
    ('Atendente', 'Colaborador responsável pelo atendimento ao cliente e abertura de OS.'),
    ('Secretária', 'Colaborador responsável por rotinas administrativas.'),
    ('Faxineiro', 'Colaborador responsável pela limpeza e organização do ambiente.'),
    ('Estoquista', 'Colaborador responsável pelo controle de peças e materiais.'),
    ('Gerente', 'Colaborador responsável pela gestão operacional da oficina.'),
    ('Eletricista Automotivo', 'Colaborador responsável por diagnóstico e manutenção elétrica automotiva.'),
    ('Alinhador', 'Colaborador responsável por alinhamento, balanceamento e geometria.'),
    ('Consultor Técnico', 'Colaborador responsável por análise técnica, orçamento e acompanhamento da OS.')
ON CONFLICT (nome_funcao) DO NOTHING;

-- =========================================================
-- 3. Marcas e modelos de veículos
-- =========================================================
INSERT INTO marca (nome_marca)
VALUES
    ('GM'),
    ('RENAULT'),
    ('FIAT'),
    ('VOLKSWAGEN'),
    ('TOYOTA'),
    ('HYUNDAI'),
    ('FORD'),
    ('HONDA'),
    ('NISSAN')
ON CONFLICT (nome_marca) DO NOTHING;

INSERT INTO modelo (id_marca, nome_modelo)
SELECT m.id_marca, x.nome_modelo
FROM marca m
JOIN (VALUES
    ('GM', 'Cobalt'),
    ('GM', 'Captiva'),
    ('GM', 'Onix'),
    ('RENAULT', 'Duster'),
    ('RENAULT', 'Sandero'),
    ('FIAT', 'Strada'),
    ('FIAT', 'Argo'),
    ('VOLKSWAGEN', 'Polo'),
    ('VOLKSWAGEN', 'Gol'),
    ('TOYOTA', 'Corolla'),
    ('TOYOTA', 'Etios'),
    ('HYUNDAI', 'HB20'),
    ('FORD', 'Ka'),
    ('HONDA', 'Civic'),
    ('NISSAN', 'Versa')
) AS x(nome_marca, nome_modelo) ON x.nome_marca = m.nome_marca
ON CONFLICT (id_marca, nome_modelo) DO NOTHING;

-- =========================================================
-- 4. Serviços internos e terceirizados
-- =========================================================
INSERT INTO servico (nome_servico, descricao, prazo_garantia_dias, valor_base)
VALUES
    ('Serviços mecânicos', 'Serviço técnico geral de manutenção mecânica.', 90, 180.00),
    ('Revisão preventiva', 'Revisão periódica com inspeção dos principais sistemas do veículo.', 90, 350.00),
    ('Troca de óleo', 'Substituição de óleo do motor e inspeção básica.', 30, 80.00),
    ('Alinhamento', 'Serviço de alinhamento de direção.', 30, 120.00),
    ('Balanceamento', 'Serviço de balanceamento de rodas.', 30, 80.00),
    ('Cambagem', 'Serviço de correção de cambagem.', 30, 120.00),
    ('Serviço elétrico', 'Diagnóstico e manutenção do sistema elétrico automotivo.', 90, 200.00),
    ('Serviço de ar-condicionado', 'Diagnóstico, limpeza ou manutenção do sistema de ar-condicionado.', 90, 220.00),
    ('Higienização de ar-condicionado', 'Limpeza e higienização do sistema de ventilação.', 30, 100.00),
    ('Funilaria terceirizada', 'Serviço de funilaria executado por empresa parceira.', 90, 0.00),
    ('Pintura terceirizada', 'Serviço de pintura executado por empresa parceira.', 90, 0.00),
    ('Retífica terceirizada', 'Serviço de retífica executado por empresa especializada.', 180, 0.00)
ON CONFLICT (nome_servico) DO NOTHING;

INSERT INTO servico_interno (id_servico, observacao_interna)
SELECT id_servico, 'Serviço executado diretamente pela equipe da oficina.'
FROM servico
WHERE nome_servico IN (
    'Serviços mecânicos', 'Revisão preventiva', 'Troca de óleo', 'Alinhamento',
    'Balanceamento', 'Cambagem', 'Serviço elétrico', 'Serviço de ar-condicionado',
    'Higienização de ar-condicionado'
)
ON CONFLICT (id_servico) DO NOTHING;

-- =========================================================
-- 5. Empresas terceirizadas
-- CNPJs válidos para testes acadêmicos.
-- =========================================================
INSERT INTO empresa_terceirizada (nome_empresa, cnpj, telefone, email, endereco)
SELECT 'Parceiro de Funilaria e Pintura', '11.222.333/0001-81', '(62) 3300-1100', 'funilaria.parceira@exemplo.com', 'Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM empresa_terceirizada WHERE cnpj = '11.222.333/0001-81');

INSERT INTO empresa_terceirizada (nome_empresa, cnpj, telefone, email, endereco)
SELECT 'Retífica Técnica Centro Oeste', '45.997.418/0001-53', '(62) 3300-1101', 'retifica.centro@exemplo.com', 'Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM empresa_terceirizada WHERE cnpj = '45.997.418/0001-53');

INSERT INTO empresa_terceirizada (nome_empresa, cnpj, telefone, email, endereco)
SELECT 'Ar Gelado Auto Service', '19.354.200/0001-70', '(62) 3300-1102', 'argelado@exemplo.com', 'Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM empresa_terceirizada WHERE cnpj = '19.354.200/0001-70');

INSERT INTO servico_terceirizado (id_servico, id_empresa_terceirizada_padrao, observacao_terceirizacao)
SELECT s.id_servico,
       CASE
           WHEN s.nome_servico = 'Retífica terceirizada' THEN (SELECT id_empresa_terceirizada FROM empresa_terceirizada WHERE cnpj = '45.997.418/0001-53')
           ELSE (SELECT id_empresa_terceirizada FROM empresa_terceirizada WHERE cnpj = '11.222.333/0001-81')
       END,
       'Serviço encaminhado a empresa parceira; a oficina mantém responsabilidade perante o cliente.'
FROM servico s
WHERE s.nome_servico IN ('Funilaria terceirizada', 'Pintura terceirizada', 'Retífica terceirizada')
ON CONFLICT (id_servico) DO UPDATE
   SET id_empresa_terceirizada_padrao = EXCLUDED.id_empresa_terceirizada_padrao,
       observacao_terceirizacao = EXCLUDED.observacao_terceirizacao,
       ativo = TRUE;

-- =========================================================
-- 6. Fornecedores e peças
-- =========================================================
INSERT INTO fornecedor (nome_fornecedor, cnpj, telefone, email, endereco)
SELECT 'Fornecedor Auto Peças Centro', '11.222.333/0001-81', '(62) 3300-1200', 'autopecas.centro@exemplo.com', 'Avenida Anhanguera, Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM fornecedor WHERE cnpj = '11.222.333/0001-81');

INSERT INTO fornecedor (nome_fornecedor, cnpj, telefone, email, endereco)
SELECT 'Distribuidora de Peças Goiânia', '45.997.418/0001-53', '(62) 3300-1201', 'distribuidora.pecas@exemplo.com', 'Setor Campinas, Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM fornecedor WHERE cnpj = '45.997.418/0001-53');

INSERT INTO fornecedor (nome_fornecedor, cnpj, telefone, email, endereco)
SELECT 'Representante de Lubrificantes', '19.354.200/0001-70', '(62) 3300-1202', 'lubrificantes@exemplo.com', 'Setor Leste Universitário, Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM fornecedor WHERE cnpj = '19.354.200/0001-70');

INSERT INTO peca (nome_peca, codigo_nacional, marca_peca, modelo_aplicavel, ano_veiculo, ano_modelo, id_fornecedor_padrao, valor_unitario_padrao, prazo_garantia_dias, descricao)
VALUES
    ('Filtro de óleo FOL0113', 'FOL0113', 'AMX', 'Aplicação conforme catálogo', NULL, NULL, (SELECT id_fornecedor FROM fornecedor WHERE cnpj = '11.222.333/0001-81'), 40.00, 90, 'Peça de manutenção preventiva.'),
    ('Filtro de ar do motor ART8826', 'ART8826', 'AMX', 'Aplicação conforme catálogo', NULL, NULL, (SELECT id_fornecedor FROM fornecedor WHERE cnpj = '11.222.333/0001-81'), 58.00, 90, 'Peça de manutenção preventiva.'),
    ('Óleo do motor 5W30', 'OLEO5W30', 'Selenia', 'Aplicação conforme especificação do veículo', NULL, NULL, (SELECT id_fornecedor FROM fornecedor WHERE cnpj = '19.354.200/0001-70'), 55.00, 90, 'Lubrificante automotivo.'),
    ('Filtro de cabine FCA0125', 'FCA0125', 'Tecfil', 'Aplicação conforme catálogo', NULL, NULL, (SELECT id_fornecedor FROM fornecedor WHERE cnpj = '11.222.333/0001-81'), 40.00, 90, 'Filtro de cabine para ar-condicionado.'),
    ('Kit distribuição', 'KITDISTRIBUICAO', 'AMX', 'Aplicação conforme motorização', NULL, NULL, (SELECT id_fornecedor FROM fornecedor WHERE cnpj = '45.997.418/0001-53'), 168.00, 180, 'Kit de manutenção do sistema de distribuição.'),
    ('Correia micro V', 'CORREIAMICROV', 'Gates', 'Aplicação conforme catálogo', NULL, NULL, (SELECT id_fornecedor FROM fornecedor WHERE cnpj = '45.997.418/0001-53'), 63.00, 90, 'Correia auxiliar do motor.'),
    ('Cubo de roda dianteiro com rolamento', 'NKF8116', 'Nakata', 'Aplicação conforme catálogo', NULL, NULL, (SELECT id_fornecedor FROM fornecedor WHERE cnpj = '45.997.418/0001-53'), 288.00, 180, 'Cubo de roda dianteiro com rolamento.'),
    ('Radiador', 'RADIADOR522201', 'Valeo', 'Aplicação conforme catálogo', NULL, NULL, (SELECT id_fornecedor FROM fornecedor WHERE cnpj = '45.997.418/0001-53'), 1265.00, 180, 'Radiador do sistema de arrefecimento.'),
    ('Aditivo de radiador orgânico rosa 1L', 'ADITIVOORGROSA1L', 'Petronas', 'Aplicação geral', NULL, NULL, (SELECT id_fornecedor FROM fornecedor WHERE cnpj = '19.354.200/0001-70'), 40.00, 90, 'Aditivo para sistema de arrefecimento.'),
    ('Vela de ignição NGK', 'VELANGK99632', 'NGK', 'Aplicação conforme catálogo', NULL, NULL, (SELECT id_fornecedor FROM fornecedor WHERE cnpj = '11.222.333/0001-81'), 20.00, 90, 'Vela de ignição automotiva.')
ON CONFLICT DO NOTHING;

-- =========================================================
-- 7. Clientes Pessoa Física e Pessoa Jurídica
-- CPFs/CNPJs válidos para testes de validação real.
-- =========================================================
INSERT INTO pessoa (nome, telefone, email, endereco)
SELECT 'Davi Martins Alexandre', '(62) 98619-7730', 'daviconcyline@gmail.com', 'Avenida W 6, S/N'
WHERE NOT EXISTS (SELECT 1 FROM pessoa WHERE nome = 'Davi Martins Alexandre' AND email = 'daviconcyline@gmail.com');

INSERT INTO cliente (id_pessoa)
SELECT id_pessoa FROM pessoa WHERE nome = 'Davi Martins Alexandre' AND email = 'daviconcyline@gmail.com'
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO pessoa_fisica (id_cliente, cpf, rg, data_nascimento)
SELECT c.id_cliente, '071.997.471-28', '74923225', DATE '2000-06-06'
FROM cliente c JOIN pessoa p ON p.id_pessoa = c.id_pessoa
WHERE p.email = 'daviconcyline@gmail.com'
ON CONFLICT (id_cliente) DO NOTHING;

INSERT INTO pessoa (nome, telefone, email, endereco)
SELECT 'Eugenio Julio Messala', '(62) 99241-0579', 'eugeniojuliomessala@gmail.com', 'Rua Eugênio Jardim, Setor Leste Vila Nova, Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM pessoa WHERE nome = 'Eugenio Julio Messala' AND email = 'eugeniojuliomessala@gmail.com');

INSERT INTO cliente (id_pessoa)
SELECT id_pessoa FROM pessoa WHERE nome = 'Eugenio Julio Messala' AND email = 'eugeniojuliomessala@gmail.com'
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO pessoa_fisica (id_cliente, cpf, rg, data_nascimento)
SELECT c.id_cliente, '521.773.431-00', 'MG123456', DATE '1985-04-15'
FROM cliente c JOIN pessoa p ON p.id_pessoa = c.id_pessoa
WHERE p.email = 'eugeniojuliomessala@gmail.com'
ON CONFLICT (id_cliente) DO NOTHING;

INSERT INTO pessoa (nome, telefone, email, endereco)
SELECT 'Maria Souza Santos', '(62) 98888-7777', 'maria.souza@exemplo.com', 'Rua 10, Setor Central, Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM pessoa WHERE nome = 'Maria Souza Santos' AND email = 'maria.souza@exemplo.com');

INSERT INTO cliente (id_pessoa)
SELECT id_pessoa FROM pessoa WHERE nome = 'Maria Souza Santos' AND email = 'maria.souza@exemplo.com'
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO pessoa_fisica (id_cliente, cpf, rg, data_nascimento)
SELECT c.id_cliente, '111.444.777-35', 'GO998877', DATE '1992-02-20'
FROM cliente c JOIN pessoa p ON p.id_pessoa = c.id_pessoa
WHERE p.email = 'maria.souza@exemplo.com'
ON CONFLICT (id_cliente) DO NOTHING;

INSERT INTO pessoa (nome, telefone, email, endereco)
SELECT 'João Silva', '(62) 99999-1111', 'joao.silva@exemplo.com', 'Rua 15, Setor Sul, Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM pessoa WHERE nome = 'João Silva' AND email = 'joao.silva@exemplo.com');

INSERT INTO cliente (id_pessoa)
SELECT id_pessoa FROM pessoa WHERE nome = 'João Silva' AND email = 'joao.silva@exemplo.com'
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO pessoa_fisica (id_cliente, cpf, rg, data_nascimento)
SELECT c.id_cliente, '529.982.247-25', 'GO112233', DATE '1990-01-10'
FROM cliente c JOIN pessoa p ON p.id_pessoa = c.id_pessoa
WHERE p.email = 'joao.silva@exemplo.com'
ON CONFLICT (id_cliente) DO NOTHING;

INSERT INTO pessoa (nome, telefone, email, endereco)
SELECT 'Tecno IT Tecnologia Serviços e Comunicação Ltda', '(62) 98123-5038', 'financeiro@tecnoit.com.br', 'Avenida Olinda, Park Lozandes, Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM pessoa WHERE nome = 'Tecno IT Tecnologia Serviços e Comunicação Ltda' AND email = 'financeiro@tecnoit.com.br');

INSERT INTO cliente (id_pessoa)
SELECT id_pessoa FROM pessoa WHERE nome = 'Tecno IT Tecnologia Serviços e Comunicação Ltda' AND email = 'financeiro@tecnoit.com.br'
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO pessoa_juridica (id_cliente, cnpj, razao_social, nome_fantasia, inscricao_estadual)
SELECT c.id_cliente, '19.354.200/0001-70', 'Tecno IT Tecnologia Serviços e Comunicação Ltda', 'TECNO IT', '105858633'
FROM cliente c JOIN pessoa p ON p.id_pessoa = c.id_pessoa
WHERE p.email = 'financeiro@tecnoit.com.br'
ON CONFLICT (id_cliente) DO NOTHING;

INSERT INTO pessoa (nome, telefone, email, endereco)
SELECT 'Auto Peças Central Ltda', '(62) 4002-8922', 'contato@autopecascentral.com.br', 'Avenida Goiás, Centro, Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM pessoa WHERE nome = 'Auto Peças Central Ltda' AND email = 'contato@autopecascentral.com.br');

INSERT INTO cliente (id_pessoa)
SELECT id_pessoa FROM pessoa WHERE nome = 'Auto Peças Central Ltda' AND email = 'contato@autopecascentral.com.br'
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO pessoa_juridica (id_cliente, cnpj, razao_social, nome_fantasia, inscricao_estadual)
SELECT c.id_cliente, '45.997.418/0001-53', 'Auto Peças Central Ltda', 'Auto Peças Central', 'ISENTO'
FROM cliente c JOIN pessoa p ON p.id_pessoa = c.id_pessoa
WHERE p.email = 'contato@autopecascentral.com.br'
ON CONFLICT (id_cliente) DO NOTHING;

-- =========================================================
-- 8. Colaboradores e vínculos com funções
-- =========================================================
INSERT INTO pessoa (nome, telefone, email, endereco)
SELECT 'Caio Ferreira', '(62) 98619-7731', 'caio@avcar.local', 'Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM pessoa WHERE nome = 'Caio Ferreira' AND email = 'caio@avcar.local');

INSERT INTO colaborador (id_pessoa, data_admissao, status_colaborador)
SELECT id_pessoa, DATE '2025-01-10', 'ATIVO' FROM pessoa WHERE email = 'caio@avcar.local'
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO pessoa (nome, telefone, email, endereco)
SELECT 'Almir Pinto da Silva', '(62) 99911-2233', 'almir@avcar.local', 'Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM pessoa WHERE nome = 'Almir Pinto da Silva' AND email = 'almir@avcar.local');

INSERT INTO colaborador (id_pessoa, data_admissao, status_colaborador)
SELECT id_pessoa, DATE '2024-03-18', 'ATIVO' FROM pessoa WHERE email = 'almir@avcar.local'
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO pessoa (nome, telefone, email, endereco)
SELECT 'Fabio Biziack', '(62) 99922-3344', 'fabio@avcar.local', 'Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM pessoa WHERE nome = 'Fabio Biziack' AND email = 'fabio@avcar.local');

INSERT INTO colaborador (id_pessoa, data_admissao, status_colaborador)
SELECT id_pessoa, DATE '2024-06-01', 'ATIVO' FROM pessoa WHERE email = 'fabio@avcar.local'
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO pessoa (nome, telefone, email, endereco)
SELECT 'Rafael Alves', '(62) 99933-4455', 'rafael@avcar.local', 'Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM pessoa WHERE nome = 'Rafael Alves' AND email = 'rafael@avcar.local');

INSERT INTO colaborador (id_pessoa, data_admissao, status_colaborador)
SELECT id_pessoa, DATE '2024-09-12', 'ATIVO' FROM pessoa WHERE email = 'rafael@avcar.local'
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO pessoa (nome, telefone, email, endereco)
SELECT 'Paula Administrativa', '(62) 99944-5566', 'paula@avcar.local', 'Goiânia-GO'
WHERE NOT EXISTS (SELECT 1 FROM pessoa WHERE nome = 'Paula Administrativa' AND email = 'paula@avcar.local');

INSERT INTO colaborador (id_pessoa, data_admissao, status_colaborador)
SELECT id_pessoa, DATE '2025-02-03', 'ATIVO' FROM pessoa WHERE email = 'paula@avcar.local'
ON CONFLICT (id_pessoa) DO NOTHING;

INSERT INTO colaborador_funcao (id_colaborador, id_funcao, data_inicio)
SELECT col.id_colaborador, f.id_funcao, CURRENT_DATE
FROM colaborador col
JOIN pessoa p ON p.id_pessoa = col.id_pessoa
JOIN funcao f ON f.nome_funcao IN ('Mecânico')
WHERE p.email IN ('caio@avcar.local', 'almir@avcar.local', 'fabio@avcar.local')
  AND NOT EXISTS (
      SELECT 1 FROM colaborador_funcao cf
      WHERE cf.id_colaborador = col.id_colaborador AND cf.id_funcao = f.id_funcao AND cf.ativo = TRUE
  );

INSERT INTO colaborador_funcao (id_colaborador, id_funcao, data_inicio)
SELECT col.id_colaborador, f.id_funcao, CURRENT_DATE
FROM colaborador col
JOIN pessoa p ON p.id_pessoa = col.id_pessoa
JOIN funcao f ON f.nome_funcao = 'Alinhador'
WHERE p.email = 'rafael@avcar.local'
  AND NOT EXISTS (
      SELECT 1 FROM colaborador_funcao cf
      WHERE cf.id_colaborador = col.id_colaborador AND cf.id_funcao = f.id_funcao AND cf.ativo = TRUE
  );

INSERT INTO colaborador_funcao (id_colaborador, id_funcao, data_inicio)
SELECT col.id_colaborador, f.id_funcao, CURRENT_DATE
FROM colaborador col
JOIN pessoa p ON p.id_pessoa = col.id_pessoa
JOIN funcao f ON f.nome_funcao IN ('Atendente', 'Secretária')
WHERE p.email = 'paula@avcar.local'
  AND NOT EXISTS (
      SELECT 1 FROM colaborador_funcao cf
      WHERE cf.id_colaborador = col.id_colaborador AND cf.id_funcao = f.id_funcao AND cf.ativo = TRUE
  );

-- =========================================================
-- 9. Veículos e histórico de proprietários
-- =========================================================
INSERT INTO veiculo (id_modelo, placa, chassi, cor, ano_veiculo, ano_modelo, quilometragem_atual, observacao)
SELECT mo.id_modelo, 'ACC1234', '9BGKS48R0FG123456', 'PRATA', 2024, 2025, 10000, 'Veículo usado para demonstração do cadastro de cliente pessoa física.'
FROM modelo mo JOIN marca ma ON ma.id_marca = mo.id_marca
WHERE ma.nome_marca = 'GM' AND mo.nome_modelo = 'Cobalt'
  AND NOT EXISTS (SELECT 1 FROM veiculo WHERE placa = 'ACC1234');

INSERT INTO veiculo (id_modelo, placa, chassi, cor, ano_veiculo, ano_modelo, quilometragem_atual, observacao)
SELECT mo.id_modelo, 'ONZ1170', '1GNLV13F8DS123456', 'BRANCO', 2013, 2013, 193714, 'Veículo com histórico de OS e peças aplicadas.'
FROM modelo mo JOIN marca ma ON ma.id_marca = mo.id_marca
WHERE ma.nome_marca = 'GM' AND mo.nome_modelo = 'Captiva'
  AND NOT EXISTS (SELECT 1 FROM veiculo WHERE placa = 'ONZ1170');

INSERT INTO veiculo (id_modelo, placa, chassi, cor, ano_veiculo, ano_modelo, quilometragem_atual, observacao)
SELECT mo.id_modelo, 'TNU3J90', '9BD281B22NYY69189', 'BRANCA', 2022, 2022, 39643, 'Veículo vinculado a cliente pessoa jurídica.'
FROM modelo mo JOIN marca ma ON ma.id_marca = mo.id_marca
WHERE ma.nome_marca = 'FIAT' AND mo.nome_modelo = 'Strada'
  AND NOT EXISTS (SELECT 1 FROM veiculo WHERE placa = 'TNU3J90');

INSERT INTO veiculo (id_modelo, placa, chassi, cor, ano_veiculo, ano_modelo, quilometragem_atual, observacao)
SELECT mo.id_modelo, 'PQX1354', '9BGJC69X0GB123456', 'PRATA', 2016, 2016, 100525, 'Veículo finalizado com pagamento quitado para teste de nota/recibo interno.'
FROM modelo mo JOIN marca ma ON ma.id_marca = mo.id_marca
WHERE ma.nome_marca = 'GM' AND mo.nome_modelo = 'Cobalt'
  AND NOT EXISTS (SELECT 1 FROM veiculo WHERE placa = 'PQX1354');

INSERT INTO veiculo (id_modelo, placa, chassi, cor, ano_veiculo, ano_modelo, quilometragem_atual, observacao)
SELECT mo.id_modelo, 'BRA2E22', '9BRBD3HE0K0123456', 'CINZA', 2021, 2021, 55000, 'Veículo com serviço simples finalizado.'
FROM modelo mo JOIN marca ma ON ma.id_marca = mo.id_marca
WHERE ma.nome_marca = 'TOYOTA' AND mo.nome_modelo = 'Corolla'
  AND NOT EXISTS (SELECT 1 FROM veiculo WHERE placa = 'BRA2E22');

-- Histórico de proprietários atualizado para a aba Gestão > Histórico de Proprietários.
-- A tela atual agrupa os registros por cliente; por isso o seed cria posses atuais
-- e posses encerradas, permitindo demonstrar que o cliente antigo permanece visível
-- mesmo depois de perder a posse atual de um veículo.

-- ACC1234: veículo atualmente do Davi, com Maria como proprietária anterior.
INSERT INTO historico_proprietario (id_cliente, id_veiculo, data_inicio_posse, data_fim_posse, proprietario_atual, observacao)
SELECT c.id_cliente, v.id_veiculo, DATE '2024-03-01', DATE '2025-12-31', FALSE,
       'Posse anterior preservada pelo seed para demonstrar histórico de proprietários por cliente.'
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.placa = 'ACC1234'
WHERE p.email = 'maria.souza@exemplo.com'
  AND NOT EXISTS (
      SELECT 1 FROM historico_proprietario hp
      WHERE hp.id_veiculo = v.id_veiculo
        AND hp.id_cliente = c.id_cliente
        AND hp.data_inicio_posse = DATE '2024-03-01'
  );

INSERT INTO historico_proprietario (id_cliente, id_veiculo, data_inicio_posse, proprietario_atual, observacao)
SELECT c.id_cliente, v.id_veiculo, DATE '2026-01-01', TRUE,
       'Proprietário atual cadastrado pelo seed atualizado.'
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.placa = 'ACC1234'
WHERE p.email = 'daviconcyline@gmail.com'
  AND NOT EXISTS (SELECT 1 FROM historico_proprietario hp WHERE hp.id_veiculo = v.id_veiculo AND hp.proprietario_atual = TRUE AND hp.ativo = TRUE);

-- ONZ1170: veículo atualmente do Eugenio, com Davi como proprietário anterior.
-- Isso faz o Davi aparecer uma única vez no histórico, contendo um veículo atual e uma posse antiga.
INSERT INTO historico_proprietario (id_cliente, id_veiculo, data_inicio_posse, data_fim_posse, proprietario_atual, observacao)
SELECT c.id_cliente, v.id_veiculo, DATE '2022-03-10', DATE '2023-01-10', FALSE,
       'Posse encerrada antes da entrada do veículo no histórico atual da oficina.'
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.placa = 'ONZ1170'
WHERE p.email = 'daviconcyline@gmail.com'
  AND NOT EXISTS (
      SELECT 1 FROM historico_proprietario hp
      WHERE hp.id_veiculo = v.id_veiculo
        AND hp.id_cliente = c.id_cliente
        AND hp.data_inicio_posse = DATE '2022-03-10'
  );

INSERT INTO historico_proprietario (id_cliente, id_veiculo, data_inicio_posse, proprietario_atual, observacao)
SELECT c.id_cliente, v.id_veiculo, DATE '2023-01-10', TRUE,
       'Proprietário atual cadastrado pelo seed atualizado.'
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.placa = 'ONZ1170'
WHERE p.email = 'eugeniojuliomessala@gmail.com'
  AND NOT EXISTS (SELECT 1 FROM historico_proprietario hp WHERE hp.id_veiculo = v.id_veiculo AND hp.proprietario_atual = TRUE AND hp.ativo = TRUE);

-- TNU3J90: veículo empresarial atualmente da Tecno IT, com posse anterior da Auto Peças Central.
INSERT INTO historico_proprietario (id_cliente, id_veiculo, data_inicio_posse, data_fim_posse, proprietario_atual, observacao)
SELECT c.id_cliente, v.id_veiculo, DATE '2023-06-01', DATE '2025-10-31', FALSE,
       'Posse anterior de pessoa jurídica preservada para demonstrar rastreabilidade empresarial.'
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.placa = 'TNU3J90'
WHERE p.email = 'contato@autopecascentral.com.br'
  AND NOT EXISTS (
      SELECT 1 FROM historico_proprietario hp
      WHERE hp.id_veiculo = v.id_veiculo
        AND hp.id_cliente = c.id_cliente
        AND hp.data_inicio_posse = DATE '2023-06-01'
  );

INSERT INTO historico_proprietario (id_cliente, id_veiculo, data_inicio_posse, proprietario_atual, observacao)
SELECT c.id_cliente, v.id_veiculo, DATE '2025-11-01', TRUE,
       'Veículo empresarial cadastrado pelo seed atualizado.'
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.placa = 'TNU3J90'
WHERE p.email = 'financeiro@tecnoit.com.br'
  AND NOT EXISTS (SELECT 1 FROM historico_proprietario hp WHERE hp.id_veiculo = v.id_veiculo AND hp.proprietario_atual = TRUE AND hp.ativo = TRUE);

-- PQX1354: segundo veículo atual do Eugenio. Ele continua aparecendo uma única vez na lista,
-- e o detalhe mostra ONZ1170 e PQX1354 agrupados no mesmo cliente.
INSERT INTO historico_proprietario (id_cliente, id_veiculo, data_inicio_posse, data_fim_posse, proprietario_atual, observacao)
SELECT c.id_cliente, v.id_veiculo, DATE '2022-05-15', DATE '2024-11-30', FALSE,
       'Posse anterior encerrada, mantida para demonstrar data de fim da posse.'
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.placa = 'PQX1354'
WHERE p.email = 'joao.silva@exemplo.com'
  AND NOT EXISTS (
      SELECT 1 FROM historico_proprietario hp
      WHERE hp.id_veiculo = v.id_veiculo
        AND hp.id_cliente = c.id_cliente
        AND hp.data_inicio_posse = DATE '2022-05-15'
  );

INSERT INTO historico_proprietario (id_cliente, id_veiculo, data_inicio_posse, proprietario_atual, observacao)
SELECT c.id_cliente, v.id_veiculo, DATE '2024-12-01', TRUE,
       'Proprietário atual cadastrado pelo seed atualizado.'
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.placa = 'PQX1354'
WHERE p.email = 'eugeniojuliomessala@gmail.com'
  AND NOT EXISTS (SELECT 1 FROM historico_proprietario hp WHERE hp.id_veiculo = v.id_veiculo AND hp.proprietario_atual = TRUE AND hp.ativo = TRUE);

-- BRA2E22: veículo atualmente da Maria, com João como proprietário anterior.
INSERT INTO historico_proprietario (id_cliente, id_veiculo, data_inicio_posse, data_fim_posse, proprietario_atual, observacao)
SELECT c.id_cliente, v.id_veiculo, DATE '2023-02-01', DATE '2025-04-30', FALSE,
       'Posse anterior de cliente pessoa física preservada no histórico.'
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.placa = 'BRA2E22'
WHERE p.email = 'joao.silva@exemplo.com'
  AND NOT EXISTS (
      SELECT 1 FROM historico_proprietario hp
      WHERE hp.id_veiculo = v.id_veiculo
        AND hp.id_cliente = c.id_cliente
        AND hp.data_inicio_posse = DATE '2023-02-01'
  );

INSERT INTO historico_proprietario (id_cliente, id_veiculo, data_inicio_posse, proprietario_atual, observacao)
SELECT c.id_cliente, v.id_veiculo, DATE '2025-05-01', TRUE,
       'Proprietário atual cadastrado pelo seed atualizado.'
FROM cliente c
JOIN pessoa p ON p.id_pessoa = c.id_pessoa
JOIN veiculo v ON v.placa = 'BRA2E22'
WHERE p.email = 'maria.souza@exemplo.com'
  AND NOT EXISTS (SELECT 1 FROM historico_proprietario hp WHERE hp.id_veiculo = v.id_veiculo AND hp.proprietario_atual = TRUE AND hp.ativo = TRUE);

-- =========================================================
-- 10. Ordens de Serviço de demonstração
-- =========================================================
INSERT INTO ordem_servico (id_cliente, id_veiculo, numero_os, data_abertura, prioridade, valor_total, observacao)
SELECT c.id_cliente, v.id_veiculo, '1', CURRENT_TIMESTAMP - INTERVAL '5 days', 'NORMAL', 840.00, 'OS em orçamento para revisão simples com serviço terceirizado previsto.'
FROM cliente c JOIN pessoa p ON p.id_pessoa = c.id_pessoa JOIN veiculo v ON v.placa = 'ACC1234'
WHERE p.email = 'daviconcyline@gmail.com'
ON CONFLICT (numero_os) DO NOTHING;

INSERT INTO ordem_servico (id_cliente, id_veiculo, numero_os, data_abertura, prioridade, valor_total, observacao)
SELECT c.id_cliente, v.id_veiculo, '2', CURRENT_TIMESTAMP - INTERVAL '4 days', 'ALTA', 633.00, 'OS em execução com serviço mecânico e peças preventivas.'
FROM cliente c JOIN pessoa p ON p.id_pessoa = c.id_pessoa JOIN veiculo v ON v.placa = 'ONZ1170'
WHERE p.email = 'eugeniojuliomessala@gmail.com'
ON CONFLICT (numero_os) DO NOTHING;

INSERT INTO ordem_servico (id_cliente, id_veiculo, numero_os, data_abertura, prioridade, valor_total, observacao)
SELECT c.id_cliente, v.id_veiculo, '3', CURRENT_TIMESTAMP - INTERVAL '3 days', 'NORMAL', 491.00, 'OS aguardando quitação financeira.'
FROM cliente c JOIN pessoa p ON p.id_pessoa = c.id_pessoa JOIN veiculo v ON v.placa = 'TNU3J90'
WHERE p.email = 'financeiro@tecnoit.com.br'
ON CONFLICT (numero_os) DO NOTHING;

INSERT INTO ordem_servico (id_cliente, id_veiculo, numero_os, data_abertura, data_finalizacao, prioridade, valor_total, observacao)
SELECT c.id_cliente, v.id_veiculo, '4', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '2 days', 'URGENTE', 740.00, 'OS finalizada e quitada para teste de PDF e garantias.'
FROM cliente c JOIN pessoa p ON p.id_pessoa = c.id_pessoa JOIN veiculo v ON v.placa = 'PQX1354'
WHERE p.email = 'eugeniojuliomessala@gmail.com'
ON CONFLICT (numero_os) DO NOTHING;

INSERT INTO ordem_servico (id_cliente, id_veiculo, numero_os, data_abertura, data_finalizacao, prioridade, valor_total, observacao)
SELECT c.id_cliente, v.id_veiculo, '5', CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP - INTERVAL '1 day', 'NORMAL', 340.00, 'OS finalizada com serviços de alinhamento, balanceamento e cambagem.'
FROM cliente c JOIN pessoa p ON p.id_pessoa = c.id_pessoa JOIN veiculo v ON v.placa = 'BRA2E22'
WHERE p.email = 'maria.souza@exemplo.com'
ON CONFLICT (numero_os) DO NOTHING;

-- Histórico de status das OS.
INSERT INTO historico_status_ordem (id_ordem_servico, id_status_ordem_servico, data_status, observacao)
SELECT os.id_ordem_servico, s.id_status_ordem_servico, os.data_abertura, 'Status inicial registrado pelo seed.'
FROM ordem_servico os JOIN status_ordem_servico s ON s.nome_status = 'ORCAMENTO'
WHERE os.numero_os IN ('1', '2', '3', '4', '5')
  AND NOT EXISTS (SELECT 1 FROM historico_status_ordem h WHERE h.id_ordem_servico = os.id_ordem_servico AND h.id_status_ordem_servico = s.id_status_ordem_servico);

INSERT INTO historico_status_ordem (id_ordem_servico, id_status_ordem_servico, data_status, observacao)
SELECT os.id_ordem_servico, s.id_status_ordem_servico, os.data_abertura + INTERVAL '1 day', 'OS avançada para execução.'
FROM ordem_servico os JOIN status_ordem_servico s ON s.nome_status = 'EXECUCAO'
WHERE os.numero_os IN ('2', '3', '4', '5')
  AND NOT EXISTS (SELECT 1 FROM historico_status_ordem h WHERE h.id_ordem_servico = os.id_ordem_servico AND h.id_status_ordem_servico = s.id_status_ordem_servico);

INSERT INTO historico_status_ordem (id_ordem_servico, id_status_ordem_servico, data_status, observacao)
SELECT os.id_ordem_servico, s.id_status_ordem_servico, os.data_abertura + INTERVAL '2 days', 'OS aguardando pagamento.'
FROM ordem_servico os JOIN status_ordem_servico s ON s.nome_status = 'PAGAMENTO'
WHERE os.numero_os IN ('3', '4', '5')
  AND NOT EXISTS (SELECT 1 FROM historico_status_ordem h WHERE h.id_ordem_servico = os.id_ordem_servico AND h.id_status_ordem_servico = s.id_status_ordem_servico);

INSERT INTO historico_status_ordem (id_ordem_servico, id_status_ordem_servico, data_status, observacao)
SELECT os.id_ordem_servico, s.id_status_ordem_servico, COALESCE(os.data_finalizacao, CURRENT_TIMESTAMP), 'OS finalizada pelo seed completo.'
FROM ordem_servico os JOIN status_ordem_servico s ON s.nome_status = 'FINALIZADO'
WHERE os.numero_os IN ('4', '5')
  AND NOT EXISTS (SELECT 1 FROM historico_status_ordem h WHERE h.id_ordem_servico = os.id_ordem_servico AND h.id_status_ordem_servico = s.id_status_ordem_servico);

-- =========================================================
-- 11. Itens de serviço, peças, terceirização, garantias e pagamentos
-- =========================================================
INSERT INTO item_servico (id_ordem_servico, id_servico, id_colaborador, descricao_execucao, quantidade, valor_unitario, valor_total, data_inicio)
SELECT os.id_ordem_servico, s.id_servico, col.id_colaborador, 'Revisão preventiva cadastrada pelo seed.', 1, 340.00, 340.00, os.data_abertura
FROM ordem_servico os
JOIN servico s ON s.nome_servico = 'Revisão preventiva'
JOIN colaborador col ON col.id_pessoa = (SELECT id_pessoa FROM pessoa WHERE email = 'caio@avcar.local')
WHERE os.numero_os = '1'
  AND NOT EXISTS (SELECT 1 FROM item_servico i WHERE i.id_ordem_servico = os.id_ordem_servico AND i.id_servico = s.id_servico);


-- Serviço terceirizado em OS de orçamento: não possui colaborador interno e usa empresa padrão do serviço.
INSERT INTO item_servico (id_ordem_servico, id_servico, id_colaborador, descricao_execucao, quantidade, valor_unitario, valor_total)
SELECT os.id_ordem_servico, s.id_servico, NULL, 'Funilaria terceirizada prevista; empresa preenchida pelo cadastro do serviço.', 1, 500.00, 500.00
FROM ordem_servico os
JOIN servico s ON s.nome_servico = 'Funilaria terceirizada'
WHERE os.numero_os = '1'
  AND NOT EXISTS (SELECT 1 FROM item_servico i WHERE i.id_ordem_servico = os.id_ordem_servico AND i.id_servico = s.id_servico);

INSERT INTO execucao_servico_terceirizado (id_item_servico, id_empresa_terceirizada, data_envio, data_retorno, valor_cobrado, observacao)
SELECT its.id_item_servico,
       st.id_empresa_terceirizada_padrao,
       os.data_abertura + INTERVAL '1 day',
       os.data_abertura + INTERVAL '4 days',
       350.00,
       'Execução terceirizada gerada pelo seed usando empresa padrão do serviço.'
FROM item_servico its
JOIN ordem_servico os ON os.id_ordem_servico = its.id_ordem_servico
JOIN servico s ON s.id_servico = its.id_servico
JOIN servico_terceirizado st ON st.id_servico = s.id_servico
WHERE os.numero_os = '1'
  AND s.nome_servico = 'Funilaria terceirizada'
  AND NOT EXISTS (SELECT 1 FROM execucao_servico_terceirizado ext WHERE ext.id_item_servico = its.id_item_servico);

INSERT INTO item_servico (id_ordem_servico, id_servico, id_colaborador, descricao_execucao, quantidade, valor_unitario, valor_total, data_inicio)
SELECT os.id_ordem_servico, s.id_servico, col.id_colaborador, 'Serviços mecânicos gerais.', 1, 180.00, 180.00, os.data_abertura + INTERVAL '1 day'
FROM ordem_servico os
JOIN servico s ON s.nome_servico = 'Serviços mecânicos'
JOIN colaborador col ON col.id_pessoa = (SELECT id_pessoa FROM pessoa WHERE email = 'fabio@avcar.local')
WHERE os.numero_os = '2'
  AND NOT EXISTS (SELECT 1 FROM item_servico i WHERE i.id_ordem_servico = os.id_ordem_servico AND i.id_servico = s.id_servico);

INSERT INTO item_peca (id_ordem_servico, id_peca, id_fornecedor, quantidade, valor_unitario, valor_total, observacao)
SELECT os.id_ordem_servico, pe.id_peca, f.id_fornecedor, 1, 40.00, 40.00, 'Peça aplicada conforme orçamento.'
FROM ordem_servico os
JOIN peca pe ON pe.codigo_nacional = 'FCA0125'
JOIN fornecedor f ON f.id_fornecedor = pe.id_fornecedor_padrao
WHERE os.numero_os = '2'
  AND NOT EXISTS (SELECT 1 FROM item_peca ip WHERE ip.id_ordem_servico = os.id_ordem_servico AND ip.id_peca = pe.id_peca);

INSERT INTO item_peca (id_ordem_servico, id_peca, id_fornecedor, quantidade, valor_unitario, valor_total, observacao)
SELECT os.id_ordem_servico, pe.id_peca, f.id_fornecedor, 1, 58.00, 58.00, 'Peça aplicada conforme orçamento.'
FROM ordem_servico os
JOIN peca pe ON pe.codigo_nacional = 'ART8826'
JOIN fornecedor f ON f.id_fornecedor = pe.id_fornecedor_padrao
WHERE os.numero_os = '2'
  AND NOT EXISTS (SELECT 1 FROM item_peca ip WHERE ip.id_ordem_servico = os.id_ordem_servico AND ip.id_peca = pe.id_peca);

INSERT INTO item_peca (id_ordem_servico, id_peca, id_fornecedor, quantidade, valor_unitario, valor_total, observacao)
SELECT os.id_ordem_servico, pe.id_peca, f.id_fornecedor, 5, 55.00, 275.00, 'Lubrificante aplicado conforme especificação.'
FROM ordem_servico os
JOIN peca pe ON pe.codigo_nacional = 'OLEO5W30'
JOIN fornecedor f ON f.id_fornecedor = pe.id_fornecedor_padrao
WHERE os.numero_os = '2'
  AND NOT EXISTS (SELECT 1 FROM item_peca ip WHERE ip.id_ordem_servico = os.id_ordem_servico AND ip.id_peca = pe.id_peca);

INSERT INTO item_peca (id_ordem_servico, id_peca, id_fornecedor, quantidade, valor_unitario, valor_total, observacao)
SELECT os.id_ordem_servico, pe.id_peca, f.id_fornecedor, 1, 40.00, 40.00, 'Filtro aplicado conforme orçamento.'
FROM ordem_servico os
JOIN peca pe ON pe.codigo_nacional = 'FOL0113'
JOIN fornecedor f ON f.id_fornecedor = pe.id_fornecedor_padrao
WHERE os.numero_os = '2'
  AND NOT EXISTS (SELECT 1 FROM item_peca ip WHERE ip.id_ordem_servico = os.id_ordem_servico AND ip.id_peca = pe.id_peca);

INSERT INTO item_servico (id_ordem_servico, id_servico, id_colaborador, descricao_execucao, quantidade, valor_unitario, valor_total, data_inicio)
SELECT os.id_ordem_servico, s.id_servico, col.id_colaborador, 'Troca de óleo e inspeção básica.', 1, 120.00, 120.00, os.data_abertura + INTERVAL '1 day'
FROM ordem_servico os
JOIN servico s ON s.nome_servico = 'Troca de óleo'
JOIN colaborador col ON col.id_pessoa = (SELECT id_pessoa FROM pessoa WHERE email = 'almir@avcar.local')
WHERE os.numero_os = '3'
  AND NOT EXISTS (SELECT 1 FROM item_servico i WHERE i.id_ordem_servico = os.id_ordem_servico AND i.id_servico = s.id_servico);

INSERT INTO item_peca (id_ordem_servico, id_peca, id_fornecedor, quantidade, valor_unitario, valor_total, observacao)
SELECT os.id_ordem_servico, pe.id_peca, f.id_fornecedor, 3, 55.00, 165.00, 'Óleo 5W30 usado na manutenção.'
FROM ordem_servico os
JOIN peca pe ON pe.codigo_nacional = 'OLEO5W30'
JOIN fornecedor f ON f.id_fornecedor = pe.id_fornecedor_padrao
WHERE os.numero_os = '3'
  AND NOT EXISTS (SELECT 1 FROM item_peca ip WHERE ip.id_ordem_servico = os.id_ordem_servico AND ip.id_peca = pe.id_peca);

INSERT INTO item_peca (id_ordem_servico, id_peca, id_fornecedor, quantidade, valor_unitario, valor_total, observacao)
SELECT os.id_ordem_servico, pe.id_peca, f.id_fornecedor, 1, 40.00, 40.00, 'Filtro de óleo usado na manutenção.'
FROM ordem_servico os
JOIN peca pe ON pe.codigo_nacional = 'FOL0113'
JOIN fornecedor f ON f.id_fornecedor = pe.id_fornecedor_padrao
WHERE os.numero_os = '3'
  AND NOT EXISTS (SELECT 1 FROM item_peca ip WHERE ip.id_ordem_servico = os.id_ordem_servico AND ip.id_peca = pe.id_peca);

INSERT INTO item_peca (id_ordem_servico, id_peca, id_fornecedor, quantidade, valor_unitario, valor_total, observacao)
SELECT os.id_ordem_servico, pe.id_peca, f.id_fornecedor, 1, 166.00, 166.00, 'Peça complementar conforme orçamento.'
FROM ordem_servico os
JOIN peca pe ON pe.codigo_nacional = 'CORREIAMICROV'
JOIN fornecedor f ON f.id_fornecedor = pe.id_fornecedor_padrao
WHERE os.numero_os = '3'
  AND NOT EXISTS (SELECT 1 FROM item_peca ip WHERE ip.id_ordem_servico = os.id_ordem_servico AND ip.id_peca = pe.id_peca);

INSERT INTO item_servico (id_ordem_servico, id_servico, id_colaborador, descricao_execucao, quantidade, valor_unitario, valor_total, data_inicio, data_fim)
SELECT os.id_ordem_servico, s.id_servico, col.id_colaborador, 'Serviços mecânicos finalizados.', 1, 280.00, 280.00, os.data_abertura + INTERVAL '1 day', os.data_finalizacao
FROM ordem_servico os
JOIN servico s ON s.nome_servico = 'Serviços mecânicos'
JOIN colaborador col ON col.id_pessoa = (SELECT id_pessoa FROM pessoa WHERE email = 'almir@avcar.local')
WHERE os.numero_os = '4'
  AND NOT EXISTS (SELECT 1 FROM item_servico i WHERE i.id_ordem_servico = os.id_ordem_servico AND i.id_servico = s.id_servico);

INSERT INTO item_peca (id_ordem_servico, id_peca, id_fornecedor, quantidade, valor_unitario, valor_total, observacao)
SELECT os.id_ordem_servico, pe.id_peca, f.id_fornecedor, 1, 460.00, 460.00, 'Peça aplicada em OS finalizada.'
FROM ordem_servico os
JOIN peca pe ON pe.codigo_nacional = 'RADIADOR522201'
JOIN fornecedor f ON f.id_fornecedor = pe.id_fornecedor_padrao
WHERE os.numero_os = '4'
  AND NOT EXISTS (SELECT 1 FROM item_peca ip WHERE ip.id_ordem_servico = os.id_ordem_servico AND ip.id_peca = pe.id_peca);

INSERT INTO item_servico (id_ordem_servico, id_servico, id_colaborador, descricao_execucao, quantidade, valor_unitario, valor_total, data_inicio, data_fim)
SELECT os.id_ordem_servico, s.id_servico, col.id_colaborador, 'Alinhamento finalizado.', 1, 180.00, 180.00, os.data_abertura + INTERVAL '1 day', os.data_finalizacao
FROM ordem_servico os
JOIN servico s ON s.nome_servico = 'Alinhamento'
JOIN colaborador col ON col.id_pessoa = (SELECT id_pessoa FROM pessoa WHERE email = 'rafael@avcar.local')
WHERE os.numero_os = '5'
  AND NOT EXISTS (SELECT 1 FROM item_servico i WHERE i.id_ordem_servico = os.id_ordem_servico AND i.id_servico = s.id_servico);

INSERT INTO item_servico (id_ordem_servico, id_servico, id_colaborador, descricao_execucao, quantidade, valor_unitario, valor_total, data_inicio, data_fim)
SELECT os.id_ordem_servico, s.id_servico, col.id_colaborador, 'Balanceamento finalizado.', 1, 40.00, 40.00, os.data_abertura + INTERVAL '1 day', os.data_finalizacao
FROM ordem_servico os
JOIN servico s ON s.nome_servico = 'Balanceamento'
JOIN colaborador col ON col.id_pessoa = (SELECT id_pessoa FROM pessoa WHERE email = 'rafael@avcar.local')
WHERE os.numero_os = '5'
  AND NOT EXISTS (SELECT 1 FROM item_servico i WHERE i.id_ordem_servico = os.id_ordem_servico AND i.id_servico = s.id_servico);

INSERT INTO item_servico (id_ordem_servico, id_servico, id_colaborador, descricao_execucao, quantidade, valor_unitario, valor_total, data_inicio, data_fim)
SELECT os.id_ordem_servico, s.id_servico, col.id_colaborador, 'Cambagem finalizada.', 1, 120.00, 120.00, os.data_abertura + INTERVAL '1 day', os.data_finalizacao
FROM ordem_servico os
JOIN servico s ON s.nome_servico = 'Cambagem'
JOIN colaborador col ON col.id_pessoa = (SELECT id_pessoa FROM pessoa WHERE email = 'rafael@avcar.local')
WHERE os.numero_os = '5'
  AND NOT EXISTS (SELECT 1 FROM item_servico i WHERE i.id_ordem_servico = os.id_ordem_servico AND i.id_servico = s.id_servico);

-- Garantias de serviços: aguardando finalização quando a OS ainda não foi finalizada; vigente quando finalizada.
INSERT INTO garantia_servico (id_item_servico, prazo_dias, data_inicio, data_fim, status_garantia, observacao)
SELECT its.id_item_servico,
       s.prazo_garantia_dias,
       CASE WHEN os.data_finalizacao IS NOT NULL THEN os.data_finalizacao::DATE ELSE NULL END,
       CASE WHEN os.data_finalizacao IS NOT NULL THEN (os.data_finalizacao::DATE + s.prazo_garantia_dias) ELSE NULL END,
       CASE WHEN os.data_finalizacao IS NOT NULL THEN 'VIGENTE' ELSE 'AGUARDANDO_FINALIZACAO_OS' END,
       'Garantia de serviço gerada pelo seed completo.'
FROM item_servico its
JOIN servico s ON s.id_servico = its.id_servico
JOIN ordem_servico os ON os.id_ordem_servico = its.id_ordem_servico
WHERE NOT EXISTS (SELECT 1 FROM garantia_servico gs WHERE gs.id_item_servico = its.id_item_servico);

-- Garantias de peças: fornecedor é responsável por padrão.
INSERT INTO garantia_peca (id_item_peca, prazo_dias, data_inicio, data_fim, responsabilidade, status_garantia, observacao)
SELECT ip.id_item_peca,
       pe.prazo_garantia_dias,
       CASE WHEN os.data_finalizacao IS NOT NULL THEN os.data_finalizacao::DATE ELSE NULL END,
       CASE WHEN os.data_finalizacao IS NOT NULL THEN (os.data_finalizacao::DATE + pe.prazo_garantia_dias) ELSE NULL END,
       'FORNECEDOR',
       CASE WHEN os.data_finalizacao IS NOT NULL THEN 'VIGENTE' ELSE 'AGUARDANDO_FINALIZACAO_OS' END,
       'Garantia de peça gerada pelo seed completo.'
FROM item_peca ip
JOIN peca pe ON pe.id_peca = ip.id_peca
JOIN ordem_servico os ON os.id_ordem_servico = ip.id_ordem_servico
WHERE NOT EXISTS (SELECT 1 FROM garantia_peca gp WHERE gp.id_item_peca = ip.id_item_peca);


-- Demonstração do atendimento de garantia: uma garantia acionada e uma encerrada.
UPDATE garantia_servico gs
SET status_garantia = 'ACIONADA',
    data_acionamento = COALESCE(gs.data_inicio, CURRENT_DATE),
    motivo_acionamento = 'Cliente retornou relatando falha no serviço dentro do prazo de garantia.',
    descricao_defeito = 'Falha relatada após execução do serviço, aguardando análise técnica.',
    responsavel_analise = 'Almir Pinto da Silva',
    observacao = COALESCE(gs.observacao, '') || E'\nGarantia acionada pelo seed para demonstração do atendimento.'
FROM item_servico its
JOIN ordem_servico os ON os.id_ordem_servico = its.id_ordem_servico
WHERE gs.id_item_servico = its.id_item_servico
  AND os.numero_os = '4'
  AND gs.status_garantia = 'VIGENTE';

UPDATE garantia_peca gp
SET status_garantia = 'ENCERRADA',
    data_acionamento = COALESCE(gp.data_inicio, CURRENT_DATE),
    motivo_acionamento = 'Cliente retornou relatando defeito em peça aplicada.',
    descricao_defeito = 'Peça apresentou comportamento irregular durante o prazo de garantia.',
    responsavel_analise = 'Fabio Biziack',
    data_encerramento = COALESCE(gp.data_inicio, CURRENT_DATE) + 1,
    solucao_aplicada = 'Peça substituída e funcionamento conferido pela oficina.',
    custo_assumido_por = 'FORNECEDOR',
    atendimento_realizado = TRUE,
    observacao = COALESCE(gp.observacao, '') || E'\nGarantia encerrada pelo seed para demonstração do atendimento.'
FROM item_peca ip
JOIN ordem_servico os ON os.id_ordem_servico = ip.id_ordem_servico
WHERE gp.id_item_peca = ip.id_item_peca
  AND os.numero_os = '4'
  AND gp.status_garantia = 'VIGENTE';

-- Pagamentos: OS 3 parcial, OS 4 e OS 5 quitadas.
INSERT INTO pagamento (id_ordem_servico, forma_pagamento, valor_pago, data_pagamento, status_pagamento, observacao)
SELECT os.id_ordem_servico, 'PIX', 200.00, CURRENT_TIMESTAMP - INTERVAL '1 day', 'PAGO', 'Pagamento parcial de demonstração.'
FROM ordem_servico os
WHERE os.numero_os = '3'
  AND NOT EXISTS (SELECT 1 FROM pagamento p WHERE p.id_ordem_servico = os.id_ordem_servico AND p.valor_pago = 200.00 AND p.forma_pagamento = 'PIX');

INSERT INTO pagamento (id_ordem_servico, forma_pagamento, valor_pago, data_pagamento, status_pagamento, observacao)
SELECT os.id_ordem_servico, 'PIX', 740.00, os.data_finalizacao, 'PAGO', 'Pagamento integral da OS finalizada.'
FROM ordem_servico os
WHERE os.numero_os = '4'
  AND NOT EXISTS (SELECT 1 FROM pagamento p WHERE p.id_ordem_servico = os.id_ordem_servico AND p.valor_pago = 740.00 AND p.forma_pagamento = 'PIX');

INSERT INTO pagamento (id_ordem_servico, forma_pagamento, valor_pago, data_pagamento, status_pagamento, observacao)
SELECT os.id_ordem_servico, 'CARTAO_DEBITO', 340.00, os.data_finalizacao, 'PAGO', 'Pagamento integral da OS finalizada.'
FROM ordem_servico os
WHERE os.numero_os = '5'
  AND NOT EXISTS (SELECT 1 FROM pagamento p WHERE p.id_ordem_servico = os.id_ordem_servico AND p.valor_pago = 340.00 AND p.forma_pagamento = 'CARTAO_DEBITO');

COMMIT;
