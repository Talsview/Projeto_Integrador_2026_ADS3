# ADR-007 — Aplicação do Adapter nas respostas de Veículo

## Status

Aceita.

## Contexto

O modelo de dados da oficina define que o proprietário do veículo não deve ser armazenado diretamente na tabela `veiculo`. A posse é controlada pela entidade associativa `historico_proprietario`, permitindo que o sistema preserve todos os proprietários anteriores e identifique o proprietário atual.

Entretanto, a View Angular precisa receber uma resposta mais simples e consolidada, contendo placa, marca, modelo, proprietário atual e histórico de proprietários.

## Decisão

Aplicar o padrão de projeto Adapter no módulo Veículo, por meio da classe:

```text
VeiculoResponseAdapter
```

## Implementação

O adapter converte a estrutura interna:

```text
VeiculoModel
ModeloModel
MarcaModel
HistoricoProprietarioModel
ClienteModel
PessoaModel
```

em respostas próprias para a API:

```text
VeiculoDTO
VeiculoResumoDTO
HistoricoProprietarioDTO
```

## Consequências positivas

```text
1. A Controller não precisa conhecer detalhes da composição interna do domínio.
2. O Service consegue devolver uma resposta adequada ao Angular sem quebrar o modelo conceitual.
3. A relação histórica Cliente-Veículo permanece preservada.
4. O terceiro padrão de projeto exigido pela disciplina fica identificado no código.
```

## Consequências negativas

```text
1. Existe uma classe adicional no módulo Veículo.
2. É necessário manter o adapter atualizado caso os DTOs de resposta mudem.
```
