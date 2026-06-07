# ADR-041 — Auditoria final de validações por campo e regras críticas

## Status

Aceita.

## Contexto

Após a implementação de CPF/CNPJ reais e validações iniciais por campo, foi realizada uma auditoria no sistema para identificar lacunas entre a experiência da interface Angular e as validações de domínio do backend Spring Boot.

Foram identificados pontos em que a API já bloqueava a regra, mas a interface ainda permitia digitação ou seleção inconsistente até o momento do salvamento.

## Decisão

Padronizar as validações em duas camadas:

1. Angular deve formatar, normalizar e sinalizar campos inválidos visualmente.
2. Spring Boot deve manter a validação definitiva das regras de negócio.

Foram reforçadas as validações de CNPJ em fornecedores e empresas terceirizadas, placa, chassi, cliente/veículo na OS, terceirização, garantias e emissão de nota/recibo interno.

## Consequências

- A experiência do usuário melhora porque erros aparecem antes do envio.
- O backend continua protegido contra chamadas externas inválidas.
- A Ordem de Serviço passa a respeitar melhor a rastreabilidade entre cliente e veículo.
- A nota/recibo interno passa a ser emitida somente quando a OS estiver finalizada e quitada.

## Arquivos impactados

```text
frontend/oficina-web/src/app/core/validation/field-validation.ts
frontend/oficina-web/src/app/pages/empresas-terceirizadas/
frontend/oficina-web/src/app/pages/pecas-fornecedores/
frontend/oficina-web/src/app/pages/veiculos/
frontend/oficina-web/src/app/pages/ordens-servico/
frontend/oficina-web/src/app/pages/itens-os/
frontend/oficina-web/src/app/pages/garantias/
src/main/java/br/com/avcar/oficina/business/ordemservico/service/OrdemServicoService.java
src/main/java/br/com/avcar/oficina/business/notafiscal/service/NotaFiscalPdfService.java
```
