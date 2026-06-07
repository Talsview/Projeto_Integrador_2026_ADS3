# Etapa 38 — Auditoria final e correção global de validações por campo

## Objetivo

Esta etapa reforça as validações de campos e regras críticas do sistema AV CAR AUTO CENTER, mantendo a estratégia de três camadas:

1. Angular valida a entrada para melhorar a experiência do usuário;
2. Spring Boot valida as regras reais de negócio e integridade;
3. PostgreSQL preserva a consistência por meio de chaves, restrições e scripts.

## Ajustes aplicados no frontend Angular

### Empresas terceirizadas

- CNPJ passou a ter máscara automática no formato `00.000.000/0000-00`.
- CNPJ informado precisa ser válido pelo cálculo dos dígitos verificadores.
- Telefone mantém máscara e bloqueio de caracteres não numéricos.
- Campos inválidos são destacados na tela com mensagem próxima ao campo.

### Peças e fornecedores

- CNPJ do fornecedor recebeu máscara e validação real.
- Telefone do fornecedor mantém máscara e validação.
- Peças passaram a exibir erros por campo para nome, marca, modelo aplicável, anos e prazo de garantia.

### Veículos

- Placa é normalizada automaticamente para maiúsculo e aceita somente os formatos `ABC1234` e `ABC1D23`.
- Chassi é normalizado automaticamente, removendo caracteres inválidos e impedindo `I`, `O` e `Q`.
- Campos de ano, quilometragem, proprietário e data de posse exibem erro por campo.
- Modelos são filtrados conforme a marca selecionada.

### Ordens de Serviço

- A tela filtra os veículos conforme o cliente selecionado.
- O sistema impede abrir OS com veículo que não pertence ao cliente informado como proprietário atual.
- A regra também está no backend para impedir fraude por API, Swagger ou Postman.

### Serviços e peças da OS

- Serviços terceirizados exigem empresa terceirizada.
- Serviços internos não aceitam empresa terceirizada vinculada.
- Datas de início, fim, envio e retorno não podem ser futuras.
- Retorno da terceirização não pode ser anterior ao envio.
- Valor da terceirização não pode ser negativo.
- Peças continuam exigindo fornecedor identificado.

### Garantias

- A interface desabilita acionamento de garantias que não estejam vigentes.
- A interface desabilita encerramento de garantias que ainda não estejam acionadas.
- O backend continua mantendo a validação definitiva.

## Ajustes aplicados no backend Spring Boot

### Ordem de Serviço

Foi adicionada validação de vínculo entre cliente e veículo na abertura e atualização da OS.

Regra aplicada:

```text
A Ordem de Serviço só pode ser aberta se o veículo selecionado pertencer ao cliente informado como proprietário atual.
```

Arquivos envolvidos:

```text
src/main/java/br/com/avcar/oficina/business/ordemservico/service/OrdemServicoService.java
src/main/java/br/com/avcar/oficina/business/veiculo/repository/IHistoricoProprietarioRepository.java
```

### Nota fiscal / recibo interno

A geração de nota fiscal/recibo interno final passou a exigir:

- OS existente e ativa;
- cliente válido;
- veículo válido;
- serviços ou peças registrados;
- histórico de status;
- OS finalizada;
- valor total consistente;
- pagamento quitado.

A emissão continua sendo documento interno simplificado, não substituindo NF-e/NFS-e oficial.

## Arquivos principais alterados

```text
frontend/oficina-web/src/app/core/validation/field-validation.ts
frontend/oficina-web/src/app/pages/empresas-terceirizadas/
frontend/oficina-web/src/app/pages/pecas-fornecedores/
frontend/oficina-web/src/app/pages/veiculos/
frontend/oficina-web/src/app/pages/ordens-servico/
frontend/oficina-web/src/app/pages/itens-os/
frontend/oficina-web/src/app/pages/garantias/
frontend/oficina-web/src/styles.css
src/main/java/br/com/avcar/oficina/business/ordemservico/service/OrdemServicoService.java
src/main/java/br/com/avcar/oficina/business/notafiscal/service/NotaFiscalPdfService.java
```

## Testes recomendados

1. Tentar digitar letras no telefone de cliente, fornecedor e empresa terceirizada.
2. Tentar informar CNPJ inválido em fornecedor e empresa terceirizada.
3. Tentar abrir OS com cliente e veículo de outro proprietário.
4. Tentar cadastrar chassi com as letras I, O ou Q.
5. Tentar informar placa fora dos formatos aceitos.
6. Tentar lançar serviço terceirizado sem empresa terceirizada.
7. Tentar gerar nota PDF de uma OS não finalizada ou não quitada.
8. Tentar acionar garantia expirada ou aguardando finalização.

## Resultado esperado

A aplicação deve impedir dados incorretos antes do envio sempre que possível e, obrigatoriamente, bloquear regras inválidas no backend.
