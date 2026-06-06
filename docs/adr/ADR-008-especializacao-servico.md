# ADR-008 — Especialização de Serviço em Interno e Terceirizado

## Status

Aceita.

## Contexto

O MER validado do projeto define que `Servico` deve se especializar em `ServicoInterno` e `ServicoTerceirizado`, com generalização exclusiva e total. Isso significa que todo serviço cadastrado precisa pertencer a exatamente uma dessas especializações.

Além disso, os serviços terceirizados não removem a responsabilidade da oficina perante o cliente. A empresa externa apenas executa parte do trabalho, enquanto a rastreabilidade e a responsabilidade continuam registradas no sistema da oficina.

## Decisão

Foi adotada a estratégia de uma tabela para cada entidade da hierarquia:

```text
servico
servico_interno
servico_terceirizado
```

A entidade `servico` armazena os atributos comuns, enquanto `servico_interno` e `servico_terceirizado` registram as informações específicas de cada classificação.

## Consequências

A solução mantém fidelidade ao MER, facilita a validação acadêmica do modelo e prepara o sistema para a composição futura da Ordem de Serviço por meio de `ItemServico` e `ExecucaoServicoTerceirizado`.
