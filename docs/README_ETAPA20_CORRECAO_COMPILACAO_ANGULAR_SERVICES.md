# Etapa 20 — Correção de compilação Angular nos Services especializados

## 1. Objetivo

Esta etapa corrige um erro de compilação do Angular identificado durante a execução do frontend no VS Code, especificamente nos serviços especializados de Ordem de Serviço e Pagamento.

O erro ocorria porque os services `OrdemServicoApiService` e `PagamentoApiService` declaravam novamente a propriedade `tempoLimiteMs`, já existente na classe base `BaseApiService`.

## 2. Problema identificado

O Angular/TypeScript apresentou os seguintes erros:

```text
TS2415: Class incorrectly extends base class.
Types have separate declarations of a private property 'tempoLimiteMs'.

TS4114: This member must have an 'override' modifier because it overrides a member in the base class.
```

## 3. Causa técnica

A propriedade `tempoLimiteMs` estava declarada como `private` dentro de `BaseApiService` e também era declarada novamente nas subclasses.

Em TypeScript, membros `private` não podem ser sobrescritos por subclasses como se fossem o mesmo atributo herdado, pois cada declaração privada pertence exclusivamente à classe onde foi criada.

## 4. Correção aplicada

A propriedade `tempoLimiteMs` foi alterada em `BaseApiService` de `private` para `protected`, permitindo que os services especializados utilizem o mesmo tempo limite definido na classe base.

Além disso, a declaração duplicada foi removida de:

```text
frontend/oficina-web/src/app/core/services/ordem-servico-api.service.ts
frontend/oficina-web/src/app/core/services/pagamento-api.service.ts
```

## 5. Arquivos alterados

```text
frontend/oficina-web/src/app/core/services/base-api.service.ts
frontend/oficina-web/src/app/core/services/ordem-servico-api.service.ts
frontend/oficina-web/src/app/core/services/pagamento-api.service.ts
```

## 6. Impacto da correção

A correção mantém o reaproveitamento da classe genérica `BaseApiService`, reduz duplicidade de código e permite que os services especializados continuem usando `timeout` nas chamadas HTTP sem violar as regras de herança do TypeScript.

## 7. Como testar

No VS Code, executar:

```powershell
cd C:\Users\Davi\Documents\NetBeansProjects\car-repair\frontend\oficina-web
npm.cmd install
npm.cmd start
```

Ou, usando proxy para integração com o backend:

```powershell
npm.cmd run start:proxy
```

## 8. Observação acadêmica

A correção reforça a organização por camadas no frontend, mantendo a classe base responsável pelas operações comuns de comunicação HTTP e os services específicos responsáveis apenas pelas rotas particulares de cada módulo.
