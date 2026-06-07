# ADR-050 — Relatórios, Configurações e Microinterações

**Status:** Aceito  
**Data:** 15/06/2026

## Contexto

Após a finalização dos módulos operacionais, as abas de Relatórios e Configurações ainda estavam com baixo valor funcional. Além disso, o formulário de Ordem de Serviço exibia o campo de número da OS mesmo sendo gerado automaticamente pelo backend.

## Decisão

Remover o campo de número da OS da tela de cadastro, mantendo a regra de geração automática no backend. Transformar Relatórios e Configurações em telas úteis para operação e apresentação acadêmica. Adicionar microinterações visuais discretas e apoio à leitura de textos longos em campos e botões.

## Justificativa

A interface fica mais limpa e evita que o usuário tente alterar uma informação controlada pelo sistema. Relatórios e Configurações passam a apoiar o gerenciamento da oficina e a apresentação do sistema. As microinterações melhoram a usabilidade sem alterar regras de negócio.

## Consequências

- A tela de OS fica mais objetiva.
- Relatórios passam a consumir dados reais da API.
- Configurações passam a exibir ambiente local, Swagger e preferências.
- Textos longos em campos e botões ficam mais fáceis de visualizar.

## Mitigações

As animações podem ser reduzidas por preferência local ou por configuração do sistema operacional via prefers-reduced-motion.
