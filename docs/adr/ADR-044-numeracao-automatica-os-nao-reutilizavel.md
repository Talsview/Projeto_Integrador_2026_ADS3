# ADR-044 — Numeração automática e não reutilizável das Ordens de Serviço

## Status

Aceita.

## Contexto

Durante a validação do sistema, foi identificado que o campo “Número da OS” ainda permitia digitação manual pelo usuário. Essa abordagem poderia causar inconsistências, duplicidades, lacunas mal interpretadas e perda de rastreabilidade, especialmente quando uma Ordem de Serviço fosse inativada.

Em uma oficina mecânica, o número da Ordem de Serviço possui valor documental e operacional. Por esse motivo, ele deve ser controlado pelo sistema, e não pelo usuário.

## Decisão

A numeração da Ordem de Serviço será gerada automaticamente no backend, de forma sequencial e sem reutilização de números já existentes.

A tela Angular não permitirá a digitação do número da OS. Para novas ordens, será exibida a mensagem de que o número será gerado automaticamente ao salvar. Para ordens existentes, o número será exibido apenas para consulta.

A geração considera todas as Ordens de Serviço registradas na tabela, inclusive as inativas. Assim, se uma OS for inativada, seu número permanece reservado e não poderá ser usado por outra OS.

## Consequências

### Positivas

- Preserva rastreabilidade documental.
- Evita duplicidade de numeração.
- Evita reutilização de número de OS inativa.
- Reduz erro humano no cadastro.
- Facilita auditoria e conferência de documentos gerados em PDF.

### Negativas

- O usuário não poderá escolher manualmente um número de OS antigo.
- Caso haja necessidade de importar OS históricas com numeração específica, isso deverá ser feito por script controlado e validado.

## Arquivos envolvidos

```text
OrdemServicoService.java
OrdemServicoMapper.java
IOrdemServicoRepository.java
OrdemServicoValidation.java
ordens-servico.component.html
```
