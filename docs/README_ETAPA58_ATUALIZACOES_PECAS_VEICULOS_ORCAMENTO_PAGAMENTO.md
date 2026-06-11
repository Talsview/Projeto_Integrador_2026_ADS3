# Etapa 58 — Atualizações em Peças, Fornecedores, Veículos, Orçamento e Pagamentos

## Objetivo

Esta etapa ajusta pontos operacionais observados durante os testes do sistema, reforçando a rastreabilidade entre peça e fornecedor, melhorando a usabilidade do cadastro de veículos, automatizando valores no orçamento da OS e simplificando a tela de pagamentos.

## Alterações realizadas

### 1. Peças vinculadas ao fornecedor padrão

A entidade `PecaModel` passou a possuir vínculo direto com um fornecedor padrão por meio do campo `fornecedorPadrao`.

Com isso, ao cadastrar ou editar uma peça, o usuário informa:

- fornecedor padrão;
- valor unitário padrão;
- garantia em dias;
- dados de aplicação da peça.

Essa alteração melhora a regra de rastreabilidade porque, ao selecionar uma peça no orçamento da OS, o sistema já identifica automaticamente o fornecedor relacionado.

### 2. Valor unitário padrão da peça

Foi criado o campo `valorUnitarioPadrao` no cadastro de peça. Na tela de Itens da OS, quando a peça é selecionada, o campo de valor unitário é preenchido automaticamente com esse valor cadastrado.

Ao alterar a quantidade, o total da peça é recalculado automaticamente.

### 3. Fornecedor automático no item de peça da OS

Na tela de Itens da Ordem de Serviço, o fornecedor não precisa mais ser escolhido manualmente. Ele é carregado a partir da peça selecionada.

A regra permanece compatível com o modelo de dados validado:

```text
OrdemServico utiliza ItemPeca.
Peca é aplicada em ItemPeca.
Fornecedor fornece ItemPeca.
```

Ou seja, a OS continua registrando qual fornecedor foi responsável pela peça aplicada.

### 4. Valor automático no item de serviço da OS

Ao selecionar um serviço cadastrado, o sistema preenche automaticamente o valor unitário com o `valorBase` do serviço.

Ao alterar a quantidade, o total do serviço é recalculado automaticamente.

### 5. Busca de cliente no cadastro de veículo

Na tela de Veículos, foi incluído um campo de busca acima de `Proprietário atual`.

O usuário pode pesquisar por:

- nome do cliente;
- CPF;
- CNPJ.

Após a busca, a combobox de proprietário exibe somente os clientes filtrados, facilitando o cadastro quando há muitos clientes cadastrados.

### 6. Quilometragem em branco no cadastro de veículo

O formulário de veículo não inicia mais a quilometragem com `0` automaticamente.

Agora o campo fica em branco, permitindo que o usuário informe a quilometragem real somente quando tiver essa informação.

O backend também foi ajustado para aceitar `null` na quilometragem, mantendo a regra de que, quando informada, ela não pode ser negativa.

### 7. Remoção do botão “Marcar pago”

Na tela de Pagamentos, foi removido o botão `Marcar pago` da tabela de pagamentos da OS.

O fluxo correto agora fica mais limpo:

1. OS entra em pagamento.
2. Usuário registra o pagamento no formulário.
3. Backend calcula o valor pago e pendente.
4. Quando a OS for quitada, o sistema finaliza automaticamente conforme a regra financeira implementada.

## Alterações no banco de dados

Como o projeto usa:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

é necessário executar o script incremental antes de iniciar o backend atualizado em banco já existente:

```text
database/01_schema/04_alter_peca_fornecedor_valor.sql
```

Esse script adiciona:

```text
peca.id_fornecedor_padrao
peca.valor_unitario_padrao
```

Também adiciona a chave estrangeira para `fornecedor` e a restrição de valor não negativo.

## Arquivos principais alterados

```text
src/main/java/br/com/avcar/oficina/business/peca/model/PecaModel.java
src/main/java/br/com/avcar/oficina/business/peca/dto/PecaDTO.java
src/main/java/br/com/avcar/oficina/business/peca/mapper/PecaMapper.java
src/main/java/br/com/avcar/oficina/business/peca/service/PecaService.java
src/main/java/br/com/avcar/oficina/business/peca/service/ItemPecaService.java
src/main/java/br/com/avcar/oficina/business/ordemservico/service/ItemServicoService.java
src/main/java/br/com/avcar/oficina/business/veiculo/model/VeiculoModel.java
src/main/java/br/com/avcar/oficina/business/veiculo/mapper/VeiculoMapper.java
frontend/oficina-web/src/app/models/peca.model.ts
frontend/oficina-web/src/app/pages/pecas-fornecedores/pecas-fornecedores.component.ts
frontend/oficina-web/src/app/pages/pecas-fornecedores/pecas-fornecedores.component.html
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.ts
frontend/oficina-web/src/app/pages/itens-os/itens-os.component.html
frontend/oficina-web/src/app/pages/veiculos/veiculos.component.ts
frontend/oficina-web/src/app/pages/veiculos/veiculos.component.html
frontend/oficina-web/src/app/pages/pagamentos/pagamentos.component.html
database/01_schema/04_alter_peca_fornecedor_valor.sql
```

## Resultado esperado

Após esta etapa:

- peça cadastrada possui fornecedor padrão;
- peça cadastrada possui valor unitário padrão;
- orçamento da OS puxa valor de serviço automaticamente;
- orçamento da OS puxa valor e fornecedor da peça automaticamente;
- totais de serviço e peça são recalculados quando a quantidade muda;
- cadastro de veículo possui busca de proprietário;
- quilometragem não inicia mais com zero visualmente;
- pagamento ficou mais simples, sem botão redundante de marcar pago.
